package com.g44.kodeholik.service.problem.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.lambda.LambdaRequest;
import com.g44.kodeholik.model.dto.request.lambda.ResponseResult;
import com.g44.kodeholik.model.dto.request.lambda.TestCase;
import com.g44.kodeholik.model.dto.request.problem.ProblemCompileRequestDto;
import com.g44.kodeholik.model.dto.response.problem.submission.ProblemSubmissionDto;
import com.g44.kodeholik.model.dto.response.problem.submission.SubmissionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.run.RunProblemResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.AcceptedSubmissionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.CompileErrorResposneDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.FailedSubmissionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.SubmissionListResponseDto;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSubmission;
import com.g44.kodeholik.model.entity.problem.ProblemTemplate;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.problem.InputType;
import com.g44.kodeholik.model.enums.problem.SubmissionStatus;
import com.g44.kodeholik.repository.problem.ProblemRepository;
import com.g44.kodeholik.repository.problem.ProblemSubmissionRepository;
import com.g44.kodeholik.service.aws.lambda.LambdaService;
import com.g44.kodeholik.service.problem.ProblemSubmissionService;
import com.g44.kodeholik.service.setting.LanguageService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.mapper.response.problem.ProblemSubmissionMapper;
import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProblemSubmissionServiceImpl implements ProblemSubmissionService {

    private final ProblemSubmissionRepository problemSubmissionRepository;

    private final LambdaService lambdaService;

    private final UserService userService;

    private final LanguageService languageService;

    private final ProblemRepository problemRepository;

    private final ProblemSubmissionMapper problemSubmissionMapper;

    private Gson gson = new Gson();

    @Override
    public SubmissionResponseDto submitProblem(Problem problem, ProblemCompileRequestDto problemCompileRequestDto,
            List<TestCase> testCases, ProblemTemplate problemTemplate) {
        if (problemCompileRequestDto.getCode().isEmpty()) {
            throw new BadRequestException("Code is required", "Code is required");
        }
        String languageName = problemCompileRequestDto.getLanguageName();
        String functionSignature = problemTemplate.getFunctionSignature();
        InputType inputType = problemTemplate.getReturnType();
        LambdaRequest lambdaRequest = new LambdaRequest(
                languageName,
                problemCompileRequestDto.getCode(),
                functionSignature,
                inputType.toString(),
                testCases);
        String result = lambdaService.invokeLambdaFunction(lambdaRequest);
        String status = "";
        ResponseResult responseResult = new ResponseResult();
        ProblemSubmission problemSubmission = new ProblemSubmission();

        SubmissionResponseDto submissionResponseDto = null;
        try {
            responseResult = gson.fromJson(result, ResponseResult.class);
            if (responseResult.isAccepted()) {
                status = "ACCEPTED";
            } else {
                status = "FAILED";
            }
        } catch (Exception e) {
            status = result;
        }
        switch (status) {
            case "ACCEPTED":
                submissionResponseDto = new AcceptedSubmissionResponseDto(
                        responseResult.getTime(),
                        responseResult.getMemoryUsage(),
                        problemCompileRequestDto.getCode(),
                        languageName,
                        responseResult.getNoSuccessTestcase(),
                        Timestamp.from(Instant.now()));
                problemSubmission.setAccepted(true);
                problemSubmission.setStatus(SubmissionStatus.SUCCESS);
                break;
            case "FAILED":
                submissionResponseDto = new FailedSubmissionResponseDto(
                        responseResult.getNoSuccessTestcase(),
                        testCases.size(),
                        responseResult.getInputWrong(),
                        problemCompileRequestDto.getCode(),
                        languageName,
                        Timestamp.from(Instant.now()));
                problemSubmission.setStatus(SubmissionStatus.FAILED);
                problemSubmission.setAccepted(false);
                problemSubmission.setInputWrong(gson.toJson(responseResult.getInputWrong()));
                break;
            default:
                submissionResponseDto = new CompileErrorResposneDto(
                        status,
                        problemCompileRequestDto.getCode(),
                        languageName,
                        Timestamp.from(Instant.now()));
                problemSubmission.setStatus(SubmissionStatus.FAILED);
                problemSubmission.setAccepted(false);
                problemSubmission.setMessage(status);
                break;
        }
        problemSubmission.setProblem(problem);
        problemSubmission.setNoTestCasePassed(responseResult.getNoSuccessTestcase());
        problemSubmission.setUser(userService.getUserById(1L));
        problemSubmission.setCode(problemCompileRequestDto.getCode());
        problemSubmission
                .setLanguage(languageService.findByName(languageName));
        problemSubmission.setCreatedAt(Timestamp.from(Instant.now()));
        if (responseResult.getTime() != null)
            problemSubmission.setExecutionTime(Double.parseDouble(responseResult.getTime()));
        else
            problemSubmission.setExecutionTime(0);
        problemSubmission.setMemoryUsage(responseResult.getMemoryUsage());
        problemSubmissionRepository.save(problemSubmission);
        problem.setNoSubmission(problem.getNoSubmission() + 1);
        double acceptanceRate = ((double) getNumberAcceptedSubmission(problem)
                / (double) problem.getNoSubmission())
                * 100;
        BigDecimal roundAcceptanceRate = new BigDecimal(acceptanceRate).setScale(2, RoundingMode.HALF_UP);
        problem.setAcceptanceRate(roundAcceptanceRate.floatValue());
        problemRepository.save(problem);
        return submissionResponseDto;
    }

    @Override
    public RunProblemResponseDto run(Problem problem, ProblemCompileRequestDto problemCompileRequestDto,
            List<TestCase> testCases, ProblemTemplate problemTemplate) {
        if (problemCompileRequestDto.getTestCases() != null && !problemCompileRequestDto.getTestCases().isEmpty()) {
            List<TestCase> runTestCase = problemCompileRequestDto.getTestCases();
            for (int i = 0; i < runTestCase.size(); i++) {
                for (int j = 0; j < testCases.size(); j++) {
                    if (testCases.get(j).getInput() != runTestCase.get(i).getInput()) {
                        testCases.add(runTestCase.get(i));
                    }
                }
            }
        }
        if (problemCompileRequestDto.getCode().isEmpty()) {
            throw new BadRequestException("Code is required", "Code is required");
        }
        String languageName = problemCompileRequestDto.getLanguageName();
        String functionSignature = problemTemplate.getFunctionSignature();
        InputType inputType = problemTemplate.getReturnType();
        LambdaRequest lambdaRequest = new LambdaRequest(
                languageName,
                problemCompileRequestDto.getCode(),
                functionSignature,
                inputType.toString(),
                testCases);
        String result = lambdaService.invokeLambdaFunction(lambdaRequest);
        String status = "";
        ResponseResult responseResult = new ResponseResult();
        RunProblemResponseDto runProblemResponseDto = new RunProblemResponseDto();
        try {
            responseResult = gson.fromJson(result, ResponseResult.class);
            if (responseResult.isAccepted()) {
                status = "ACCEPTED";
            } else {
                status = "FAILED";
            }
        } catch (Exception e) {
            status = result;
        }
        runProblemResponseDto.setResults(responseResult.getResults());
        switch (status) {
            case "ACCEPTED":
                runProblemResponseDto.setStatus(SubmissionStatus.SUCCESS);
                runProblemResponseDto.setAccepted(true);
                break;
            case "FAILED":
                runProblemResponseDto.setStatus(SubmissionStatus.FAILED);
                runProblemResponseDto.setAccepted(true);
                break;
            default:
                runProblemResponseDto.setMessage(status);
                runProblemResponseDto.setStatus(SubmissionStatus.FAILED);
                runProblemResponseDto.setAccepted(false);
                break;
        }
        return runProblemResponseDto;
    }

    @Override
    public long getNumberAcceptedSubmission(Problem problem) {
        return problemSubmissionRepository.countByIsAcceptedAndProblem(true, problem);
    }

    @Override
    public boolean checkIsCurrentUserSolvedProblem(Problem problem) {
        Users currentUser = userService.getCurrentUser();
        log.info(
                !(problemSubmissionRepository.findByUserAndProblemAndIsAccepted(currentUser, problem, true).isEmpty()));
        return !(problemSubmissionRepository.findByUserAndProblemAndIsAccepted(currentUser, problem, true).isEmpty());
    }

    @Override
    public Long countByIsAcceptedAndProblem(boolean isAccepted, Problem problem) {
        return problemSubmissionRepository.countByIsAcceptedAndProblem(isAccepted, problem);
    }

    @Override
    public long countByUserAndIsAcceptedAndProblemIn(Users user, boolean isAccepted, List<Problem> problems) {
        return problemSubmissionRepository.countByUserAndIsAcceptedAndProblemIn(user, isAccepted, problems);
    }

    @Override
    public ProblemSubmissionDto getSubmissionDtoById(Long submissionId) {
        ProblemSubmission problemSubmission = problemSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> new NotFoundException("Submission not found", "Submission not found"));
        return problemSubmissionMapper.mapFrom(problemSubmission);
    }

    @Override
    public List<SubmissionListResponseDto> getListSubmission(Problem problem, Users user) {
        List<ProblemSubmission> problemSubmissions = problemSubmissionRepository.findByUserAndProblem(user, problem);
        List<SubmissionListResponseDto> submissionListResponseDtos = new ArrayList();
        for (int i = 0; i < problemSubmissions.size(); i++) {
            SubmissionListResponseDto submissionListResponseDto = new SubmissionListResponseDto();
            submissionListResponseDto.setExecutionTime(problemSubmissions.get(i).getExecutionTime());
            submissionListResponseDto.setMemoryUsage(problemSubmissions.get(i).getMemoryUsage());
            submissionListResponseDto.setStatus(problemSubmissions.get(i).getStatus());
            submissionListResponseDto.setLanguageName(problemSubmissions.get(i).getLanguage().getName());
            submissionListResponseDto.setCreatedAt(problemSubmissions.get(i).getCreatedAt().getTime());
            submissionListResponseDtos.add(submissionListResponseDto);
        }
        return submissionListResponseDtos;
    }

}
