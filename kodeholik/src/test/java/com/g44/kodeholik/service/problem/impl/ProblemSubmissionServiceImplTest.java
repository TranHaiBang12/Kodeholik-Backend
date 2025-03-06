package com.g44.kodeholik.service.problem.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.model.dto.request.lambda.TestCase;
import com.g44.kodeholik.model.dto.request.problem.ProblemCompileRequestDto;
import com.g44.kodeholik.model.dto.response.problem.submission.SubmissionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.run.RunProblemResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.AcceptedSubmissionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.CompileErrorResposneDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.FailedSubmissionResponseDto;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSubmission;
import com.g44.kodeholik.model.entity.problem.ProblemTemplate;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.problem.SubmissionStatus;
import com.g44.kodeholik.repository.problem.ProblemRepository;
import com.g44.kodeholik.repository.problem.ProblemSubmissionRepository;
import com.g44.kodeholik.service.aws.lambda.LambdaService;
import com.g44.kodeholik.service.setting.LanguageService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.mapper.response.problem.ProblemSubmissionMapper;
import com.g44.kodeholik.util.mapper.response.problem.SubmissionListResponseMapper;
import com.google.gson.Gson;

@ExtendWith(MockitoExtension.class)
public class ProblemSubmissionServiceImplTest {

    @Mock
    private ProblemSubmissionRepository problemSubmissionRepository;

    @Mock
    private LambdaService lambdaService;

    @Mock
    private UserService userService;

    @Mock
    private LanguageService languageService;

    @Mock
    private ProblemRepository problemRepository;

    @Mock
    private ProblemSubmissionMapper problemSubmissionMapper;

    @Mock
    private SubmissionListResponseMapper submissionListResponseMapper;

    @InjectMocks
    private ProblemSubmissionServiceImpl problemSubmissionService;

    private Gson gson = new Gson();

    private Problem problem;
    private ProblemCompileRequestDto problemCompileRequestDto;
    private List<TestCase> testCases;
    private ProblemTemplate problemTemplate;
    private Users user;

    @BeforeEach
    public void setUp() {
        problem = new Problem();
        problem.setId(1L);
        problem.setNoSubmission(0);
        problem.setAcceptanceRate(0.0f);

        problemCompileRequestDto = new ProblemCompileRequestDto();
        problemCompileRequestDto.setCode("some code");
        problemCompileRequestDto.setLanguageName("Java");

        testCases = Arrays.asList(new TestCase(), new TestCase());

        problemTemplate = new ProblemTemplate();
        problemTemplate.setFunctionSignature("someFunctionSignature");
        problemTemplate.setTemplateCode("public int someFunction()");

        user = new Users();
        user.setId(1L);
    }

    @Test
    public void testSubmitProblemAccepted() {
        String lambdaResult = "{\"isAccepted\":true,\"time\":\"1.0\",\"memoryUsage\":\"128\",\"noSuccessTestcase\":2,\"results\":[]}";
        when(lambdaService.invokeLambdaFunction(any())).thenReturn(lambdaResult);
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(languageService.findByName(anyString())).thenReturn(new Language());

        SubmissionResponseDto response = problemSubmissionService.submitProblem(problem, problemCompileRequestDto,
                testCases, problemTemplate);

        assertTrue(response instanceof AcceptedSubmissionResponseDto);
        assertEquals(SubmissionStatus.SUCCESS, response.getStatus());
        verify(problemSubmissionRepository, times(1)).save(any(ProblemSubmission.class));
        verify(problemRepository, times(1)).save(any(Problem.class));
    }

    @Test
    public void testRunProblemAccepted() {
        String lambdaResult = "{\"isAccepted\":true,\"time\":\"1.0\",\"memoryUsage\":\"128\",\"noSuccessTestcase\":2,\"results\":[]}";
        when(lambdaService.invokeLambdaFunction(any())).thenReturn(lambdaResult);

        RunProblemResponseDto response = problemSubmissionService.run(problem, problemCompileRequestDto,
                testCases, problemTemplate);

        assertEquals(SubmissionStatus.SUCCESS, response.getStatus());
    }

    @Test
    public void testRunProblemFailed() {
        String lambdaResult = "{\"isAccepted\":false,\"time\":\"1.0\",\"memoryUsage\":\"128\",\"noSuccessTestcase\":2,\"results\":[]}";
        when(lambdaService.invokeLambdaFunction(any())).thenReturn(lambdaResult);

        RunProblemResponseDto response = problemSubmissionService.run(problem, problemCompileRequestDto,
                testCases, problemTemplate);

        assertEquals(SubmissionStatus.FAILED, response.getStatus());
    }

    @Test
    public void testRunProblemCompileError() {
        String lambdaResult = "Compile Error";
        when(lambdaService.invokeLambdaFunction(any())).thenReturn(lambdaResult);

        RunProblemResponseDto response = problemSubmissionService.run(problem, problemCompileRequestDto,
                testCases, problemTemplate);

        assertEquals("Compile Error", response.getMessage());
    }

    @Test
    public void testSubmitProblemFailed() {
        String lambdaResult = "{\"isAccepted\":false,\"time\":\"1.0\",\"memoryUsage\":\"128\",\"noSuccessTestcase\":1,\"results\":[]}";
        when(lambdaService.invokeLambdaFunction(any())).thenReturn(lambdaResult);
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(languageService.findByName(anyString())).thenReturn(new Language());

        SubmissionResponseDto response = problemSubmissionService.submitProblem(problem, problemCompileRequestDto,
                testCases, problemTemplate);

        assertTrue(response instanceof FailedSubmissionResponseDto);
        assertEquals(SubmissionStatus.FAILED, response.getStatus());
        verify(problemSubmissionRepository, times(1)).save(any(ProblemSubmission.class));
        verify(problemRepository, times(1)).save(any(Problem.class));
    }

    @Test
    public void testSubmitProblemCompileError() {
        String lambdaResult = "Compile Error";
        when(lambdaService.invokeLambdaFunction(any())).thenReturn(lambdaResult);
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(languageService.findByName(anyString())).thenReturn(new Language());

        SubmissionResponseDto response = problemSubmissionService.submitProblem(problem, problemCompileRequestDto,
                testCases, problemTemplate);

        assertTrue(response instanceof CompileErrorResposneDto);
        assertEquals(SubmissionStatus.FAILED, response.getStatus());
        verify(problemSubmissionRepository, times(1)).save(any(ProblemSubmission.class));
        verify(problemRepository, times(1)).save(any(Problem.class));
    }

    @Test
    public void testSubmitProblemCodeEmpty() {
        problemCompileRequestDto.setCode("");
        assertThrows(BadRequestException.class, () -> {
            problemSubmissionService.submitProblem(problem, problemCompileRequestDto, testCases, problemTemplate);
        });
    }
}