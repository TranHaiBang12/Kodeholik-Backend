package com.g44.kodeholik.service.problem.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.exception.ForbiddenException;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.exception.TestCaseNotPassedException;
import com.g44.kodeholik.model.dto.request.lambda.TestCase;
import com.g44.kodeholik.model.dto.request.problem.ProblemCompileRequestDto;
import com.g44.kodeholik.model.dto.request.problem.ProblemRequestDto;
import com.g44.kodeholik.model.dto.request.problem.add.EditorialDto;
import com.g44.kodeholik.model.dto.request.problem.add.InputParameterDto;
import com.g44.kodeholik.model.dto.request.problem.add.ProblemBasicAddDto;
import com.g44.kodeholik.model.dto.request.problem.add.ProblemEditorialDto;
import com.g44.kodeholik.model.dto.request.problem.add.ProblemInputParameterDto;
import com.g44.kodeholik.model.dto.request.problem.add.ProblemTestCaseDto;
import com.g44.kodeholik.model.dto.request.problem.add.SolutionCodeDto;
import com.g44.kodeholik.model.dto.request.problem.add.TemplateCode;
import com.g44.kodeholik.model.dto.request.problem.add.TestCaseDto;
import com.g44.kodeholik.model.dto.request.problem.search.SearchProblemRequestDto;
import com.g44.kodeholik.model.dto.response.problem.NoAchivedInformationResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemCompileResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemDescriptionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemEditorialResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemResponseDto;
import com.g44.kodeholik.model.dto.response.problem.solution.ProblemSolutionDto;
import com.g44.kodeholik.model.dto.response.problem.submission.SubmissionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.run.RunProblemResponseDto;
import com.g44.kodeholik.model.elasticsearch.ProblemElasticsearch;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSolution;
import com.g44.kodeholik.model.entity.problem.ProblemTemplate;
import com.g44.kodeholik.model.entity.problem.SolutionCode;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.problem.Difficulty;
import com.g44.kodeholik.model.enums.problem.InputType;
import com.g44.kodeholik.model.enums.problem.ProblemStatus;
import com.g44.kodeholik.repository.discussion.CommentRepository;
import com.g44.kodeholik.repository.elasticsearch.ProblemElasticsearchRepository;
import com.g44.kodeholik.repository.problem.ProblemRepository;
import com.g44.kodeholik.repository.problem.ProblemSolutionRepository;
import com.g44.kodeholik.repository.problem.SolutionCodeRepository;
import com.g44.kodeholik.repository.user.UserRepository;
import com.g44.kodeholik.service.aws.lambda.LambdaService;
import com.g44.kodeholik.service.excel.ExcelService;
import com.g44.kodeholik.service.problem.ProblemInputParameterService;
import com.g44.kodeholik.service.problem.ProblemService;
import com.g44.kodeholik.service.problem.ProblemSolutionService;
import com.g44.kodeholik.service.problem.ProblemSubmissionService;
import com.g44.kodeholik.service.problem.ProblemTemplateService;
import com.g44.kodeholik.service.problem.ProblemTestCaseService;
import com.g44.kodeholik.service.problem.SolutionCodeService;
import com.g44.kodeholik.service.setting.LanguageService;
import com.g44.kodeholik.service.setting.TagService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.mapper.request.problem.ProblemRequestMapper;
import com.g44.kodeholik.util.mapper.response.problem.ProblemBasicResponseMapper;
import com.g44.kodeholik.util.mapper.response.problem.ProblemDescriptionMapper;
import com.g44.kodeholik.util.mapper.response.problem.ProblemResponseMapper;
import com.g44.kodeholik.util.mapper.response.problem.SolutionCodeMapper;
import com.google.gson.Gson;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;

class ProblemServiceImplTest {

        @InjectMocks
        private ProblemServiceImpl problemService;

        @Mock
        private ProblemRequestMapper problemRequestMapper;

        @Mock
        private ModelMapper modelMapper;

        @Mock
        private ProblemResponseMapper problemResponseMapper;

        @Mock
        private ProblemDescriptionMapper problemDescriptionMapper;

        @Mock
        private ProblemRepository problemRepository;

        @Mock
        private CommentRepository commentRepository;

        @Mock
        private UserRepository userRepository;

        @Mock
        private ProblemElasticsearchRepository problemElasticsearchRepository;

        @Mock
        private ProblemSubmissionService problemSubmissionService;

        @Mock
        private ElasticsearchClient elasticsearchClient;

        @Mock
        private ProblemTemplateService problemTemplateService;

        @Mock
        private ProblemTestCaseService problemTestCaseService;

        @Mock
        private ProblemInputParameterService problemInputParameterService;

        @Mock
        private UserService userService;

        @Mock
        private TagService tagService;

        @Mock
        private LanguageService languageService;

        @Mock
        private ProblemSolutionService problemSolutionService;

        @Mock
        private SolutionCodeService solutionCodeService;

        @Mock
        private ExcelService excelService;

        @Mock
        private LambdaService lambdaService;

        @Mock
        private ProblemBasicResponseMapper problemBasicResponseMapper;

        @Mock
        private SolutionCodeMapper solutionCodeMapper;

        @Spy
        private ProblemSolutionRepository problemSolutionRepository;

        @Spy
        private SolutionCodeRepository solutionCodeRepository;

