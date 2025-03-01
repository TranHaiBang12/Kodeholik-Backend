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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.lambda.TestCase;
import com.g44.kodeholik.model.dto.request.problem.ProblemCompileRequestDto;
import com.g44.kodeholik.model.dto.request.problem.ProblemRequestDto;
import com.g44.kodeholik.model.dto.request.problem.add.ProblemBasicAddDto;
import com.g44.kodeholik.model.dto.request.problem.add.ProblemEditorialDto;
import com.g44.kodeholik.model.dto.request.problem.add.ProblemInputParameterDto;
import com.g44.kodeholik.model.dto.request.problem.add.ProblemTestCaseDto;
import com.g44.kodeholik.model.dto.request.problem.search.SearchProblemRequestDto;
import com.g44.kodeholik.model.dto.response.problem.NoAchivedInformationResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemCompileResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemDescriptionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.SubmissionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.run.RunProblemResponseDto;
import com.g44.kodeholik.model.elasticsearch.ProblemElasticsearch;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemTemplate;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.problem.Difficulty;
import com.g44.kodeholik.model.enums.problem.ProblemStatus;
import com.g44.kodeholik.repository.elasticsearch.ProblemElasticsearchRepository;
import com.g44.kodeholik.repository.problem.ProblemRepository;
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
                problem.setComments(new HashSet());

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
                problem.setComments(new HashSet());

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
                Problem problem = new Problem();

                when(problemRepository.findById(anyLong())).thenReturn(Optional.empty());

                NotFoundException notFoundException = assertThrows(
                                NotFoundException.class,
                                () -> problemService.getProblemById(1L));
                assertEquals("Problem not found", notFoundException.getMessage());
                assertEquals("Problem not found", notFoundException.getDetails());
                verify(problemRepository, times(1)).findById(anyLong());
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

                when(problemRepository.findByLinkAndStatusAndIsActive(anyString(), any(), anyBoolean()))
                                .thenReturn(Optional.of(problem));
                when(problemTestCaseService.getTestCaseByProblem(any())).thenReturn(new ArrayList<>());

                List<TestCase> result = problemService.getTestCaseByProblem("test-link");

                assertNotNull(result);
        }

        @Test
        void testGetSampleTestCaseByProblem() {
                Problem problem = new Problem();

                when(problemRepository.findByLinkAndStatusAndIsActive(anyString(), any(), anyBoolean()))
                                .thenReturn(Optional.of(problem));
                when(problemTestCaseService.getSampleTestCaseByProblem(any())).thenReturn(new ArrayList<>());

                List<TestCase> result = problemService.getSampleTestCaseByProblem("test-link");

                assertNotNull(result);
        }

        @Test
        void testGetProblemCompileInformationById() {
                Problem problem = new Problem();

                when(problemRepository.findByLinkAndStatusAndIsActive(anyString(), any(), anyBoolean()))
                                .thenReturn(Optional.of(problem));
                when(problemTestCaseService.getProblemCompileInformationByProblem(any(), anyString()))
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
        void testAddProblem() {
                ProblemBasicAddDto problemBasicAddDto = new ProblemBasicAddDto();
                ProblemEditorialDto problemEditorialDto = new ProblemEditorialDto();
                List<ProblemInputParameterDto> problemInputParameterDto = new ArrayList<>();
                MultipartFile excelFile = mock(MultipartFile.class);

                when(problemRepository.isTitleExisted(anyString())).thenReturn(false);
                // when(excelService.readTestCaseExcel(any(), anyList())).thenReturn(new
                // ProblemTestCaseDto());
                when(problemRequestMapper.mapTo(any())).thenReturn(new Problem());
                when(userService.getCurrentUser()).thenReturn(new Users());
                when(problemRepository.save(any())).thenReturn(new Problem());

                // problemService.addProblem(problemBasicAddDto, problemEditorialDto,
                // problemInputParameterDto, excelFile);

                verify(problemRepository, times(1)).save(any());
        }

}