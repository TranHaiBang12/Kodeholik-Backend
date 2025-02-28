package com.g44.kodeholik.service.problem.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.exception.ForbiddenException;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.lambda.LambdaRequest;
import com.g44.kodeholik.model.dto.request.lambda.ResponseResult;
import com.g44.kodeholik.model.dto.request.lambda.TestCase;
import com.g44.kodeholik.model.dto.request.lambda.TestResult;
import com.g44.kodeholik.model.dto.request.problem.ProblemCompileRequestDto;
import com.g44.kodeholik.model.dto.response.problem.submission.ProblemSubmissionDto;
import com.g44.kodeholik.model.dto.response.problem.submission.SubmissionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.run.RunProblemResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.AcceptedSubmissionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.CompileErrorResposneDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.FailedSubmissionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.SubmissionListResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.SuccessSubmissionListResponseDto;
import com.g44.kodeholik.model.dto.response.user.ProblemProgressResponseDto;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSubmission;
import com.g44.kodeholik.model.entity.problem.ProblemTemplate;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.problem.InputType;
import com.g44.kodeholik.model.enums.problem.SubmissionStatus;
import com.g44.kodeholik.model.enums.user.ProgressType;
import com.g44.kodeholik.repository.problem.ProblemRepository;
import com.g44.kodeholik.repository.problem.ProblemSubmissionRepository;
import com.g44.kodeholik.service.aws.lambda.LambdaService;
import com.g44.kodeholik.service.problem.ProblemSubmissionService;
import com.g44.kodeholik.service.setting.LanguageService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.mapper.response.problem.ProblemSubmissionMapper;
import com.g44.kodeholik.util.mapper.response.problem.SubmissionListResponseMapper;
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

    private final SubmissionListResponseMapper submissionListResponseMapper;

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
        log.info(responseResult.getResults());

        switch (status) {
            case "ACCEPTED":
                submissionResponseDto = new AcceptedSubmissionResponseDto(
                        responseResult.getTime(),
                        responseResult.getMemoryUsage(),
                        problemCompileRequestDto.getCode(),
                        languageName,
                        responseResult.getNoSuccessTestcase(),
                        Timestamp.from(Instant.now()),
                        SubmissionStatus.SUCCESS);
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
                        Timestamp.from(Instant.now()),
                        SubmissionStatus.FAILED);
                problemSubmission.setStatus(SubmissionStatus.FAILED);
                problemSubmission.setAccepted(false);
                problemSubmission.setInputWrong(gson.toJson(responseResult.getInputWrong()));
                break;
            default:
                submissionResponseDto = new CompileErrorResposneDto(
                        status,
                        problemCompileRequestDto.getCode(),
                        languageName,
                        Timestamp.from(Instant.now()),
                        SubmissionStatus.FAILED);
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
        log.info(responseResult.getResults());

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
        return !(problemSubmissionRepository.findByUserAndProblemAndIsAccepted(currentUser, problem, true).isEmpty());
    }

    public boolean checkIsUserSolvedProblem(Users user, Problem problem) {
        return !(problemSubmissionRepository.findByUserAndProblemAndIsAccepted(user, problem, true).isEmpty());
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
            submissionListResponseDto.setId(problemSubmissions.get(i).getId());
            submissionListResponseDto.setProblemTitle(problemSubmissions.get(i).getProblem().getTitle());
            submissionListResponseDto.setProblemLink(problemSubmissions.get(i).getProblem().getLink());
            submissionListResponseDto.setExecutionTime(problemSubmissions.get(i).getExecutionTime());
            submissionListResponseDto.setMemoryUsage(problemSubmissions.get(i).getMemoryUsage());
            submissionListResponseDto.setStatus(problemSubmissions.get(i).getStatus());
            submissionListResponseDto.setLanguageName(problemSubmissions.get(i).getLanguage().getName());
            submissionListResponseDto.setCreatedAt(problemSubmissions.get(i).getCreatedAt().getTime());
            submissionListResponseDtos.add(submissionListResponseDto);
        }
        return submissionListResponseDtos;
    }

    @Override
    public List<SuccessSubmissionListResponseDto> getSuccessSubmissionList(List<Long> excludes, Problem problem,
            Users user) {
        List<ProblemSubmission> problemSubmissions = problemSubmissionRepository.findByUserAndProblemAndIsAccepted(user,
                problem, true);
        List<SuccessSubmissionListResponseDto> successSubmissionListResponseDtos = new ArrayList();
        for (int i = 0; i < problemSubmissions.size(); i++) {
            SuccessSubmissionListResponseDto successSubmissionListResponseDto = new SuccessSubmissionListResponseDto();
            if (!excludes.contains(problemSubmissions.get(i).getId())) {
                successSubmissionListResponseDto.setId(problemSubmissions.get(i).getId());
                successSubmissionListResponseDto.setLanguageName(problemSubmissions.get(i).getLanguage().getName());
                successSubmissionListResponseDto.setCreatedAt(problemSubmissions.get(i).getCreatedAt().getTime());
                successSubmissionListResponseDtos.add(successSubmissionListResponseDto);
            }
        }
        return successSubmissionListResponseDtos;
    }

    @Override
    public SubmissionResponseDto getSubmissionDetail(ProblemSubmission problemSubmission, int noTestCase,
            Users currentUser) {
        if (currentUser.getId() != problemSubmission.getUser().getId()) {
            throw new ForbiddenException("You are not allowed to view this submission",
                    "You are not allowed to view this submission");
        }
        String status = "";
        if (problemSubmission.isAccepted()) {
            status = "ACCEPTED";
        } else {
            status = "FAILED";
        }
        SubmissionResponseDto submissionResponseDto;
        switch (status) {
            case "ACCEPTED":
                submissionResponseDto = new AcceptedSubmissionResponseDto(
                        String.valueOf(problemSubmission.getExecutionTime()),
                        problemSubmission.getMemoryUsage(),
                        problemSubmission.getCode(),
                        problemSubmission.getLanguage().getName(),
                        problemSubmission.getNoTestCasePassed(),
                        problemSubmission.getCreatedAt(),
                        SubmissionStatus.SUCCESS);
                break;
            case "FAILED":
                submissionResponseDto = new FailedSubmissionResponseDto(
                        problemSubmission.getNoTestCasePassed(),
                        noTestCase,
                        gson.fromJson(problemSubmission.getInputWrong(), TestResult.class),
                        problemSubmission.getCode(),
                        problemSubmission.getLanguage().getName(),
                        Timestamp.from(Instant.now()),
                        SubmissionStatus.FAILED);
                break;
            default:
                submissionResponseDto = new CompileErrorResposneDto(
                        problemSubmission.getMessage(),
                        problemSubmission.getCode(),
                        problemSubmission.getLanguage().getName(),
                        Timestamp.from(Instant.now()),
                        SubmissionStatus.FAILED);
                break;
        }
        return submissionResponseDto;
    }

    @Override
    public ProblemSubmission getProblemSubmissionById(Long submissionId) {
        return problemSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> new NotFoundException("Submission not found", "Submission not found"));
    }

    @Override
    public Page<SubmissionListResponseDto> getListSubmission(
            Users user,
            Problem problem,
            SubmissionStatus status,
            Date start,
            Date end,
            int page,
            Integer size,
            String sortBy,
            Boolean ascending) {
        if ((start != null && end == null) || (start == null && end != null)) {
            throw new BadRequestException("Start and end date must be provided together",
                    "Start and end date must be provided together");
        }
        if ((start != null && end != null) && start.after(end)) {
            throw new BadRequestException("Start date cannot be after end date", "Start date cannot be after end date");
        }
        Pageable pageable;
        if (sortBy != null
                && (sortBy.equals("createdAt") || sortBy.equals("executionTime") || sortBy.equals("memoryUsage"))) {
            Sort sort = ascending.booleanValue() ? Sort.by(sortBy.toString()).ascending()
                    : Sort.by(sortBy.toString()).descending();
            pageable = PageRequest.of(page, size == null ? 5 : size.intValue(), sort);
        } else {
            pageable = PageRequest.of(page, size == null ? 5 : size.intValue());
        }
        Timestamp startTimestamp = start != null ? new Timestamp(start.getTime())
                : Timestamp.valueOf("1970-01-01 00:00:00");
        Timestamp endTimestamp = end != null ? new Timestamp(end.getTime()) : Timestamp.valueOf("2100-01-01 00:00:00");
        Page<ProblemSubmission> problemSubmissions = problemSubmissionRepository
                .findByUserAndTimeBetween(
                        user,
                        problem,
                        status,

                        startTimestamp,
                        endTimestamp,
                        pageable);
        return problemSubmissions.map(submissionListResponseMapper::mapFrom);
    }

    @Override
    public Map<String, String> getAllProblemHasSubmitted(Users currentUser) {
        Map<String, String> problemHasSubmitted = new HashMap<>();
        List<Problem> problems = problemSubmissionRepository.findByUserAndProblemDistinct(currentUser);
        for (int i = 0; i < problems.size(); i++) {
            problemHasSubmitted.put(problems.get(i).getTitle(), problems.get(i).getLink());
        }
        return problemHasSubmitted;
    }

    @Override
    public Page<ProblemProgressResponseDto> findLastSubmittedByUser(
            Users user,
            SubmissionStatus status, int page, Integer size, String sortBy, Boolean ascending) {
        if (page < 0) {
            throw new BadRequestException("Page must be greater than 0", "Page must be greater than 0");
        }
        Pageable pageable;
        if (sortBy != null
                && (sortBy.equals("createdAt") || sortBy.equals("noSubmission"))) {
            if (sortBy.equals("noSubmission"))
                sortBy = "problem.noSubmission";
            Sort sort = ascending.booleanValue() ? Sort.by(sortBy.toString()).ascending()
                    : Sort.by(sortBy.toString()).descending();
            pageable = PageRequest.of(page, size == null ? 5 : size.intValue(), sort);
        } else {
            pageable = PageRequest.of(page, size == null ? 5 : size.intValue());
        }
        Page<Object[]> lastSubmitted;
        if (status == null) {
            lastSubmitted = problemSubmissionRepository.findLastSubmittedByUserAndProblemIn(user, pageable);
        } else {
            if (status == SubmissionStatus.SUCCESS) {
                lastSubmitted = problemSubmissionRepository.findLastSubmittedByUserAndProblemInAndSuccessStatus(user,
                        pageable);
            } else {
                lastSubmitted = problemSubmissionRepository.findLastSubmittedByUserAndProblemInAndFailedStatus(user,
                        pageable);
            }
        }
        List<ProblemProgressResponseDto> problemProgressResponseDtos = new ArrayList<>();
        for (Object[] obj : lastSubmitted) {
            ProblemProgressResponseDto problemProgressResponseDto = new ProblemProgressResponseDto();
            Problem problem = (Problem) obj[0];
            if (checkIsUserSolvedProblem(user, problem)) {
                problemProgressResponseDto.setProgressType(ProgressType.SOLVED);
            } else {
                problemProgressResponseDto.setProgressType(ProgressType.ATTEMPTED);
            }
            problemProgressResponseDto.setProblemTitle(problem.getTitle());
            problemProgressResponseDto.setProblemLink(problem.getLink());
            problemProgressResponseDto.setDifficulty(problem.getDifficulty());
            problemProgressResponseDto.setLastSubmitted(((Timestamp) obj[2]).getTime());
            problemProgressResponseDto.setNoSubmission(problem.getNoSubmission());
            problemProgressResponseDtos.add(problemProgressResponseDto);
        }
        return new PageImpl<>(problemProgressResponseDtos, pageable, lastSubmitted.getTotalElements());
    }

}