        private Gson gson = new Gson();

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
        }

        @Test
        void testSyncProblemsToElasticsearch() {
                List<Problem> problems = new ArrayList<>();
                Problem problem = new Problem();
                problem.setId(1L);
                problem.setTitle("Test Problem");
                problem.setDifficulty(Difficulty.EASY);
                problem.setAcceptanceRate(50);
                problem.setNoSubmission(10);
                problem.setLink("test-problem");
                problems.add(problem);

                when(problemRepository.findByStatusAndIsActive(ProblemStatus.PUBLIC, true)).thenReturn(problems);
                when(problemSubmissionService.checkIsCurrentUserSolvedProblem(any())).thenReturn(true);

                problemService.syncProblemsToElasticsearch();

                verify(problemElasticsearchRepository, times(1)).saveAll(anyList());
        }

        @Test
        void testGetAllProblems() {
                List<Problem> problems = new ArrayList<>();
                Problem problem = new Problem();
                problems.add(problem);

                when(problemRepository.findAll()).thenReturn(problems);
                when(problemResponseMapper.mapFrom(any())).thenReturn(new ProblemResponseDto());

                List<ProblemResponseDto> result = problemService.getAllProblems();

                assertEquals(1, result.size());
                verify(problemRepository, times(1)).findAll();
        }

        @Test
        void testGetProblemResponseDtoById() {
                Problem problem = new Problem();
                when(problemRepository.findByLink(anyString())).thenReturn(Optional.of(problem));
                when(problemResponseMapper.mapFrom(any())).thenReturn(new ProblemResponseDto());

                ProblemResponseDto result = problemService.getProblemResponseDtoById("test-link");

                assertNotNull(result);
                verify(problemRepository, times(1)).findByLink(anyString());
        }

        @Test
        void testCreateProblem() {
                ProblemRequestDto problemRequestDto = new ProblemRequestDto();
                Problem problem = new Problem();
                Users user = new Users();
                user.setId(1L);

                when(problemRequestMapper.mapTo(any())).thenReturn(problem);
                when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
                when(problemRepository.save(any())).thenReturn(problem);
                when(problemResponseMapper.mapFrom(any())).thenReturn(new ProblemResponseDto());

                ProblemResponseDto result = problemService.createProblem(problemRequestDto);

                assertNotNull(result);
                verify(problemRepository, times(1)).save(any());
        }

        @Test
        void testUpdateProblem() {
                ProblemRequestDto problemRequestDto = new ProblemRequestDto();
                Problem problem = new Problem();
                Users user = new Users();
                user.setId(1L);

                when(problemRepository.findByLink(anyString())).thenReturn(Optional.of(problem));
                when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
                when(problemRepository.save(any())).thenReturn(problem);
                when(problemResponseMapper.mapFrom(any())).thenReturn(new ProblemResponseDto());

                ProblemResponseDto result = problemService.updateProblem("test-link", problemRequestDto);

                assertNotNull(result);
                verify(problemRepository, times(1)).save(any());
        }

        @Test
        void testDeleteProblem() {
                Problem problem = new Problem();

                when(problemRepository.findByLink(anyString())).thenReturn(Optional.of(problem));

                problemService.deleteProblem("test-link");

                verify(problemRepository, times(1)).delete(any());
        }

        @Test
        void testGetProblemDescriptionByIdSuccess() {
                Problem problem = new Problem();
                problem.setTopics(new HashSet<>());
                problem.setSkills(new HashSet<>());
                // problem.setComments(new HashSet());

                when(commentRepository.countByProblemsContains(any(Problem.class))).thenReturn(1);
                when(problemRepository.findByLinkAndStatusAndIsActive(anyString(), any(), anyBoolean()))
                                .thenReturn(Optional.of(problem));
                when(problemDescriptionMapper.mapFrom(any())).thenReturn(new ProblemDescriptionResponseDto());
                when(problemSubmissionService.countByIsAcceptedAndProblem(anyBoolean(), any())).thenReturn(0L);

                ProblemDescriptionResponseDto result = problemService.getProblemDescriptionById("test-link");

                assertNotNull(result);
                verify(problemRepository, times(1)).findByLinkAndStatusAndIsActive(anyString(), any(), anyBoolean());
        }

        @Test
        void testGetProblemDescriptionByIdSuccessNotFound() {
                Problem problem = new Problem();
                problem.setTopics(new HashSet<>());
                problem.setSkills(new HashSet<>());
                // problem.setComments(new HashSet());

                when(problemRepository.findByLinkAndStatusAndIsActive(anyString(), any(), anyBoolean()))
                                .thenReturn(Optional.empty());
                when(problemDescriptionMapper.mapFrom(any())).thenReturn(new ProblemDescriptionResponseDto());
                when(problemSubmissionService.countByIsAcceptedAndProblem(anyBoolean(), any())).thenReturn(0L);

                NotFoundException notFoundException = assertThrows(
                                NotFoundException.class,
                                () -> problemService.getProblemDescriptionById("test-link"));
                assertEquals("Problem not found", notFoundException.getMessage());
                assertEquals("Problem not found", notFoundException.getDetails());

                verify(problemRepository, times(1)).findByLinkAndStatusAndIsActive(anyString(), any(), anyBoolean());
        }

        @Test
        void testGetProblemByIdSuccess() {
                Problem problem = new Problem();

                when(problemRepository.findById(anyLong())).thenReturn(Optional.of(problem));

                Problem result = problemService.getProblemById(1L);

                assertNotNull(result);
                verify(problemRepository, times(1)).findById(anyLong());
        }

        @Test
        void testGetProblemByIdNotFound() {
                when(problemRepository.findById(anyLong())).thenReturn(Optional.empty());

                NotFoundException notFoundException = assertThrows(
                                NotFoundException.class,
                                () -> problemService.getProblemById(1L));
                assertEquals("Problem not found", notFoundException.getMessage());
                assertEquals("Problem not found", notFoundException.getDetails());
                verify(problemRepository, times(1)).findById(anyLong());
        }

        @Test
        void testGetProblemEditorialDtoLis() {
                String link = "test-link";

                Problem problem = new Problem();
                problem.setTitle("test");
                problem.setLink(link);
                problem.setTopics(new HashSet<>());
                problem.setSkills(new HashSet<>());

                ProblemResponseDto problemResponseDto = new ProblemResponseDto();
                problemResponseDto.setTitle("test");

                when(problemRepository.findByLinkAndStatusAndIsActive(anyString(), any(), anyBoolean()))
                                .thenReturn(Optional.of(problem));
                when(problemSolutionService.findEditorialByProblem(any(Problem.class)))
                                .thenReturn(List.of(new ProblemSolution()));
                when(problemResponseMapper.mapFrom(any(Problem.class))).thenReturn(problemResponseDto);
                when(solutionCodeService.findBySolution(any(ProblemSolution.class)))
                                .thenReturn(List.of(new SolutionCodeDto()));
                ProblemEditorialResponseDto result = problemService.getProblemEditorialDtoList(link);

                assertNotNull(result);
                verify(problemSolutionService, times(1)).findEditorialByProblem(any(Problem.class));
                verify(solutionCodeService, times(1)).findBySolution(any(ProblemSolution.class));
        }

        @Test
        void testGetProblemByLinkSuccess() {
                Problem problem = new Problem();

                when(problemRepository.findByLink(anyString())).thenReturn(Optional.of(problem));

                Problem result = problemService.getProblemByLink("test-link");

                assertNotNull(result);
                verify(problemRepository, times(1)).findByLink(anyString());
        }

        @Test
        void testGetProblemByLinkNotFound() {
                Problem problem = new Problem();

                when(problemRepository.findByLink(anyString())).thenReturn(Optional.empty());

                NotFoundException notFoundException = assertThrows(
                                NotFoundException.class,
                                () -> problemService.getProblemByLink("test-link"));
                assertEquals("Problem not found", notFoundException.getMessage());
                assertEquals("Problem not found", notFoundException.getDetails());
                verify(problemRepository, times(1)).findByLink(anyString());
        }

        @Test
        void testGetActivePublicProblemByLinkSuccess() {
                Problem problem = new Problem();

                when(problemRepository.findByLinkAndStatusAndIsActive(anyString(), any(), anyBoolean()))
                                .thenReturn(Optional.of(problem));

                Problem result = problemService.getActivePublicProblemByLink("test-link");
                assertNotNull(result);
                verify(problemRepository, times(1)).findByLinkAndStatusAndIsActive(anyString(), any(), anyBoolean());
        }

        @Test
        void testGetActivePublicProblemByLinkNotFound() {
                Problem problem = new Problem();

                when(problemRepository.findByLinkAndStatusAndIsActive(anyString(), any(), anyBoolean()))
                                .thenReturn(Optional.empty());

                NotFoundException notFoundException = assertThrows(
                                NotFoundException.class,
                                () -> problemService.getActivePublicProblemByLink("test-link"));
                assertEquals("Problem not found", notFoundException.getMessage());
                assertEquals("Problem not found", notFoundException.getDetails());
                verify(problemRepository, times(1)).findByLinkAndStatusAndIsActive(anyString(), any(), anyBoolean());
        }

        @Test
        void testSearchProblemsSuccess() throws IOException {
                // Mock dữ liệu từ Elasticsearch
                SearchProblemRequestDto searchDto = new SearchProblemRequestDto();
                searchDto.setTitle("");
                searchDto.setDifficulty(List.of("EASY"));
                searchDto.setTopics(List.of("Array"));
                searchDto.setSkills(List.of("MATH"));

                ProblemElasticsearch mockProblem = new ProblemElasticsearch();
                mockProblem.setId(1L);
                mockProblem.setTitle("Algorithm Problem");
                mockProblem.setDifficulty("EASY");
                mockProblem.setTopics(List.of("Array"));
                mockProblem.setSkills(List.of("MATH"));

                List<Hit<ProblemElasticsearch>> hits = List.of(Hit.of(h -> h
                                .index("problems")
                                .id("1")
                                .score(1.0)
                                .source(mockProblem)));

                TotalHits totalHits = new TotalHits.Builder()
                                .value(1L)
                                .relation(TotalHitsRelation.Eq)
                                .build();

                // Mock SearchResponse
                SearchResponse<ProblemElasticsearch> mockResponse = mock(SearchResponse.class);
                when(mockResponse.hits()).thenReturn(new HitsMetadata.Builder<ProblemElasticsearch>()
                                .hits(hits)
                                .total(totalHits)
                                .build());

                when(elasticsearchClient.search(any(SearchRequest.class), eq(ProblemElasticsearch.class)))
                                .thenReturn(mockResponse);

                // Mock dữ liệu từ Database
                Problem mockDbProblem = new Problem();
                mockDbProblem.setId(1L);
                mockDbProblem.setTitle("Algorithm Problem");

                // Gọi hàm service
                Page<ProblemElasticsearch> result = problemService.searchProblems(searchDto, 0, 5, null, true);

                // Kiểm tra kết quả
                assertNotNull(result);
                assertEquals(1, result.getTotalElements());
                assertEquals("Algorithm Problem", result.getContent().get(0).getTitle());

                // Kiểm tra mock được gọi đúng số lần
                verify(elasticsearchClient, times(1)).search(any(SearchRequest.class), eq(ProblemElasticsearch.class));
        }

        @Test
        void testSearchProblemsEmptyResult() throws IOException {
                SearchProblemRequestDto searchDto = new SearchProblemRequestDto();
                searchDto.setTitle("");
                searchDto.setDifficulty(List.of("EASY"));
                searchDto.setTopics(List.of("Array"));
                searchDto.setSkills(List.of("MATH"));

                ProblemElasticsearch mockProblem = new ProblemElasticsearch();
                mockProblem.setId(1L);
                mockProblem.setTitle("Algorithm Problem");
                mockProblem.setDifficulty("EASY");

                List<Hit<ProblemElasticsearch>> hits = List.of(Hit.of(h -> h
                                .index("problems")
                                .id("1")
                                .score(1.0)
                                .source(mockProblem)));

                TotalHits totalHits = new TotalHits.Builder()
                                .value(1L)
                                .relation(TotalHitsRelation.Eq)
                                .build();

                // Mock SearchResponse
                SearchResponse<ProblemElasticsearch> mockResponse = mock(SearchResponse.class);
                when(mockResponse.hits()).thenReturn(new HitsMetadata.Builder<ProblemElasticsearch>()
                                .hits(hits)
                                .total(totalHits)
                                .build());

                when(elasticsearchClient.search(any(SearchRequest.class), eq(ProblemElasticsearch.class)))
                                .thenReturn(mock(SearchResponse.class));

                // Mock dữ liệu từ Database
                Problem mockDbProblem = new Problem();
                mockDbProblem.setId(1L);
                mockDbProblem.setTitle("Algorithm Problem");

                // Gọi hàm service
                Page<ProblemElasticsearch> result = problemService.searchProblems(searchDto, 0, 5, null, true);

                // Kiểm tra kết quả
                assertNotNull(result);
                assertEquals(0, result.getTotalElements());

                // Kiểm tra mock được gọi đúng số lần
                verify(elasticsearchClient, times(1)).search(any(SearchRequest.class), eq(ProblemElasticsearch.class));
        }

        @Test
        void testGetAutocompleteSuggestionsForProblemTitle() throws Exception {
                // Mock ElasticsearchClient
                ElasticsearchClient mockClient = mock(ElasticsearchClient.class);

                // Mock dữ liệu giả lập
                ProblemElasticsearch problem1 = new ProblemElasticsearch();
                problem1.setId(1L);
                problem1.setTitle("Java Programming");

                ProblemElasticsearch problem2 = new ProblemElasticsearch();
                problem2.setId(2L);
                problem2.setTitle("JavaScript Basics");

                // Tạo danh sách `Hit` chứa kết quả tìm kiếm giả lập
                List<Hit<ProblemElasticsearch>> hits = List.of(
                                Hit.of(h -> h.index("problems").id("1").score(1.0).source(problem1)),
                                Hit.of(h -> h.index("problems").id("2").score(0.9).source(problem2)));

                // Mock SearchResponse
                TotalHits totalHits = new TotalHits.Builder().value(2L).relation(TotalHitsRelation.Eq).build();
                SearchResponse<ProblemElasticsearch> mockResponse = mock(SearchResponse.class);
                when(mockResponse.hits()).thenReturn(new HitsMetadata.Builder<ProblemElasticsearch>()
                                .hits(hits)
                                .total(totalHits)
                                .build());
                when(elasticsearchClient.search(any(SearchRequest.class), eq(ProblemElasticsearch.class)))
                                .thenReturn(mockResponse);

                // Gọi hàm thực tế
                List<String> suggestions = problemService.getAutocompleteSuggestionsForProblemTitle("Java");

                // Kiểm tra kết quả
                assertNotNull(suggestions);
                assertEquals(2, suggestions.size());
                assertEquals("Java Programming", suggestions.get(0));
                assertEquals("JavaScript Basics", suggestions.get(1));
        }

        @Test
        void testNotGetAutocompleteSuggestionsForProblemTitle() throws Exception {
                // Mock ElasticsearchClient
                ElasticsearchClient mockClient = mock(ElasticsearchClient.class);

                // Mock dữ liệu giả lập
                ProblemElasticsearch problem1 = new ProblemElasticsearch();
                problem1.setId(1L);
                problem1.setTitle("Java Programming");

                ProblemElasticsearch problem2 = new ProblemElasticsearch();
                problem2.setId(2L);
                problem2.setTitle("JavaScript Basics");

                // Tạo danh sách `Hit` chứa kết quả tìm kiếm giả lập
                List<Hit<ProblemElasticsearch>> hits = List.of(
                                Hit.of(h -> h.index("problems").id("1").score(1.0).source(problem1)),
                                Hit.of(h -> h.index("problems").id("2").score(0.9).source(problem2)));

                // Mock SearchResponse
                TotalHits totalHits = new TotalHits.Builder().value(2L).relation(TotalHitsRelation.Eq).build();
                SearchResponse<ProblemElasticsearch> mockResponse = mock(SearchResponse.class);
                when(mockResponse.hits()).thenReturn(new HitsMetadata.Builder<ProblemElasticsearch>()
                                .hits(hits)
                                .total(totalHits)
                                .build());
                when(elasticsearchClient.search(any(SearchRequest.class), eq(ProblemElasticsearch.class)))
                                .thenReturn(mock(SearchResponse.class));

                // Gọi hàm thực tế
                List<String> suggestions = problemService.getAutocompleteSuggestionsForProblemTitle("Java");

                // Kiểm tra kết quả
                assertNotNull(suggestions);
                assertEquals(0, suggestions.size());
        }

        @Test
        void testRun() {
                Problem problem = new Problem();
                ProblemCompileRequestDto problemCompileRequestDto = new ProblemCompileRequestDto();

                when(problemRepository.findByLinkAndStatusAndIsActive(anyString(), any(), anyBoolean()))
                                .thenReturn(Optional.of(problem));
                when(problemSubmissionService.run(any(), any(), any(), any())).thenReturn(new RunProblemResponseDto());

                RunProblemResponseDto result = problemService.run("test-link", problemCompileRequestDto);

                assertNotNull(result);
        }

        @Test
        void testFindByProblemAndLanguage() {
                Problem problem = new Problem();

                when(problemRepository.findByLinkAndStatusAndIsActive(anyString(), any(), anyBoolean()))
                                .thenReturn(Optional.of(problem));
                when(problemTemplateService.findByProblemAndLanguage(any(), anyString()))
                                .thenReturn(new ProblemTemplate());

                ProblemTemplate result = problemService.findByProblemAndLanguage("test-link", "Java");

                assertNotNull(result);
        }

        @Test
        void testGetTestCaseByProblem() {
                Problem problem = new Problem();

                Language language = new Language();
                List<Language> languages = List.of(language);

                when(problemRepository.findByLinkAndStatusAndIsActive(anyString(), any(), anyBoolean()))
                                .thenReturn(Optional.of(problem));
                when(problemTestCaseService.getTestCaseByProblem(any(), any())).thenReturn(new ArrayList<>());

                List<List<TestCase>> result = problemService.getTestCaseByProblem("test-link", languages);

                assertNotNull(result);
        }

        @Test
        void testGetSampleTestCaseByProblem() {
                Problem problem = new Problem();

                Language language = new Language();
                List<Language> languages = List.of(language);

                when(problemRepository.findByLinkAndStatusAndIsActive(anyString(), any(), anyBoolean()))
                                .thenReturn(Optional.of(problem));
                when(problemTestCaseService.getSampleTestCaseByProblem(any(), any())).thenReturn(new ArrayList<>());

                List<List<TestCase>> result = problemService.getSampleTestCaseByProblem("test-link", languages);

                assertNotNull(result);
        }

        @Test
        void testGetProblemCompileInformationById() {
                Problem problem = new Problem();

                Language language = new Language();

                when(problemRepository.findByLinkAndStatusAndIsActive(anyString(), any(),
                                anyBoolean()))
                                .thenReturn(Optional.of(problem));
                when(problemTestCaseService.getProblemCompileInformationByProblem(any(),
                                any()))
                                .thenReturn(new ProblemCompileResponseDto());

                ProblemCompileResponseDto result = problemService.getProblemCompileInformationById("test-link", "Java");

                assertNotNull(result);
        }

        @Test
        void testGetListNoAchievedInformationByCurrentUser() {
                Users user = new Users();
                List<Problem> problems = new ArrayList<>();

                when(userService.getCurrentUser()).thenReturn(user);
                when(problemRepository.findAll()).thenReturn(problems);
                when(problemRepository.findByDifficulty(any())).thenReturn(problems);
                when(problemSubmissionService.countByUserAndIsAcceptedAndProblemIn(any(), anyBoolean(), anyList()))
                                .thenReturn(0L);

                List<NoAchivedInformationResponseDto> result = problemService
                                .getListNoAchievedInformationByCurrentUser();

                assertNotNull(result);
        }

        @Test
        void testAddProblemSuccess() {
                Language language = new Language();
                language.setId(1L);
                language.setName("Java");

                ProblemBasicAddDto problemBasicAddDto = new ProblemBasicAddDto();
                problemBasicAddDto.setTitle("test title");
                problemBasicAddDto.setDescription("test description");
                problemBasicAddDto.setDifficulty(Difficulty.EASY);
                problemBasicAddDto.setIsActive(true);
                problemBasicAddDto.setLanguageSupport(List.of("Java"));
                problemBasicAddDto.setSkills(new ArrayList());
                problemBasicAddDto.setStatus(ProblemStatus.PUBLIC);
                problemBasicAddDto.setTopics(new ArrayList());

                ProblemEditorialDto problemEditorialDto = new ProblemEditorialDto();
                EditorialDto editorialDto = new EditorialDto();
                editorialDto.setEditorialTitle("test editorial title");
                editorialDto.setEditorialTextSolution("test editorial text solution");
                editorialDto.setEditorialSkills(new ArrayList<>());
                SolutionCodeDto solutionCodeDto = new SolutionCodeDto();
                solutionCodeDto.setSolutionCode("test code");
                solutionCodeDto.setSolutionLanguage("Java");
                editorialDto.setSolutionCodes(List.of(solutionCodeDto));
                problemEditorialDto.setEditorialDtos(editorialDto);

                ProblemInputParameterDto problemInputParameterDto = new ProblemInputParameterDto();
                problemInputParameterDto.setFunctionSignature("test function signature");
                problemInputParameterDto.setLanguage("Java");
                problemInputParameterDto.setReturnType(InputType.INT);
                InputParameterDto inputParameterDto = new InputParameterDto();
                inputParameterDto.setInputName("test");
                inputParameterDto.setInputType(InputType.INT);
                problemInputParameterDto.setParameters(List.of(inputParameterDto));
                TemplateCode templateCode = new TemplateCode();
                templateCode.setCode("test code");
                templateCode.setLanguage("Java");
                problemInputParameterDto.setTemplateCode(templateCode);
                List<ProblemInputParameterDto> problemInputParameterDtoList = List.of(problemInputParameterDto);

                TestCaseDto testCase = new TestCaseDto();
                testCase.setInput(new HashMap());
                testCase.setExpectedOutput("test output");
                testCase.setIsSample(true);
                List<TestCaseDto> testCaseList = List.of(testCase);
                ProblemTestCaseDto problemTestCaseDto = new ProblemTestCaseDto();
                problemTestCaseDto.setLanguage("Java");
                problemTestCaseDto.setTestCases(testCaseList);
                List<ProblemTestCaseDto> problemTestCaseDtoList = List.of(problemTestCaseDto);

                MultipartFile excelFile = mock(MultipartFile.class);

                when(problemRepository.findByLink(any())).thenReturn(Optional.empty());
                when(languageService.findByName(any())).thenReturn(language);
                when(languageService.getLanguagesByNameList(any())).thenReturn(Set.of(language));
                when(problemRepository.isTitleExisted(anyString())).thenReturn(false);
                when(excelService.readTestCaseExcel(any(), anyList(), anyString()))
                                .thenReturn(problemTestCaseDtoList);
                when(problemRequestMapper.mapTo(any())).thenReturn(new Problem());
                when(userService.getCurrentUser()).thenReturn(new Users());
                when(problemRepository.save(any())).thenReturn(new Problem());
                String lambdaResult = "{\"isAccepted\":true,\"time\":\"1.0\",\"memoryUsage\":\"128\",\"noSuccessTestcase\":2,\"results\":[]}";
                when(lambdaService.invokeLambdaFunction(any())).thenReturn(lambdaResult);

                problemService.addProblem(problemBasicAddDto, problemEditorialDto,
                                problemInputParameterDtoList, excelFile);

                verify(problemRepository, times(1)).save(any());
        }

        @Test
        void testAddProblemTitleExisted() {
                Language language = new Language();
                language.setId(1L);
                language.setName("Java");

                ProblemBasicAddDto problemBasicAddDto = new ProblemBasicAddDto();
                problemBasicAddDto.setTitle("test title");
                problemBasicAddDto.setDescription("test description");
                problemBasicAddDto.setDifficulty(Difficulty.EASY);
                problemBasicAddDto.setIsActive(true);
                problemBasicAddDto.setLanguageSupport(List.of("Java"));
                problemBasicAddDto.setSkills(new ArrayList());
                problemBasicAddDto.setStatus(ProblemStatus.PUBLIC);
                problemBasicAddDto.setTopics(new ArrayList());

                ProblemEditorialDto problemEditorialDto = new ProblemEditorialDto();
                EditorialDto editorialDto = new EditorialDto();
                editorialDto.setEditorialTitle("test editorial title");
                editorialDto.setEditorialTextSolution("test editorial text solution");
                editorialDto.setEditorialSkills(new ArrayList<>());
                SolutionCodeDto solutionCodeDto = new SolutionCodeDto();
                solutionCodeDto.setSolutionCode("test code");
                solutionCodeDto.setSolutionLanguage("Java");
                editorialDto.setSolutionCodes(List.of(solutionCodeDto));
                problemEditorialDto.setEditorialDtos(editorialDto);

                ProblemInputParameterDto problemInputParameterDto = new ProblemInputParameterDto();
                problemInputParameterDto.setFunctionSignature("test function signature");
                problemInputParameterDto.setLanguage("Java");
                problemInputParameterDto.setReturnType(InputType.INT);
                InputParameterDto inputParameterDto = new InputParameterDto();
                inputParameterDto.setInputName("test");
                inputParameterDto.setInputType(InputType.INT);
                problemInputParameterDto.setParameters(List.of(inputParameterDto));
                TemplateCode templateCode = new TemplateCode();
                templateCode.setCode("test code");
                templateCode.setLanguage("Java");
                problemInputParameterDto.setTemplateCode(templateCode);
                List<ProblemInputParameterDto> problemInputParameterDtoList = List.of(problemInputParameterDto);

                TestCaseDto testCase = new TestCaseDto();
                testCase.setInput(new HashMap());
                testCase.setExpectedOutput("test output");
                testCase.setIsSample(true);
                List<TestCaseDto> testCaseList = List.of(testCase);
                ProblemTestCaseDto problemTestCaseDto = new ProblemTestCaseDto();
                problemTestCaseDto.setLanguage("Java");
                problemTestCaseDto.setTestCases(testCaseList);
                List<ProblemTestCaseDto> problemTestCaseDtoList = List.of(problemTestCaseDto);

                MultipartFile excelFile = mock(MultipartFile.class);

                when(problemRepository.findByLink(any())).thenReturn(Optional.empty());
                when(languageService.findByName(any())).thenReturn(language);
                when(languageService.getLanguagesByNameList(any())).thenReturn(Set.of(language));
                when(problemRepository.isTitleExisted(anyString())).thenReturn(true);
                when(excelService.readTestCaseExcel(any(), anyList(), anyString()))
                                .thenReturn(problemTestCaseDtoList);
                when(problemRequestMapper.mapTo(any())).thenReturn(new Problem());
                when(userService.getCurrentUser()).thenReturn(new Users());
                when(problemRepository.save(any())).thenReturn(new Problem());
                String lambdaResult = "{\"isAccepted\":true,\"time\":\"1.0\",\"memoryUsage\":\"128\",\"noSuccessTestcase\":2,\"results\":[]}";
                when(lambdaService.invokeLambdaFunction(any())).thenReturn(lambdaResult);

                BadRequestException badRequestException = assertThrows(
                                BadRequestException.class,
                                () -> problemService.addProblem(problemBasicAddDto, problemEditorialDto,
                                                problemInputParameterDtoList, excelFile));
                assertEquals("Title has already existed", badRequestException.getMessage());
                assertEquals("Title has already existed", badRequestException.getDetails());
        }

        @Test
        void testAddProblemDuplicatedLink() {
                Language language = new Language();
                language.setId(1L);
                language.setName("Java");

                ProblemBasicAddDto problemBasicAddDto = new ProblemBasicAddDto();
                problemBasicAddDto.setTitle("test title");
                problemBasicAddDto.setDescription("test description");
                problemBasicAddDto.setDifficulty(Difficulty.EASY);
                problemBasicAddDto.setIsActive(true);
                problemBasicAddDto.setLanguageSupport(List.of("Java"));
                problemBasicAddDto.setSkills(new ArrayList());
                problemBasicAddDto.setStatus(ProblemStatus.PUBLIC);
                problemBasicAddDto.setTopics(new ArrayList());

                ProblemEditorialDto problemEditorialDto = new ProblemEditorialDto();
                EditorialDto editorialDto = new EditorialDto();
                editorialDto.setEditorialTitle("test editorial title");
                editorialDto.setEditorialTextSolution("test editorial text solution");
                editorialDto.setEditorialSkills(new ArrayList<>());
                SolutionCodeDto solutionCodeDto = new SolutionCodeDto();
                solutionCodeDto.setSolutionCode("test code");
                solutionCodeDto.setSolutionLanguage("Java");
                editorialDto.setSolutionCodes(List.of(solutionCodeDto));
                problemEditorialDto.setEditorialDtos(editorialDto);

                ProblemInputParameterDto problemInputParameterDto = new ProblemInputParameterDto();
                problemInputParameterDto.setFunctionSignature("test function signature");
                problemInputParameterDto.setLanguage("Java");
                problemInputParameterDto.setReturnType(InputType.INT);
                InputParameterDto inputParameterDto = new InputParameterDto();
                inputParameterDto.setInputName("test");
                inputParameterDto.setInputType(InputType.INT);
                problemInputParameterDto.setParameters(List.of(inputParameterDto));
                TemplateCode templateCode = new TemplateCode();
                templateCode.setCode("test code");
                templateCode.setLanguage("Java");
                problemInputParameterDto.setTemplateCode(templateCode);
                List<ProblemInputParameterDto> problemInputParameterDtoList = List.of(problemInputParameterDto);

                TestCaseDto testCase = new TestCaseDto();
                testCase.setInput(new HashMap());
                testCase.setExpectedOutput("test output");
                testCase.setIsSample(true);
                List<TestCaseDto> testCaseList = List.of(testCase);
                ProblemTestCaseDto problemTestCaseDto = new ProblemTestCaseDto();
                problemTestCaseDto.setLanguage("Java");
                problemTestCaseDto.setTestCases(testCaseList);
                List<ProblemTestCaseDto> problemTestCaseDtoList = List.of(problemTestCaseDto);

                MultipartFile excelFile = mock(MultipartFile.class);

                when(problemRepository.findByLink(any())).thenReturn(Optional.of(new Problem()));
                when(languageService.findByName(any())).thenReturn(language);
                when(languageService.getLanguagesByNameList(any())).thenReturn(Set.of(language));
                when(problemRepository.isTitleExisted(anyString())).thenReturn(false);
                when(excelService.readTestCaseExcel(any(), anyList(), anyString()))
                                .thenReturn(problemTestCaseDtoList);
                when(problemRequestMapper.mapTo(any())).thenReturn(new Problem());
                when(userService.getCurrentUser()).thenReturn(new Users());
                when(problemRepository.save(any())).thenReturn(new Problem());
                String lambdaResult = "{\"isAccepted\":true,\"time\":\"1.0\",\"memoryUsage\":\"128\",\"noSuccessTestcase\":2,\"results\":[]}";
                when(lambdaService.invokeLambdaFunction(any())).thenReturn(lambdaResult);

                BadRequestException badRequestException = assertThrows(
                                BadRequestException.class,
                                () -> problemService.addProblem(problemBasicAddDto, problemEditorialDto,
                                                problemInputParameterDtoList, excelFile));
                assertEquals("Title is not valid because the link is duplicated", badRequestException.getMessage());
                assertEquals("Title is not valid because the link is duplicated", badRequestException.getDetails());
        }

        @Test
        void testAddProblemEmptyTestCase() {
                Language language = new Language();
                language.setId(1L);
                language.setName("Java");

                ProblemBasicAddDto problemBasicAddDto = new ProblemBasicAddDto();
                problemBasicAddDto.setTitle("test title");
                problemBasicAddDto.setDescription("test description");
                problemBasicAddDto.setDifficulty(Difficulty.EASY);
                problemBasicAddDto.setIsActive(true);
                problemBasicAddDto.setLanguageSupport(List.of("Java"));
                problemBasicAddDto.setSkills(new ArrayList());
                problemBasicAddDto.setStatus(ProblemStatus.PUBLIC);
                problemBasicAddDto.setTopics(new ArrayList());

                ProblemEditorialDto problemEditorialDto = new ProblemEditorialDto();
                EditorialDto editorialDto = new EditorialDto();
                editorialDto.setEditorialTitle("test editorial title");
                editorialDto.setEditorialTextSolution("test editorial text solution");
                editorialDto.setEditorialSkills(new ArrayList<>());
                SolutionCodeDto solutionCodeDto = new SolutionCodeDto();
                solutionCodeDto.setSolutionCode("test code");
                solutionCodeDto.setSolutionLanguage("Java");
                editorialDto.setSolutionCodes(List.of(solutionCodeDto));
                problemEditorialDto.setEditorialDtos(editorialDto);

                ProblemInputParameterDto problemInputParameterDto = new ProblemInputParameterDto();
                problemInputParameterDto.setFunctionSignature("test function signature");
                problemInputParameterDto.setLanguage("Java");
                problemInputParameterDto.setReturnType(InputType.INT);
                InputParameterDto inputParameterDto = new InputParameterDto();
                inputParameterDto.setInputName("test");
                inputParameterDto.setInputType(InputType.INT);
                problemInputParameterDto.setParameters(List.of(inputParameterDto));
                TemplateCode templateCode = new TemplateCode();
                templateCode.setCode("test code");
                templateCode.setLanguage("Java");
                problemInputParameterDto.setTemplateCode(templateCode);
                List<ProblemInputParameterDto> problemInputParameterDtoList = List.of(problemInputParameterDto);

                TestCaseDto testCase = new TestCaseDto();
                testCase.setInput(new HashMap());
                testCase.setExpectedOutput("test output");
                testCase.setIsSample(true);
                List<TestCaseDto> testCaseList = List.of(testCase);
                ProblemTestCaseDto problemTestCaseDto = new ProblemTestCaseDto();
                problemTestCaseDto.setLanguage("Java");
                problemTestCaseDto.setTestCases(testCaseList);
                List<ProblemTestCaseDto> problemTestCaseDtoList = List.of(problemTestCaseDto);

                MultipartFile excelFile = mock(MultipartFile.class);

                when(problemRepository.findByLink(any())).thenReturn(Optional.empty());
                when(languageService.findByName(any())).thenReturn(language);
                when(languageService.getLanguagesByNameList(any())).thenReturn(Set.of(language));
                when(problemRepository.isTitleExisted(anyString())).thenReturn(false);
                when(excelService.readTestCaseExcel(any(), anyList(), anyString()))
                                .thenReturn(new ArrayList());
                when(problemRequestMapper.mapTo(any())).thenReturn(new Problem());
                when(userService.getCurrentUser()).thenReturn(new Users());
                when(problemRepository.save(any())).thenReturn(new Problem());
                String lambdaResult = "{\"isAccepted\":true,\"time\":\"1.0\",\"memoryUsage\":\"128\",\"noSuccessTestcase\":2,\"results\":[]}";
                when(lambdaService.invokeLambdaFunction(any())).thenReturn(lambdaResult);

                BadRequestException badRequestException = assertThrows(
                                BadRequestException.class,
                                () -> problemService.addProblem(problemBasicAddDto, problemEditorialDto,
                                                problemInputParameterDtoList, excelFile));
                assertEquals("Excel file doesn't contain any test case", badRequestException.getMessage());
                assertEquals("Excel file doesn't contain any test case", badRequestException.getDetails());
        }

        @Test
        void testAddProblemTestCaseNotPassed() {
                Language language = new Language();
                language.setId(1L);
                language.setName("Java");

                ProblemBasicAddDto problemBasicAddDto = new ProblemBasicAddDto();
                problemBasicAddDto.setTitle("test title");
                problemBasicAddDto.setDescription("test description");
                problemBasicAddDto.setDifficulty(Difficulty.EASY);
                problemBasicAddDto.setIsActive(true);
                problemBasicAddDto.setLanguageSupport(List.of("Java"));
                problemBasicAddDto.setSkills(new ArrayList());
                problemBasicAddDto.setStatus(ProblemStatus.PUBLIC);
                problemBasicAddDto.setTopics(new ArrayList());

                ProblemEditorialDto problemEditorialDto = new ProblemEditorialDto();
                EditorialDto editorialDto = new EditorialDto();
                editorialDto.setEditorialTitle("test editorial title");
                editorialDto.setEditorialTextSolution("test editorial text solution");
                editorialDto.setEditorialSkills(new ArrayList<>());
                SolutionCodeDto solutionCodeDto = new SolutionCodeDto();
                solutionCodeDto.setSolutionCode("test code");
                solutionCodeDto.setSolutionLanguage("Java");
                editorialDto.setSolutionCodes(List.of(solutionCodeDto));
                problemEditorialDto.setEditorialDtos(editorialDto);

                ProblemInputParameterDto problemInputParameterDto = new ProblemInputParameterDto();
                problemInputParameterDto.setFunctionSignature("test function signature");
                problemInputParameterDto.setLanguage("Java");
                problemInputParameterDto.setReturnType(InputType.INT);
                InputParameterDto inputParameterDto = new InputParameterDto();
                inputParameterDto.setInputName("test");
                inputParameterDto.setInputType(InputType.INT);
                problemInputParameterDto.setParameters(List.of(inputParameterDto));
                TemplateCode templateCode = new TemplateCode();
                templateCode.setCode("test code");
                templateCode.setLanguage("Java");
                problemInputParameterDto.setTemplateCode(templateCode);
                List<ProblemInputParameterDto> problemInputParameterDtoList = List.of(problemInputParameterDto);

                TestCaseDto testCase = new TestCaseDto();
                testCase.setInput(new HashMap());
                testCase.setExpectedOutput("test output");
                testCase.setIsSample(true);
                List<TestCaseDto> testCaseList = List.of(testCase);
                ProblemTestCaseDto problemTestCaseDto = new ProblemTestCaseDto();
                problemTestCaseDto.setLanguage("Java");
                problemTestCaseDto.setTestCases(testCaseList);
                List<ProblemTestCaseDto> problemTestCaseDtoList = List.of(problemTestCaseDto);

                MultipartFile excelFile = mock(MultipartFile.class);

                when(problemRepository.findByLink(any())).thenReturn(Optional.empty());
                when(languageService.findByName(any())).thenReturn(language);
                when(languageService.getLanguagesByNameList(any())).thenReturn(Set.of(language));
                when(problemRepository.isTitleExisted(anyString())).thenReturn(false);
                when(excelService.readTestCaseExcel(any(), anyList(), anyString()))
                                .thenReturn(problemTestCaseDtoList);
                when(problemRequestMapper.mapTo(any())).thenReturn(new Problem());
                when(userService.getCurrentUser()).thenReturn(new Users());
                when(problemRepository.save(any())).thenReturn(new Problem());
                String lambdaResult = "{\"isAccepted\":false,\"time\":\"1.0\",\"memoryUsage\":\"128\",\"noSuccessTestcase\":2,\"results\":[]}";
                when(lambdaService.invokeLambdaFunction(any())).thenReturn(lambdaResult);

                assertThrows(
                                TestCaseNotPassedException.class,
                                () -> problemService.addProblem(problemBasicAddDto, problemEditorialDto,
                                                problemInputParameterDtoList, excelFile));
        }

        @Test
        void testEditProblemSuccess() {
                String link = "test";

                Problem problem = new Problem();
                problem.setId(1L);
                problem.setTitle("test title");
                problem.setLink(link);

                Language language = new Language();
                language.setId(1L);
                language.setName("Java");

                ProblemBasicAddDto problemBasicAddDto = new ProblemBasicAddDto();
                problemBasicAddDto.setTitle("test title");
                problemBasicAddDto.setDescription("test description");
                problemBasicAddDto.setDifficulty(Difficulty.EASY);
                problemBasicAddDto.setIsActive(true);
                problemBasicAddDto.setLanguageSupport(List.of("Java"));
                problemBasicAddDto.setSkills(new ArrayList());
                problemBasicAddDto.setStatus(ProblemStatus.PUBLIC);
                problemBasicAddDto.setTopics(new ArrayList());

                ProblemEditorialDto problemEditorialDto = new ProblemEditorialDto();
                EditorialDto editorialDto = new EditorialDto();
                editorialDto.setEditorialTitle("test editorial title");
                editorialDto.setEditorialTextSolution("test editorial text solution");
                editorialDto.setEditorialSkills(new ArrayList<>());
                SolutionCodeDto solutionCodeDto = new SolutionCodeDto();
                solutionCodeDto.setSolutionCode("test code");
                solutionCodeDto.setSolutionLanguage("Java");
                editorialDto.setSolutionCodes(List.of(solutionCodeDto));
                problemEditorialDto.setEditorialDtos(editorialDto);

                ProblemInputParameterDto problemInputParameterDto = new ProblemInputParameterDto();
                problemInputParameterDto.setFunctionSignature("test function signature");
                problemInputParameterDto.setLanguage("Java");
                problemInputParameterDto.setReturnType(InputType.INT);
                InputParameterDto inputParameterDto = new InputParameterDto();
                inputParameterDto.setInputName("test");
                inputParameterDto.setInputType(InputType.INT);
                problemInputParameterDto.setParameters(List.of(inputParameterDto));
                TemplateCode templateCode = new TemplateCode();
                templateCode.setCode("test code");
                templateCode.setLanguage("Java");
                problemInputParameterDto.setTemplateCode(templateCode);
                List<ProblemInputParameterDto> problemInputParameterDtoList = List.of(problemInputParameterDto);

                TestCaseDto testCase = new TestCaseDto();
                testCase.setInput(new HashMap());
                testCase.setExpectedOutput("test output");
                testCase.setIsSample(true);
                List<TestCaseDto> testCaseList = List.of(testCase);
                ProblemTestCaseDto problemTestCaseDto = new ProblemTestCaseDto();
                problemTestCaseDto.setLanguage("Java");
                problemTestCaseDto.setTestCases(testCaseList);
                List<ProblemTestCaseDto> problemTestCaseDtoList = List.of(problemTestCaseDto);

                MultipartFile excelFile = mock(MultipartFile.class);

                when(problemRepository.findByLink(any())).thenReturn(Optional.of(problem)).thenReturn(Optional.empty());
                when(languageService.findByName(any())).thenReturn(language);
                when(languageService.getLanguagesByNameList(any())).thenReturn(Set.of(language));
                when(problemRepository.isTitleExisted(anyString())).thenReturn(false);
                when(excelService.readTestCaseExcel(any(), anyList(), anyString()))
                                .thenReturn(problemTestCaseDtoList);
                when(problemRequestMapper.mapTo(any())).thenReturn(new Problem());
                when(userService.getCurrentUser()).thenReturn(new Users());
                when(problemRepository.save(any())).thenReturn(new Problem());
                String lambdaResult = "{\"isAccepted\":true,\"time\":\"1.0\",\"memoryUsage\":\"128\",\"noSuccessTestcase\":2,\"results\":[]}";
                when(lambdaService.invokeLambdaFunction(any())).thenReturn(lambdaResult);

                problemService.editProblem(link, problemBasicAddDto, problemEditorialDto,
                                problemInputParameterDtoList, excelFile);

                verify(problemRepository, times(1)).save(any());
        }

        @Test
        void testGetProblemSolutionDetail() {
                Long solutionId = 1L;

                Users currentUser = new Users();
                currentUser.setId(1L);

                ProblemSolution problemSolution = new ProblemSolution();
                problemSolution.setId(solutionId);
                problemSolution.setCreatedBy(currentUser);

                ProblemResponseDto problem = new ProblemResponseDto();
                problem.setId(1L);

                when(userService.getCurrentUser()).thenReturn(currentUser);
                when(problemSolutionService.findSolutionById(anyLong())).thenReturn(problemSolution);
                when(solutionCodeService.findBySolution(any(ProblemSolution.class))).thenReturn(new ArrayList());
                when(problemResponseMapper.mapFrom(any(Problem.class))).thenReturn(problem);

                ProblemSolutionDto result = problemService.getProblemSolutionDetail(solutionId);
                assertNotNull(result);
        }

        @Test
        public void testTagFavouriteProblemSuccess() {
                String link = "test";

                Problem problem = new Problem();
                problem.setId(1L);
                problem.setTitle("test title");
                problem.setLink(link);
                problem.setUsersFavourite(new HashSet());

                Users currentUser = new Users();
                currentUser.setEmail("test");

                when(problemRepository.findByLinkAndStatusAndIsActive(link, ProblemStatus.PUBLIC, true))
                                .thenReturn(Optional.of(problem));
                problemService.tagFavouriteProblem(link);
                verify(problemRepository, times(1)).save(any());
        }

        @Test
        public void testTagFavouriteProblemAlreadyFavourite() {
                String link = "test";

                Users currentUser = new Users();
                currentUser.setEmail("test");

                Problem problem = new Problem();
                problem.setId(1L);
                problem.setTitle("test title");
                problem.setLink(link);
                problem.setUsersFavourite(Set.of(currentUser));

                when(userService.getCurrentUser()).thenReturn(currentUser);
                when(problemRepository.findByLinkAndStatusAndIsActive(link, ProblemStatus.PUBLIC, true))
                                .thenReturn(Optional.of(problem));
                BadRequestException badRequestException = assertThrows(
                                BadRequestException.class,
                                () -> problemService.tagFavouriteProblem(link));
                assertEquals("This problem has already in your favourite", badRequestException.getMessage());
                assertEquals("This problem has already in your favourite", badRequestException.getDetails());
        }

        @Test
        public void testUntagFavouriteProblemSuccess() {
                String link = "test";

                Users currentUser = new Users();
                currentUser.setEmail("test");

                Problem problem = new Problem();
                problem.setId(1L);
                problem.setTitle("test title");
                problem.setLink(link);
                problem.setUsersFavourite(new HashSet<>(Set.of(currentUser))); // Sử dụng HashSet để có thể sửa đổi

                when(userService.getCurrentUser()).thenReturn(currentUser);
                when(problemRepository.findByLinkAndStatusAndIsActive(link, ProblemStatus.PUBLIC, true))
                                .thenReturn(Optional.of(problem));

                problemService.untagFavouriteProblem(link);

                verify(problemRepository, times(1)).save(any());
        }

}