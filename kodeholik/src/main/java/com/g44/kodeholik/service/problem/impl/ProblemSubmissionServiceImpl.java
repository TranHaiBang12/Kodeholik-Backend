package com.g44.kodeholik.service.problem.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.g44.kodeholik.model.dto.request.problem.ProblemCompileRequestDto;
import com.g44.kodeholik.model.dto.response.problem.submission.AcceptedSubmissionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.CompileErrorResposneDto;
import com.g44.kodeholik.model.dto.response.problem.submission.FailedSubmissionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.SubmissionResponseDto;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSubmission;
import com.g44.kodeholik.model.entity.problem.ProblemTemplate;
import com.g44.kodeholik.model.enums.problem.InputType;
import com.g44.kodeholik.repository.problem.ProblemSubmissionRepository;
import com.g44.kodeholik.service.aws.lambda.LambdaService;
import com.g44.kodeholik.service.problem.ProblemService;
import com.g44.kodeholik.service.problem.ProblemSubmissionService;
import com.g44.kodeholik.service.problem.ProblemTemplateService;
import com.g44.kodeholik.service.problem.ProblemTestCaseService;
import com.g44.kodeholik.service.setting.LanguageService;
import com.g44.kodeholik.service.user.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.g44.kodeholik.model.dto.request.lambda.LambdaRequest;
import com.g44.kodeholik.model.dto.request.lambda.ResponseResult;
import com.g44.kodeholik.model.dto.request.lambda.TestCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProblemSubmissionServiceImpl implements ProblemSubmissionService {

    private final ProblemSubmissionRepository problemSubmissionRepository;

    private final LambdaService lambdaService;

    private final ProblemTestCaseService problemTestCaseService;

    private final ProblemTemplateService problemTemplateService;

    private final ProblemService problemService;

    private final UserService userService;

    private final LanguageService languageService;

    private Gson gson = new Gson();

    @Override
    public SubmissionResponseDto submitProblem(Long problemId, ProblemCompileRequestDto problemCompileRequestDto) {
        Problem problem = problemService.getProblemById(problemId);
        List<TestCase> testCases = problemTestCaseService.getTestCaseByProblemId(problemId);
        for (int i = 0; i < testCases.size(); i++) {
            for (int j = 0; j < testCases.get(i).getInput().size(); j++) {
                log.info(testCases.get(i).getInput().get(j).getValue());
            }
        }
        String languageName = problemCompileRequestDto.getLanguageName();
        ProblemTemplate problemTemplate = problemTemplateService.findByProblemAndLanguage(problem, languageName);
        String functionSignature = problemTemplate.getFunctionSignature();
        InputType inputType = problemTemplate.getReturnType();
        LambdaRequest lambdaRequest = new LambdaRequest(
                languageName,
                problemCompileRequestDto.getCode(),
                functionSignature,
                inputType.toString(),
                testCases);
        log.info(lambdaRequest);
        String result = lambdaService.invokeLambdaFunction(lambdaRequest);
        log.info(result);
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
                break;
            case "FAILED":
                submissionResponseDto = new FailedSubmissionResponseDto(
                        responseResult.getNoSuccessTestcase(),
                        testCases.size(),
                        responseResult.getInputWrong(),
                        problemCompileRequestDto.getCode(),
                        languageName,
                        Timestamp.from(Instant.now()));
                problemSubmission.setAccepted(false);
                problemSubmission.setInputWrong(gson.toJson(responseResult.getInputWrong()));
                break;
            default:
                submissionResponseDto = new CompileErrorResposneDto(
                        status,
                        problemCompileRequestDto.getCode(),
                        languageName,
                        Timestamp.from(Instant.now()));
                problemSubmission.setAccepted(false);
                problemSubmission.setStatus(status);
                break;
        }
        problemSubmission.setProblem(problem);
        problemSubmission.setUser(userService.getUserById(1L));
        problemSubmission.setCode(problemCompileRequestDto.getCode());
        problemSubmission
                .setLanguage(languageService.findByName(languageName));
        problemSubmission.setCreatedAt(Timestamp.from(Instant.now()));
        problemSubmission.setExecutionTime(Double.parseDouble(responseResult.getTime()));
        problemSubmission.setMemoryUsage(responseResult.getMemoryUsage());
        problemSubmissionRepository.save(problemSubmission);
        return submissionResponseDto;
    }

}
