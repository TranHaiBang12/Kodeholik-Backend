package com.g44.kodeholik.service.problem.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.exception.ForbiddenException;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.lambda.TestCase;
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
import com.g44.kodeholik.model.enums.problem.Difficulty;
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
                Users user = new Users();
                user.setId(1L);
                String lambdaResult = "{\"isAccepted\":true,\"time\":\"1.0\",\"memoryUsage\":\"128\",\"noSuccessTestcase\":2,\"results\":[]}";
                when(lambdaService.invokeLambdaFunction(any())).thenReturn(lambdaResult);
                when(userService.getUserById(anyLong())).thenReturn(user);
                when(languageService.findByName(anyString())).thenReturn(new Language());

                SubmissionResponseDto response = problemSubmissionService.submitProblem(problem,
                                problemCompileRequestDto,
                                testCases, problemTemplate, user);

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
                Users user = new Users();
                user.setId(1L);
                String lambdaResult = "{\"isAccepted\":false,\"time\":\"1.0\",\"memoryUsage\":\"128\",\"noSuccessTestcase\":1,\"results\":[]}";
                when(lambdaService.invokeLambdaFunction(any())).thenReturn(lambdaResult);
                when(userService.getUserById(anyLong())).thenReturn(user);
                when(languageService.findByName(anyString())).thenReturn(new Language());

                SubmissionResponseDto response = problemSubmissionService.submitProblem(problem,
                                problemCompileRequestDto,
                                testCases, problemTemplate, user);

                assertTrue(response instanceof FailedSubmissionResponseDto);
                assertEquals(SubmissionStatus.FAILED, response.getStatus());
                verify(problemSubmissionRepository, times(1)).save(any(ProblemSubmission.class));
                verify(problemRepository, times(1)).save(any(Problem.class));
        }

        @Test
        public void testSubmitProblemCompileError() {
                Users user = new Users();
                user.setId(1L);
                String lambdaResult = "Compile Error";
                when(lambdaService.invokeLambdaFunction(any())).thenReturn(lambdaResult);
                when(userService.getUserById(anyLong())).thenReturn(user);
                when(languageService.findByName(anyString())).thenReturn(new Language());

                SubmissionResponseDto response = problemSubmissionService.submitProblem(problem,
                                problemCompileRequestDto,
                                testCases, problemTemplate, user);

                assertTrue(response instanceof CompileErrorResposneDto);
                assertEquals(SubmissionStatus.FAILED, response.getStatus());
                verify(problemSubmissionRepository, times(1)).save(any(ProblemSubmission.class));
                verify(problemRepository, times(1)).save(any(Problem.class));
        }

        @Test
        public void testSubmitProblemCodeEmpty() {
                Users user = new Users();
                user.setId(1L);
                problemCompileRequestDto.setCode("");
                assertThrows(BadRequestException.class, () -> {
                        problemSubmissionService.submitProblem(problem, problemCompileRequestDto, testCases,
                                        problemTemplate, user);
                });
        }

        @Test
        public void testGetSubmissionDtoByIdSuccess() {
                ProblemSubmissionDto problemSubmissionDto = new ProblemSubmissionDto();
                problemSubmissionDto.setId(1L);
                problemSubmissionDto.setCode("test code");

                ProblemSubmission problemSubmission = new ProblemSubmission();
                problemSubmission.setId(1L);
                problemSubmission.setCode("test code");

                when(problemSubmissionRepository.findById(anyLong())).thenReturn(Optional.of(problemSubmission));
                when(problemSubmissionMapper.mapFrom(any(ProblemSubmission.class))).thenReturn(problemSubmissionDto);

                ProblemSubmissionDto response = problemSubmissionService.getSubmissionDtoById(1L);

                assertNotNull(response);
                assertEquals(problemSubmissionDto.getId(), response.getId());
                assertEquals(problemSubmissionDto.getCode(), response.getCode());
        }

        @Test
        public void testGetSubmissionDtoByIdNotFound() {
                when(problemSubmissionRepository.findById(anyLong())).thenReturn(Optional.empty());

                NotFoundException notFoundException = assertThrows(NotFoundException.class,
                                () -> problemSubmissionService.getSubmissionDtoById(1L));

                assertEquals("Submission not found", notFoundException.getMessage());
                assertEquals("Submission not found", notFoundException.getDetails());
        }

        @Test
        public void testGetListSubmission() {
                Users user = new Users();
                user.setId(1L);

                Language language = new Language();
                language.setName("Java");

                Problem problem = new Problem();
                problem.setId(1L);
                problem.setTitle("test title");
                problem.setLink("test link");

                ProblemSubmission problemSubmission = new ProblemSubmission();
                problemSubmission.setId(1L);
                problemSubmission.setCode("test code");
                problemSubmission.setProblem(problem);
                problemSubmission.setExecutionTime(0.1);
                problemSubmission.setMemoryUsage(0.5);
                problemSubmission.setUser(user);
                problemSubmission.setLanguage(language);
                problemSubmission.setCreatedAt(Timestamp.from(Instant.now()));

                List<ProblemSubmission> problemSubmissionList = new ArrayList<>();
                problemSubmissionList.add(problemSubmission);

                when(problemSubmissionRepository.findByUserAndProblem(any(Users.class), any(Problem.class)))
                                .thenReturn(problemSubmissionList);

                List<SubmissionListResponseDto> submissionListResponseDtos = problemSubmissionService.getListSubmission(
                                problem,
                                user);

                assertNotNull(submissionListResponseDtos);
                assertEquals(1, submissionListResponseDtos.size());
                assertEquals("test title", submissionListResponseDtos.get(0).getProblemTitle());
                assertEquals("test link", submissionListResponseDtos.get(0).getProblemLink());
                assertEquals(0.1, submissionListResponseDtos.get(0).getExecutionTime());
                assertEquals(0.5, submissionListResponseDtos.get(0).getMemoryUsage());
        }

        @Test
        public void testGetSuccessListSubmission() {
                Users user = new Users();
                user.setId(1L);

                Language language = new Language();
                language.setName("Java");

                Problem problem = new Problem();
                problem.setId(1L);
                problem.setTitle("test title");
                problem.setLink("test link");

                ProblemSubmission problemSubmission = new ProblemSubmission();
                problemSubmission.setId(1L);
                problemSubmission.setCode("test code");
                problemSubmission.setProblem(problem);
                problemSubmission.setExecutionTime(0.1);
                problemSubmission.setMemoryUsage(0.5);
                problemSubmission.setUser(user);
                problemSubmission.setLanguage(language);
                problemSubmission.setCreatedAt(Timestamp.from(Instant.now()));
                problemSubmission.setAccepted(true);
                problemSubmission.setStatus(SubmissionStatus.SUCCESS);

                List<ProblemSubmission> problemSubmissionList = new ArrayList<>();
                problemSubmissionList.add(problemSubmission);

                when(problemSubmissionRepository.findByUserAndProblemAndIsAccepted(any(Users.class), any(Problem.class),
                                anyBoolean()))
                                .thenReturn(problemSubmissionList);

                List<SuccessSubmissionListResponseDto> submissionListResponseDtos = problemSubmissionService
                                .getSuccessSubmissionList(
                                                new ArrayList(), problem,
                                                user);

                assertNotNull(submissionListResponseDtos);
                assertEquals(1, submissionListResponseDtos.size());
                assertEquals(1L, submissionListResponseDtos.get(0).getId());
                assertEquals("Java", submissionListResponseDtos.get(0).getLanguageName());
                assertEquals(problemSubmission.getCreatedAt().getTime(),
                                submissionListResponseDtos.get(0).getCreatedAt());
        }

        @Test
        public void testGetSuccessListSubmissionWithExclude() {
                Users user = new Users();
                user.setId(1L);

                Language language = new Language();
                language.setName("Java");

                Problem problem = new Problem();
                problem.setId(1L);
                problem.setTitle("test title");
                problem.setLink("test link");

                ProblemSubmission problemSubmission = new ProblemSubmission();
                problemSubmission.setId(1L);
                problemSubmission.setCode("test code");
                problemSubmission.setProblem(problem);
                problemSubmission.setExecutionTime(0.1);
                problemSubmission.setMemoryUsage(0.5);
                problemSubmission.setUser(user);
                problemSubmission.setLanguage(language);
                problemSubmission.setCreatedAt(Timestamp.from(Instant.now()));
                problemSubmission.setAccepted(true);
                problemSubmission.setStatus(SubmissionStatus.SUCCESS);

                List<ProblemSubmission> problemSubmissionList = new ArrayList<>();
                problemSubmissionList.add(problemSubmission);

                when(problemSubmissionRepository.findByUserAndProblemAndIsAccepted(any(Users.class), any(Problem.class),
                                anyBoolean()))
                                .thenReturn(problemSubmissionList);

                List<SuccessSubmissionListResponseDto> submissionListResponseDtos = problemSubmissionService
                                .getSuccessSubmissionList(
                                                List.of(1L),
                                                problem,
                                                user);

                assertNotNull(submissionListResponseDtos);
                assertTrue(submissionListResponseDtos.isEmpty());
        }

        @Test
        public void testGetSubmissionDetailAccepted() {
                Users user = new Users();
                user.setId(1L);

                Language language = new Language();
                language.setName("Java");

                Problem problem = new Problem();
                problem.setId(1L);
                problem.setTitle("test title");
                problem.setLink("test link");

                ProblemSubmission problemSubmission = new ProblemSubmission();
                problemSubmission.setId(1L);
                problemSubmission.setCode("test code");
                problemSubmission.setProblem(problem);
                problemSubmission.setExecutionTime(0.1);
                problemSubmission.setMemoryUsage(0.5);
                problemSubmission.setUser(user);
                problemSubmission.setLanguage(language);
                problemSubmission.setCreatedAt(Timestamp.from(Instant.now()));
                problemSubmission.setAccepted(true);
                problemSubmission.setStatus(SubmissionStatus.SUCCESS);
                problemSubmission.setNoTestCasePassed(5);

                SubmissionResponseDto submissionDetail = problemSubmissionService.getSubmissionDetail(problemSubmission,
                                5,
                                user);

                assertNotNull(submissionDetail);
                assertEquals("test code", submissionDetail.getCode());
                assertEquals(SubmissionStatus.SUCCESS, ((AcceptedSubmissionResponseDto) submissionDetail).getStatus());
                assertEquals("0.1", ((AcceptedSubmissionResponseDto) submissionDetail).getExecutionTime());
                assertEquals(0.5, ((AcceptedSubmissionResponseDto) submissionDetail).getMemoryUsage());
                assertEquals("Java", ((AcceptedSubmissionResponseDto) submissionDetail).getLanguageName());
                assertEquals(5, ((AcceptedSubmissionResponseDto) submissionDetail).getNoTestcase());
                assertEquals(problemSubmission.getCreatedAt().getTime(),
                                ((AcceptedSubmissionResponseDto) submissionDetail).getCreatedAt());
        }

        @Test
        public void testGetSubmissionDetailFailed() {
                Users user = new Users();
                user.setId(1L);

                Language language = new Language();
                language.setName("Java");

                Problem problem = new Problem();
                problem.setId(1L);
                problem.setTitle("test title");
                problem.setLink("test link");

                ProblemSubmission problemSubmission = new ProblemSubmission();
                problemSubmission.setId(1L);
                problemSubmission.setCode("test code");
                problemSubmission.setProblem(problem);
                problemSubmission.setExecutionTime(0.1);
                problemSubmission.setMemoryUsage(0.5);
                problemSubmission.setUser(user);
                problemSubmission.setLanguage(language);
                problemSubmission.setCreatedAt(Timestamp.from(Instant.now()));
                problemSubmission.setAccepted(false);
                problemSubmission.setStatus(SubmissionStatus.FAILED);
                problemSubmission.setNoTestCasePassed(3);

                SubmissionResponseDto submissionDetail = problemSubmissionService.getSubmissionDetail(problemSubmission,
                                5,
                                user);

                assertNotNull(submissionDetail);
                assertEquals("test code", submissionDetail.getCode());
                assertEquals(SubmissionStatus.FAILED, ((FailedSubmissionResponseDto) submissionDetail).getStatus());
                assertEquals(3, ((FailedSubmissionResponseDto) submissionDetail).getNoSuccessTestcase());
                assertEquals("Java", ((FailedSubmissionResponseDto) submissionDetail).getLanguageName());
                assertEquals(5, ((FailedSubmissionResponseDto) submissionDetail).getNoTestcase());
                assertEquals(problemSubmission.getCreatedAt().getTime(),
                                ((FailedSubmissionResponseDto) submissionDetail).getCreatedAt());

        }

        @Test
        public void testGetSubmissionDetailCompileError() {
                Users user = new Users();
                user.setId(1L);

                Language language = new Language();
                language.setName("Java");

                Problem problem = new Problem();
                problem.setId(1L);
                problem.setTitle("test title");
                problem.setLink("test link");

                ProblemSubmission problemSubmission = new ProblemSubmission();
                problemSubmission.setId(1L);
                problemSubmission.setCode("test code");
                problemSubmission.setProblem(problem);
                problemSubmission.setExecutionTime(0.1);
                problemSubmission.setMemoryUsage(0.5);
                problemSubmission.setUser(user);
                problemSubmission.setLanguage(language);
                problemSubmission.setCreatedAt(Timestamp.from(Instant.now()));
                problemSubmission.setAccepted(false);
                problemSubmission.setStatus(SubmissionStatus.FAILED);
                problemSubmission.setNoTestCasePassed(3);
                problemSubmission.setMessage("test message");

                SubmissionResponseDto submissionDetail = problemSubmissionService.getSubmissionDetail(problemSubmission,
                                5,
                                user);

                assertNotNull(submissionDetail);
                assertEquals("test code", submissionDetail.getCode());
                assertEquals(SubmissionStatus.FAILED, ((CompileErrorResposneDto) submissionDetail).getStatus());
                assertEquals(problemSubmission.getMessage(), ((CompileErrorResposneDto) submissionDetail).getMessage());
                assertEquals("Java", ((CompileErrorResposneDto) submissionDetail).getLanguageName());
                assertEquals(problemSubmission.getCreatedAt().getTime(),
                                ((CompileErrorResposneDto) submissionDetail).getCreatedAt());

        }

        @Test
        public void testGetSubmissionDetailNotOwnerOfSubmission() {
                Users user = new Users();
                user.setId(1L);

                Users currentUser = new Users();
                user.setId(2L);

                Language language = new Language();
                language.setName("Java");

                Problem problem = new Problem();
                problem.setId(1L);
                problem.setTitle("test title");
                problem.setLink("test link");

                ProblemSubmission problemSubmission = new ProblemSubmission();
                problemSubmission.setId(1L);
                problemSubmission.setCode("test code");
                problemSubmission.setProblem(problem);
                problemSubmission.setExecutionTime(0.1);
                problemSubmission.setMemoryUsage(0.5);
                problemSubmission.setUser(user);
                problemSubmission.setLanguage(language);
                problemSubmission.setCreatedAt(Timestamp.from(Instant.now()));
                problemSubmission.setAccepted(false);
                problemSubmission.setStatus(SubmissionStatus.FAILED);
                problemSubmission.setNoTestCasePassed(3);
                problemSubmission.setMessage("test message");

                ForbiddenException forbiddenException = assertThrows(ForbiddenException.class,
                                () -> problemSubmissionService.getSubmissionDetail(problemSubmission, 5,
                                                currentUser));
                assertEquals("You are not allowed to view this submission", forbiddenException.getMessage());
                assertEquals("You are not allowed to view this submission", forbiddenException.getDetails());

        }

        @Test
        public void testGetProblemSubmissionByIdSuccess() {
                ProblemSubmission problemSubmission = new ProblemSubmission();
                problemSubmission.setId(1L);

                when(problemSubmissionRepository.findById(anyLong())).thenReturn(Optional.of(problemSubmission));

                ProblemSubmission result = problemSubmissionService.getProblemSubmissionById(1L);
                assertNotNull(result);
                assertEquals(1L, result.getId());
        }

        @Test
        public void testGetProblemSubmissionByIdNotFound() {
                when(problemSubmissionRepository.findById(anyLong())).thenReturn(Optional.empty());

                NotFoundException notFoundException = assertThrows(NotFoundException.class,
                                () -> problemSubmissionService.getProblemSubmissionById(1L));
                assertEquals("Submission not found", notFoundException.getMessage());
                assertEquals("Submission not found", notFoundException.getDetails());
        }

        @Test
        public void testGetListSubmissions() {
                Users user = new Users();
                user.setId(1L);

                Language language = new Language();
                language.setName("Java");

                Problem problem = new Problem();
                problem.setId(1L);
                problem.setTitle("test title");
                problem.setLink("test link");

                Date start = Date.valueOf("2024-03-01");
                Date end = Date.valueOf("2024-03-02");

                int page = 0;
                Integer size = 1;
                String sortBy = "createdAt";
                Boolean ascending = true;
                Sort sort = Sort.by(sortBy).ascending();

                SubmissionListResponseDto dto = new SubmissionListResponseDto();
                dto.setId(1L);
                dto.setCreatedAt(Timestamp.from(Instant.now()).getTime());
                dto.setLanguageName("Java");
                dto.setProblemLink(problem.getLink());
                dto.setProblemTitle(problem.getTitle());

                ProblemSubmission problemSubmission = new ProblemSubmission();
                problemSubmission.setId(1L);
                problemSubmission.setCreatedAt(Timestamp.from(Instant.now()));
                problemSubmission.setLanguage(language);
                problemSubmission.setProblem(problem);

                Pageable pageable = PageRequest.of(page, size, sort);
                Page<ProblemSubmission> submissionPage = new PageImpl<>(List.of(problemSubmission), pageable, 1);

                when(problemSubmissionRepository
                                .findByUserAndTimeBetween(
                                                any(Users.class),
                                                any(Problem.class),
                                                any(SubmissionStatus.class),

                                                any(Timestamp.class),
                                                any(Timestamp.class),
                                                any(Pageable.class)))
                                .thenReturn(submissionPage);
                when(submissionListResponseMapper.mapFrom(any(ProblemSubmission.class))).thenReturn(dto);

                Page<SubmissionListResponseDto> result = problemSubmissionService.getListSubmission(user,
                                problem,
                                SubmissionStatus.SUCCESS,
                                start,
                                end,
                                page,
                                size,
                                sortBy,
                                ascending);
                assertNotNull(result);
                assertEquals(1, result.getSize());
        }

        @Test
        public void testGetListSubmissionsNullStart() {
                Users user = new Users();
                user.setId(1L);

                Language language = new Language();
                language.setName("Java");

                Problem problem = new Problem();
                problem.setId(1L);
                problem.setTitle("test title");
                problem.setLink("test link");

                Date end = Date.valueOf("2024-03-02");

                int page = 0;
                Integer size = 1;
                String sortBy = "createdAt";
                Boolean ascending = true;
                Sort sort = Sort.by(sortBy).ascending();

                SubmissionListResponseDto dto = new SubmissionListResponseDto();
                dto.setId(1L);
                dto.setCreatedAt(Timestamp.from(Instant.now()).getTime());
                dto.setLanguageName("Java");
                dto.setProblemLink(problem.getLink());
                dto.setProblemTitle(problem.getTitle());

                ProblemSubmission problemSubmission = new ProblemSubmission();
                problemSubmission.setId(1L);
                problemSubmission.setCreatedAt(Timestamp.from(Instant.now()));
                problemSubmission.setLanguage(language);
                problemSubmission.setProblem(problem);

                Pageable pageable = PageRequest.of(page, size, sort);
                Page<ProblemSubmission> submissionPage = new PageImpl<>(List.of(problemSubmission), pageable, 1);

                BadRequestException badRequestException = assertThrows(BadRequestException.class,
                                () -> problemSubmissionService.getListSubmission(user,
                                                problem,
                                                SubmissionStatus.SUCCESS,
                                                null,
                                                end,
                                                page,
                                                size,
                                                sortBy,
                                                ascending));
                assertEquals("Start and end date must be provided together", badRequestException.getMessage());
                assertEquals("Start and end date must be provided together", badRequestException.getDetails());
        }

        @Test
        public void testGetListSubmissionsNullEnd() {
                Users user = new Users();
                user.setId(1L);

                Language language = new Language();
                language.setName("Java");

                Problem problem = new Problem();
                problem.setId(1L);
                problem.setTitle("test title");
                problem.setLink("test link");

                Date start = Date.valueOf("2024-03-01");

                int page = 0;
                Integer size = 1;
                String sortBy = "createdAt";
                Boolean ascending = true;
                Sort sort = Sort.by(sortBy).ascending();

                SubmissionListResponseDto dto = new SubmissionListResponseDto();
                dto.setId(1L);
                dto.setCreatedAt(Timestamp.from(Instant.now()).getTime());
                dto.setLanguageName("Java");
                dto.setProblemLink(problem.getLink());
                dto.setProblemTitle(problem.getTitle());

                ProblemSubmission problemSubmission = new ProblemSubmission();
                problemSubmission.setId(1L);
                problemSubmission.setCreatedAt(Timestamp.from(Instant.now()));
                problemSubmission.setLanguage(language);
                problemSubmission.setProblem(problem);

                BadRequestException badRequestException = assertThrows(BadRequestException.class,
                                () -> problemSubmissionService.getListSubmission(user,
                                                problem,
                                                SubmissionStatus.SUCCESS,
                                                start,
                                                null,
                                                page,
                                                size,
                                                sortBy,
                                                ascending));
                assertEquals("Start and end date must be provided together", badRequestException.getMessage());
                assertEquals("Start and end date must be provided together", badRequestException.getDetails());
        }

        @Test
        public void testGetListSubmissionsNullEndAndStart() {
                Users user = new Users();
                user.setId(1L);

                Language language = new Language();
                language.setName("Java");

                Problem problem = new Problem();
                problem.setId(1L);
                problem.setTitle("test title");
                problem.setLink("test link");

                int page = 0;
                Integer size = 1;
                String sortBy = "createdAt";
                Boolean ascending = true;
                Sort sort = Sort.by(sortBy).ascending();

                SubmissionListResponseDto dto = new SubmissionListResponseDto();
                dto.setId(1L);
                dto.setCreatedAt(Timestamp.from(Instant.now()).getTime());
                dto.setLanguageName("Java");
                dto.setProblemLink(problem.getLink());
                dto.setProblemTitle(problem.getTitle());

                ProblemSubmission problemSubmission = new ProblemSubmission();
                problemSubmission.setId(1L);
                problemSubmission.setCreatedAt(Timestamp.from(Instant.now()));
                problemSubmission.setLanguage(language);
                problemSubmission.setProblem(problem);

                Pageable pageable = PageRequest.of(page, size, sort);
                Page<ProblemSubmission> submissionPage = new PageImpl<>(List.of(problemSubmission), pageable, 1);

                when(problemSubmissionRepository
                                .findByUserAndTimeBetween(
                                                any(Users.class),
                                                any(Problem.class),
                                                any(SubmissionStatus.class),

                                                any(Timestamp.class),
                                                any(Timestamp.class),
                                                any(Pageable.class)))
                                .thenReturn(submissionPage);
                when(submissionListResponseMapper.mapFrom(any(ProblemSubmission.class))).thenReturn(dto);

                Page<SubmissionListResponseDto> result = problemSubmissionService.getListSubmission(user,
                                problem,
                                SubmissionStatus.SUCCESS,
                                null,
                                null,
                                page,
                                size,
                                sortBy,
                                ascending);
                assertNotNull(result);
                assertEquals(1, result.getSize());
        }

        @Test
        public void testGetListSubmissionsStartAfterEnd() {
                Users user = new Users();
                user.setId(1L);

                Language language = new Language();
                language.setName("Java");

                Problem problem = new Problem();
                problem.setId(1L);
                problem.setTitle("test title");
                problem.setLink("test link");

                int page = 0;
                Integer size = 1;
                String sortBy = "createdAt";
                Boolean ascending = true;

                SubmissionListResponseDto dto = new SubmissionListResponseDto();

                dto.setId(1L);
                dto.setCreatedAt(Timestamp.from(Instant.now()).getTime());
                dto.setLanguageName("Java");
                dto.setProblemLink(problem.getLink());
                dto.setProblemTitle(problem.getTitle());

                ProblemSubmission problemSubmission = new ProblemSubmission();
                problemSubmission.setId(1L);
                problemSubmission.setCreatedAt(Timestamp.from(Instant.now()));
                problemSubmission.setLanguage(language);
                problemSubmission.setProblem(problem);

                Date start = Date.valueOf("2025-03-01");
                Date end = Date.valueOf("2023-03-02");

                BadRequestException badRequestException = assertThrows(BadRequestException.class,
                                () -> problemSubmissionService.getListSubmission(user,
                                                problem,
                                                SubmissionStatus.SUCCESS,
                                                start,
                                                end,
                                                page,
                                                size,
                                                sortBy,
                                                ascending));
                assertEquals("Start date cannot be after end date", badRequestException.getMessage());
                assertEquals("Start date cannot be after end date", badRequestException.getDetails());
        }

        @Test
        public void testGetAllProblemHasSubmitted() {
                Users user = new Users();
                user.setId(1L);

                Problem problem = new Problem();
                problem.setId(1L);
                problem.setTitle("test title");
                problem.setLink("test link");

                when(problemSubmissionRepository.findByUserAndProblemDistinct(any(Users.class)))
                                .thenReturn(List.of(problem));

                Map<String, String> results = problemSubmissionService.getAllProblemHasSubmitted(user);
                assertNotNull(results);
                assertEquals(1, results.size());
                assertEquals(problem.getLink(), results.get(problem.getTitle()));
        }

        @Test
        void testFindLastSubmittedByUserWhenPageIsNegativeShouldThrowBadRequestException() {
                // Given
                int page = -1;
                Integer size = 10;
                String sortBy = "createdAt";
                Boolean ascending = true;

                Users user = new Users();
                user.setId(1L);

                // When & Then
                BadRequestException exception = assertThrows(BadRequestException.class, () -> {
                        problemSubmissionService.findLastSubmittedByUser(user, null, page, size, sortBy, ascending);
                });

                assertEquals("Page must be greater than 0", exception.getMessage());
        }

        @Test
        void testFindLastSubmittedSuccessByUserShouldReturnPagedResult() {
                // Given
                int page = 0;
                Integer size = 5;
                String sortBy = "createdAt";
                Boolean ascending = true;
                Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());

                Users user = new Users();
                user.setId(1L);

                Problem mockProblem = new Problem();
                mockProblem.setTitle("Sample Problem");
                mockProblem.setLink("sample-problem");
                mockProblem.setDifficulty(Difficulty.EASY);
                mockProblem.setNoSubmission(10);

                Timestamp mockTimestamp = new Timestamp(System.currentTimeMillis());

                Object[] mockResult = new Object[] { mockProblem, 1, mockTimestamp };
                List<Object[]> mockList = new ArrayList<Object[]>();
                mockList.add(mockResult);

                Page<Object[]> mockPage = new PageImpl<>(mockList, pageable,
                                1);

                when(problemSubmissionRepository.findLastSubmittedByUserAndProblemInAndSuccessStatus(eq(user),
                                eq(pageable)))
                                .thenReturn(mockPage);

                // When
                Page<ProblemProgressResponseDto> result = problemSubmissionService.findLastSubmittedByUser(user,
                                SubmissionStatus.SUCCESS, page,
                                size,
                                sortBy, ascending);

                // Then
                assertNotNull(result);
                assertEquals(1, result.getTotalElements());
                assertEquals("Sample Problem", result.getContent().get(0).getProblemTitle());
                assertEquals("sample-problem", result.getContent().get(0).getProblemLink());
                assertEquals(Difficulty.EASY, result.getContent().get(0).getDifficulty());
                assertEquals(10, result.getContent().get(0).getNoSubmission());

                verify(problemSubmissionRepository, times(1))
                                .findLastSubmittedByUserAndProblemInAndSuccessStatus(eq(user),
                                                eq(pageable));
        }

        @Test
        void testFindLastSubmittedNullStatusByUserShouldReturnPagedResult() {
                // Given
                int page = 0;
                Integer size = 5;
                String sortBy = "createdAt";
                Boolean ascending = true;
                Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());

                Users user = new Users();
                user.setId(1L);

                Problem mockProblem = new Problem();
                mockProblem.setTitle("Sample Problem");
                mockProblem.setLink("sample-problem");
                mockProblem.setDifficulty(Difficulty.EASY);
                mockProblem.setNoSubmission(10);

                Timestamp mockTimestamp = new Timestamp(System.currentTimeMillis());

                Object[] mockResult = new Object[] { mockProblem, 1, mockTimestamp };
                List<Object[]> mockList = new ArrayList<Object[]>();
                mockList.add(mockResult);

                Page<Object[]> mockPage = new PageImpl<>(mockList, pageable,
                                1);

                when(problemSubmissionRepository.findLastSubmittedByUserAndProblemIn(eq(user),
                                eq(pageable)))
                                .thenReturn(mockPage);

                // When
                Page<ProblemProgressResponseDto> result = problemSubmissionService.findLastSubmittedByUser(user,
                                null, page,
                                size,
                                sortBy, ascending);

                // Then
                assertNotNull(result);
                assertEquals(1, result.getTotalElements());
                assertEquals("Sample Problem", result.getContent().get(0).getProblemTitle());
                assertEquals("sample-problem", result.getContent().get(0).getProblemLink());
                assertEquals(Difficulty.EASY, result.getContent().get(0).getDifficulty());
                assertEquals(10, result.getContent().get(0).getNoSubmission());

                verify(problemSubmissionRepository, times(1))
                                .findLastSubmittedByUserAndProblemIn(eq(user),
                                                eq(pageable));
        }

        @Test
        void testFindLastSubmittedFailedStatusByUserShouldReturnPagedResult() {
                // Given
                int page = 0;
                Integer size = 5;
                String sortBy = "createdAt";
                Boolean ascending = true;
                Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());

                Users user = new Users();
                user.setId(1L);

                Problem mockProblem = new Problem();
                mockProblem.setTitle("Sample Problem");
                mockProblem.setLink("sample-problem");
                mockProblem.setDifficulty(Difficulty.EASY);
                mockProblem.setNoSubmission(10);

                Timestamp mockTimestamp = new Timestamp(System.currentTimeMillis());

                Object[] mockResult = new Object[] { mockProblem, 1, mockTimestamp };
                List<Object[]> mockList = new ArrayList<Object[]>();
                mockList.add(mockResult);

                Page<Object[]> mockPage = new PageImpl<>(mockList, pageable,
                                1);

                when(problemSubmissionRepository.findLastSubmittedByUserAndProblemInAndFailedStatus(eq(user),
                                eq(pageable)))
                                .thenReturn(mockPage);

                // When
                Page<ProblemProgressResponseDto> result = problemSubmissionService.findLastSubmittedByUser(user,
                                SubmissionStatus.FAILED, page,
                                size,
                                sortBy, ascending);

                // Then
                assertNotNull(result);
                assertEquals(1, result.getTotalElements());
                assertEquals("Sample Problem", result.getContent().get(0).getProblemTitle());
                assertEquals("sample-problem", result.getContent().get(0).getProblemLink());
                assertEquals(Difficulty.EASY, result.getContent().get(0).getDifficulty());
                assertEquals(10, result.getContent().get(0).getNoSubmission());

                verify(problemSubmissionRepository, times(1))
                                .findLastSubmittedByUserAndProblemInAndFailedStatus(eq(user),
                                                eq(pageable));
        }
}