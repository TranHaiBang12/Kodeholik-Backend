package com.g44.kodeholik.service.problem.impl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g44.kodeholik.controller.problem.ProblemSolutionController;
import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.exception.TestCaseNotPassedException;
import com.g44.kodeholik.model.dto.request.exam.ExamProblemRequestDto;
import com.g44.kodeholik.model.dto.request.exam.SubmitExamRequestDto;
import com.g44.kodeholik.model.dto.request.lambda.InputVariable;
import com.g44.kodeholik.model.dto.request.lambda.LambdaRequest;
import com.g44.kodeholik.model.dto.request.lambda.ResponseResult;
import com.g44.kodeholik.model.dto.request.lambda.TestCase;
import com.g44.kodeholik.model.dto.request.problem.FilterProblemRequestAdminDto;
import com.g44.kodeholik.model.dto.request.problem.ProblemCompileRequestDto;
import com.g44.kodeholik.model.dto.request.problem.ProblemRequestDto;
import com.g44.kodeholik.model.dto.request.problem.add.EditorialDto;
import com.g44.kodeholik.model.dto.request.problem.add.InputParameterDto;
import com.g44.kodeholik.model.dto.request.problem.add.ProblemBasicAddDto;
import com.g44.kodeholik.model.dto.request.problem.add.ProblemEditorialDto;
import com.g44.kodeholik.model.dto.request.problem.add.ProblemInputParameterDto;
import com.g44.kodeholik.model.dto.request.problem.add.ProblemTestCaseDto;
import com.g44.kodeholik.model.dto.request.problem.add.ShareSolutionRequestDto;
import com.g44.kodeholik.model.dto.request.problem.add.SolutionCodeDto;
import com.g44.kodeholik.model.dto.request.problem.add.TemplateCode;
import com.g44.kodeholik.model.dto.request.problem.add.TestCaseDto;
import com.g44.kodeholik.model.dto.request.problem.search.ProblemSortField;
import com.g44.kodeholik.model.dto.request.problem.search.SearchProblemRequestDto;
import com.g44.kodeholik.model.dto.response.exam.student.ExamResultOverviewResponseDto;
import com.g44.kodeholik.model.dto.response.exam.student.ProblemResultOverviewResponseDto;
import com.g44.kodeholik.model.dto.response.problem.EditorialResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ListProblemAdminDto;
import com.g44.kodeholik.model.dto.response.problem.NoAchivedInformationResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemBasicResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemCompileResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemDescriptionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemEditorialResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemInputParameterResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemShortResponseDto;
import com.g44.kodeholik.model.dto.response.problem.TemplateCodeResponseDto;
import com.g44.kodeholik.model.dto.response.problem.overview.ProblemInfoOverviewDto;
import com.g44.kodeholik.model.dto.response.problem.overview.ProblemOverviewReportDto;
import com.g44.kodeholik.model.dto.response.problem.solution.ProblemSolutionDto;
import com.g44.kodeholik.model.dto.response.problem.solution.SolutionListResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.SubmissionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.run.RunProblemResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.AcceptedSubmissionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.CompileErrorResposneDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.FailedSubmissionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.SubmissionListResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.SuccessSubmissionListResponseDto;
import com.g44.kodeholik.model.dto.response.user.ProblemProgressResponseDto;
import com.g44.kodeholik.model.dto.response.user.UserResponseDto;
import com.g44.kodeholik.model.elasticsearch.ProblemElasticsearch;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemInputParameter;
import com.g44.kodeholik.model.entity.problem.ProblemSolution;
import com.g44.kodeholik.model.entity.problem.ProblemSubmission;
import com.g44.kodeholik.model.entity.problem.ProblemTemplate;
import com.g44.kodeholik.model.entity.problem.ProblemTestCase;
import com.g44.kodeholik.model.entity.problem.SolutionCode;
import com.g44.kodeholik.model.entity.problem.SolutionLanguageId;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.model.entity.setting.Skill;
import com.g44.kodeholik.model.entity.setting.Topic;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.problem.Difficulty;
import com.g44.kodeholik.model.enums.problem.InputType;
import com.g44.kodeholik.model.enums.problem.ProblemStatus;
import com.g44.kodeholik.model.enums.problem.SubmissionStatus;
import com.g44.kodeholik.model.enums.setting.Level;
import com.g44.kodeholik.repository.discussion.CommentRepository;
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
import com.g44.kodeholik.util.mapper.request.exam.AddExamRequestMapper;
import com.g44.kodeholik.util.mapper.request.exam.AddExamRequestMapper;
import com.g44.kodeholik.util.mapper.request.problem.ProblemRequestMapper;
import com.g44.kodeholik.util.mapper.response.problem.ListProblemAdminResponseMapper;
import com.g44.kodeholik.util.mapper.response.problem.ProblemBasicResponseMapper;
import com.g44.kodeholik.util.mapper.response.problem.ProblemDescriptionMapper;
import com.g44.kodeholik.util.mapper.response.problem.ProblemResponseMapper;
import com.g44.kodeholik.util.mapper.response.problem.SolutionCodeMapper;
import com.g44.kodeholik.util.mapper.response.user.UserResponseMapper;
import com.g44.kodeholik.util.string.StringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProblemServiceImpl implements ProblemService {

    private final ProblemRequestMapper problemRequestMapper;

    private final ModelMapper modelMapper;

    private final ProblemResponseMapper problemResponseMapper;

    private final ProblemDescriptionMapper problemDescriptionMapper;

    private final ProblemRepository problemRepository;

    private final UserRepository userRepository;

    private final ProblemElasticsearchRepository problemElasticsearchRepository;

    private final ProblemSubmissionService problemSubmissionService;

    private final ElasticsearchClient elasticsearchClient;

    private final ProblemTemplateService problemTemplateService;

    private final ProblemTestCaseService problemTestCaseService;

    private final ProblemInputParameterService problemInputParameterService;

    private final UserService userService;

    private final TagService tagService;

    private final LanguageService languageService;

    private final ProblemSolutionService problemSolutionService;

    private final SolutionCodeService solutionCodeService;

    private final ExcelService excelService;

    private final LambdaService lambdaService;

    private final ProblemBasicResponseMapper problemBasicResponseMapper;

    private final CommentRepository commentRepository;

    private List<List<InputVariable>> inputs = new ArrayList<>();

    private Gson gson = new Gson();

    private SubmissionResponseDto submissionResponseDto;

    private final ListProblemAdminResponseMapper listProblemAdminResponseMapper;

    private Map<String, String> templateLanguage = new HashMap<>();

    private final ObjectMapper objectMapper;

    private final UserResponseMapper userResponseMapper;

    @Transactional
    @Override
    public void syncProblemsToElasticsearch() {
        List<ProblemElasticsearch> problems = problemRepository.findByStatusAndIsActive(ProblemStatus.PUBLIC, true)
                .stream()
                .map(problem -> ProblemElasticsearch.builder()
                        .id(problem.getId())
                        .title(problem.getTitle())
                        .titleSearchAndSort(problem.getTitle())
                        .difficulty(problem.getDifficulty().toString())
                        .acceptanceRate(problem.getAcceptanceRate())
                        .noSubmission(problem.getNoSubmission())
                        .link(problem.getLink())
                        .topics(problem.getTopics().stream().map(Topic::getName).collect(Collectors.toList()))
                        .skills(problem.getSkills().stream().map(Skill::getName).collect(Collectors.toList()))
                        .solved(problemSubmissionService.checkIsCurrentUserSolvedProblem(problem))
                        .build())
                .collect(Collectors.toList());
        problemElasticsearchRepository.saveAll(problems);
    }

    @Override
    public List<ProblemResponseDto> getAllProblems() {
        List<Problem> problems = problemRepository.findAll();
        return problems.stream()
                .map(problemResponseMapper::mapFrom)
                .collect(Collectors.toList());
    }

    @Override
    public ProblemResponseDto getProblemResponseDtoById(String link) {
        Problem problem = problemRepository.findByLink(link)
                .orElseThrow(() -> new NotFoundException("Problem not found", "Problem not found"));
        return problemResponseMapper.mapFrom(problem);
    }

    @Override
    public ProblemResponseDto createProblem(ProblemRequestDto problemRequest) {
        Problem problem = problemRequestMapper.mapTo(problemRequest);
        problem.setAcceptanceRate(0);
        problem.setNoSubmission(0);
        problem.setCreatedAt(Timestamp.from(Instant.now()));
        problem.setCreatedBy(userRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("User not found", "User not found")));
        problem = problemRepository.save(problem);
        return problemResponseMapper.mapFrom(problem);
    }

    @Override
    public ProblemResponseDto updateProblem(String link, ProblemRequestDto problemRequest) {
        Problem problem = problemRepository.findByLink(link)
                .orElseThrow(() -> new NotFoundException("Problem not found", "Problem not found"));
        modelMapper.map(problemRequest, problem);
        problem.setUpdatedAt(Timestamp.from(Instant.now()));
        problem.setUpdatedBy(userRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("User not found", "User not found")));
        problem = problemRepository.save(problem);
        return problemResponseMapper.mapFrom(problem);
    }

    @Override
    public void deleteProblem(String link) {
        Problem problem = problemRepository.findByLink(link)
                .orElseThrow(() -> new NotFoundException("Problem not found", "Problem not found"));
        problemRepository.delete(problem);
    }

    @Override
    public ProblemDescriptionResponseDto getProblemDescriptionById(String link) {
        ProblemDescriptionResponseDto problemDescriptionResponseDto = new ProblemDescriptionResponseDto();
        Problem problem = getPublicProblemById(link);
        List<String> topics = new ArrayList<>();
        for (Topic topic : problem.getTopics()) {
            topics.add(topic.getName());
        }

        List<String> skills = new ArrayList<>();
        for (Skill skill : problem.getSkills()) {
            skills.add(skill.getName());
        }
        problemDescriptionResponseDto = problemDescriptionMapper.mapFrom(problem);
        problemDescriptionResponseDto.setNoComment(commentRepository.countByProblemsContains(problem));
        problemDescriptionResponseDto.setTopicList(topics);
        problemDescriptionResponseDto
                .setNoAccepted(problemSubmissionService.countByIsAcceptedAndProblem(true, problem));
        problemDescriptionResponseDto.setSkillList(skills);
        if (problemSubmissionService.checkIsCurrentUserSolvedProblem(problem)) {
            problemDescriptionResponseDto.setSolved(true);
        } else {
            problemDescriptionResponseDto.setSolved(false);
        }

        Users currentUser = userService.getCurrentUser();
        if (problem.getUsersFavourite().contains(currentUser)) {
            problemDescriptionResponseDto.setFavourite(true);
        } else {
            problemDescriptionResponseDto.setFavourite(false);
        }
        return problemDescriptionResponseDto;
    }

    @Override
    public Problem getProblemById(Long id) {
        return problemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Problem not found", "Problem not found"));
    }

    @Override
    public Problem getProblemByLink(String link) {
        return problemRepository.findByLink(link)
                .orElseThrow(() -> new NotFoundException("Problem not found", "Problem not found"));
    }

    @Override
    public Problem getActivePublicProblemByLink(String link) {
        return problemRepository.findByLinkAndStatusAndIsActive(link, ProblemStatus.PUBLIC, true)
                .orElseThrow(() -> new NotFoundException("Problem not found", "Problem not found"));
    }

    private Problem getPublicProblemById(String link) {
        return problemRepository.findByLinkAndStatusAndIsActive(link, ProblemStatus.PUBLIC, true)
                .orElseThrow(() -> new NotFoundException("Problem not found", "Problem not found"));
    }

    @Override
    public Page<ProblemElasticsearch> searchProblems(SearchProblemRequestDto searchProblemRequestDto, Integer page,
            Integer size, ProblemSortField sortBy, Boolean ascending) {
        int pageN;
        if (page == null) {
            pageN = 0;
        } else {
            pageN = page.intValue();
        }

        int sizeN;
        if (size == null) {
            sizeN = 5;
        } else {
            sizeN = size.intValue();
        }
        // syncProblemsToElasticsearch();
        Pageable pageable;
        if (sortBy != null && !sortBy.equals("")) {
            Sort sort = ascending.booleanValue() ? Sort.by(sortBy.toString()).ascending()
                    : Sort.by(sortBy.toString()).descending();
            pageable = PageRequest.of(pageN, sizeN, sort);
        } else {
            pageable = PageRequest.of(pageN, sizeN);
        }
        try {
            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index("problems") // Tên index của bạn
                    // .sort(sortOptions)
                    .sort(k -> {
                        if (!(sortBy != null && !sortBy.equals(""))) {
                            return k.field(f -> f
                                    .field("_score") // Sắp xếp theo điểm số
                                    .order(SortOrder.Desc));
                        } else {
                            return k.field(f -> f
                                    .field(sortBy.toString()) // Sắp xếp theo điểm số
                                    .order(ascending.booleanValue() ? SortOrder.Asc : SortOrder.Desc));
                        }
                    })
                    .query(q -> q
                            .bool(b -> b
                                    .must(m -> {
                                        if (searchProblemRequestDto.getTitle() != null
                                                && !searchProblemRequestDto.getTitle().equals("")) {
                                            return m.wildcard(t -> t
                                                    .field("title.keyword")
                                                    .value("*" + searchProblemRequestDto.getTitle() + "*")
                                                    .caseInsensitive(true));
                                        } else {
                                            return m.matchAll(ma -> ma);
                                        }
                                    })
                                    .must(m -> {
                                        if (searchProblemRequestDto.getDifficulty() != null
                                                && !searchProblemRequestDto.getDifficulty().isEmpty()) {
                                            return m.terms(t -> t
                                                    .field("difficulty") // Tìm chính xác theo keyword
                                                    .terms(v -> v.value(searchProblemRequestDto.getDifficulty().stream()
                                                            .map(FieldValue::of)
                                                            .collect(Collectors.toList()))));
                                        } else {
                                            return m.matchAll(ma -> ma);
                                        }
                                    })
                                    .must(m -> {
                                        if (searchProblemRequestDto.getTopics() != null
                                                && !searchProblemRequestDto.getTopics().isEmpty()) {
                                            return m.match(t -> t
                                                    .field("topics")
                                                    .query(String.join(" ", searchProblemRequestDto.getTopics())));
                                        } else {
                                            return m.matchAll(ma -> ma);
                                        }
                                    })
                                    .must(m -> {
                                        if (searchProblemRequestDto.getSkills() != null
                                                && !searchProblemRequestDto.getSkills().isEmpty()) {
                                            return m.match(t -> t
                                                    .field("skills")
                                                    .query(String.join(" ", searchProblemRequestDto.getSkills())));
                                        } else {
                                            return m.matchAll(ma -> ma);
                                        }
                                    })))
                    .from((int) pageable.getOffset())
                    .size(pageable.getPageSize()));
            SearchResponse<ProblemElasticsearch> searchResponse = elasticsearchClient.search(searchRequest,
                    ProblemElasticsearch.class);

            List<ProblemElasticsearch> content;
            long totalHits;

            if (searchResponse.hits() != null) {
                content = searchResponse.hits().hits().stream()
                        .map(h -> h.source())
                        .toList();
                totalHits = searchResponse.hits().total() != null ? searchResponse.hits().total().value() : 0;
            } else {
                content = new ArrayList<>();
                totalHits = 0;
            }
            return new PageImpl<>(content, pageable, totalHits);

        } catch (IOException e) {
            throw new RuntimeException("Error querying Elasticsearch", e);
        }

    }

    @Override
    public List<String> getAutocompleteSuggestionsForProblemTitle(String searchText) {
        try {
            // syncProblemsToElasticsearch();
            if (searchText != null) {
                SearchRequest searchRequest = SearchRequest.of(s -> s
                        .index("problems") // Tên index của bạn
                        .size(10) // Giới hạn kết quả trả về chỉ 5 gợi ý
                        .sort(k -> k
                                .field(f -> f
                                        .field("_score")
                                        .order(SortOrder.Desc)))
                        .query(q -> q
                                .bool(b -> b
                                        .must(m -> m
                                                .wildcard(p -> p
                                                        .field("titleSearchAndSort")
                                                        .value("*" + searchText + "*")
                                                        .caseInsensitive(true)// Từ tìm kiếm từ người dùng
                                                )))));

                // Thực hiện truy vấn Elasticsearch
                SearchResponse<ProblemElasticsearch> searchResponse = elasticsearchClient.search(searchRequest,
                        ProblemElasticsearch.class);
                if (searchResponse.hits() != null) {
                    // Lấy các gợi ý từ kết quả trả về
                    return searchResponse.hits().hits().stream()
                            .map(hit -> hit.source().getTitle()) // Lấy title từ kết quả
                            .collect(Collectors.toList());
                }
            }
            return new ArrayList<>();
        } catch (Exception e) {
            throw new RuntimeException("Error querying Elasticsearch", e);
        }
    }

    @Override
    public SubmissionResponseDto submitProblem(String link, ProblemCompileRequestDto problemCompileRequestDto) {
        Problem problem = getActivePublicProblemByLink(link);
        Users currentUser = userService.getCurrentUser();
        Language language = languageService.findByName(problemCompileRequestDto.getLanguageName());
        return problemSubmissionService.submitProblem(problem, problemCompileRequestDto,
                problemTestCaseService.getTestCaseByProblemAndLanguage(problem, language),
                findByProblemAndLanguage(link, problemCompileRequestDto.getLanguageName()), currentUser);
    }

    @Override
    public RunProblemResponseDto run(String link, ProblemCompileRequestDto problemCompileRequestDto) {
        Problem problem = getActivePublicProblemByLink(link);
        Language language = languageService.findByName(problemCompileRequestDto.getLanguageName());
        return problemSubmissionService.run(problem, problemCompileRequestDto,
                problemTestCaseService.getSampleTestCaseByProblemAndLanguage(problem, language),
                findByProblemAndLanguage(link, problemCompileRequestDto.getLanguageName()));
    }

    @Override
    public ProblemTemplate findByProblemAndLanguage(String link, String languageName) {
        Problem problem = getActivePublicProblemByLink(link);
        return problemTemplateService.findByProblemAndLanguage(problem, languageName);
    }

    @Override
    public List<List<TestCase>> getTestCaseByProblem(String link, List<Language> languages) {
        Problem problem = getActivePublicProblemByLink(link);
        return problemTestCaseService.getTestCaseByProblem(problem, languages);
    }

    @Override
    public List<List<TestCase>> getSampleTestCaseByProblem(String link, List<Language> languages) {
        Problem problem = getActivePublicProblemByLink(link);
        return problemTestCaseService.getSampleTestCaseByProblem(problem, languages);

    }

    @Override
    public ProblemCompileResponseDto getProblemCompileInformationById(String link, String languageName) {
        Language language = languageService.findByName(languageName);
        Problem problem = getActivePublicProblemByLink(link);
        return problemTestCaseService.getProblemCompileInformationByProblem(problem, language);
    }

    @Override
    public List<NoAchivedInformationResponseDto> getListNoAchievedInformationByCurrentUser() {
        Users user = userService.getCurrentUser();
        long problemSize = problemRepository.findAll().size();
        List<NoAchivedInformationResponseDto> result = new ArrayList<>();
        NoAchivedInformationResponseDto achievedEasy = getNoAchieved(user, Difficulty.EASY);
        NoAchivedInformationResponseDto achievedMedium = getNoAchieved(user, Difficulty.MEDIUM);
        NoAchivedInformationResponseDto achievedHard = getNoAchieved(user, Difficulty.HARD);
        NoAchivedInformationResponseDto achievedAll = new NoAchivedInformationResponseDto("ALL",
                achievedEasy.getNoAchived() + achievedMedium.getNoAchived() + achievedHard.getNoAchived(),
                problemSize);
        result.add(achievedEasy);
        result.add(achievedMedium);
        result.add(achievedHard);
        result.add(achievedAll);
        return result;
    }

    private NoAchivedInformationResponseDto getNoAchieved(Users user, Difficulty difficulty) {
        List<Problem> problems = problemRepository.findByDifficulty(difficulty);
        long solvedProblems = problemSubmissionService.countByUserAndIsAcceptedAndProblemIn(user, true, problems);
        return new NoAchivedInformationResponseDto(difficulty.toString(), solvedProblems, problems.size());
    }

    @Override
    public void addProblem(ProblemBasicAddDto problemBasicAddDto, ProblemEditorialDto problemEditorialDto,
            List<ProblemInputParameterDto> problemInputParameterDto, MultipartFile excelFile) {
        if (checkTitleExisted(problemBasicAddDto.getTitle())) {
            throw new BadRequestException("Title has already existed", "Title has already existed");
        }

        Set<Language> languages = languageService.getLanguagesByNameList(problemBasicAddDto.getLanguageSupport());
        List<ProblemTestCaseDto> problemTestCaseDtos = new ArrayList();
        for (Language language : languages) {
            List<String> inputNames = getInputName(problemInputParameterDto, language.getName());
            problemTestCaseDtos.addAll(excelService.readTestCaseExcel(excelFile, inputNames,
                    language.getName()));
        }
        if (problemTestCaseDtos.isEmpty()) {
            throw new BadRequestException("Excel file doesn't contain any test case",
                    "Excel file doesn't contain any test case");
        }
        if (!checkTestCase(problemEditorialDto, problemTestCaseDtos,
                problemInputParameterDto)) {
            throw new TestCaseNotPassedException("Your solution code doesn't pass all test case. Please check again",
                    submissionResponseDto);
        }
        Problem problem = addProblemBasic(problemBasicAddDto, languages);
        addProblemInputParameter(problemInputParameterDto, problem,
                problemBasicAddDto.getLanguageSupport());
        addProblemEditorial(problemEditorialDto, problem,
                problemBasicAddDto.getLanguageSupport());
        addProblemTestCase(problemTestCaseDtos, problem, problemInputParameterDto);
        // syncProblemsToElasticsearch();
    }

    private Problem addProblemBasic(ProblemBasicAddDto problemBasicAddDto, Set<Language> languages) {
        Users currentUsers = userService.getCurrentUser();
        String link = getLinkForTitle(problemBasicAddDto.getTitle());
        if (problemRepository.findByLink(link).isPresent()) {
            throw new BadRequestException("Title is not valid because the link is duplicated",
                    "Title is not valid because the link is duplicated");
        }

        Problem problem = new Problem();
        problem.setTitle(problemBasicAddDto.getTitle());
        problem.setDifficulty(problemBasicAddDto.getDifficulty());
        problem.setDescription(problemBasicAddDto.getDescription());
        problem.setAcceptanceRate(0);
        problem.setNoSubmission(0);
        problem.setCreatedAt(Timestamp.from(Instant.now()));
        problem.setCreatedBy(currentUsers);
        problem.setStatus(problemBasicAddDto.getStatus());
        problem.setLink(link);
        problem.setActive(problemBasicAddDto.getIsActive().booleanValue());
        problem.setLanguageSupport(languages);
        Set<Topic> topics = tagService.getTopicsByNameList(problemBasicAddDto.getTopics());
        Set<Skill> skills = tagService.getSkillsByNameList(problemBasicAddDto.getSkills());

        problem.setTopics(topics);
        problem.setSkills(skills);

        return problemRepository.save(problem);
    }

    private Problem editProblemBasic(ProblemBasicAddDto problemBasicAddDto, Problem problem, Set<Language> languages) {
        Users currentUsers = userService.getCurrentUser();
        String link = getLinkForTitle(problemBasicAddDto.getTitle());
        if (!problem.getLink().equals(link)) {
            if (problemRepository.findByLink(link).isPresent()) {
                throw new BadRequestException("Title is not valid because the link is duplicated",
                        "Title is not valid because the link is duplicated");
            }
        }

        problem.setLanguageSupport(languages);
        problem.setTitle(problemBasicAddDto.getTitle());
        problem.setDifficulty(problemBasicAddDto.getDifficulty());
        problem.setDescription(problemBasicAddDto.getDescription());
        problem.setAcceptanceRate(0);
        problem.setNoSubmission(0);
        problem.setUpdatedAt(Timestamp.from(Instant.now()));
        problem.setLink(link);
        problem.setUpdatedBy(currentUsers);
        problem.setStatus(problemBasicAddDto.getStatus());
        problem.setActive(problemBasicAddDto.getIsActive().booleanValue());

        Set<Topic> topics = tagService.getTopicsByNameList(problemBasicAddDto.getTopics());
        Set<Skill> skills = tagService.getSkillsByNameList(problemBasicAddDto.getSkills());

        problem.setTopics(topics);
        problem.setSkills(skills);
        syncProblemsToElasticsearch();

        return problemRepository.save(problem);
    }

    private void addProblemTemplate(ProblemInputParameterDto problemInputParameterDto, TemplateCode templateCode,
            Problem problem, List<String> languageSupports) {
        problemInputParameterDto = generateTemplate(problemInputParameterDto);
        ProblemTemplate template = new ProblemTemplate();
        if (!languageSupports.contains(templateCode.getLanguage().toString())) {
            throw new BadRequestException("Your problem doesn't support this language",
                    "Your problem doesn't support this language");
        }
        template.setProblem(problem);
        template.setLanguage(languageService.findByName(templateCode.getLanguage()));
        template.setTemplateCode(templateCode.getCode());
        template.setFunctionSignature(problemInputParameterDto.getFunctionSignature());
        template.setReturnType(problemInputParameterDto.getReturnType());

        problemTemplateService.addTemplate(template);

    }

    private List<String> getInputName(List<ProblemInputParameterDto> problemInputParameterDtos, String languageName) {
        List<String> inputNames = new ArrayList<>();
        for (int i = 0; i < problemInputParameterDtos.size(); i++) {
            if (problemInputParameterDtos.get(i).getLanguage().equals(languageName)) {
                List<InputParameterDto> inputParameters = problemInputParameterDtos.get(i).getParameters();
                for (InputParameterDto inputParameterDto : inputParameters) {
                    if (!inputNames.contains(inputParameterDto.getInputName())) {
                        inputNames.add(inputParameterDto.getInputName());
                    }
                }
            }
        }
        return inputNames;
    }

    private void addProblemInputParameter(List<ProblemInputParameterDto> problemInputParameterDto, Problem problem,
            List<String> languageSupports) {
        List<ProblemInputParameter> problemInputParameters = new ArrayList();
        if (languageSupports.size() != problemInputParameterDto.size()) {
            throw new BadRequestException(
                    "Please define a list parameter for each of language that your problem support",
                    "Please define a list parameter for each of language that your problem support");
        }
        for (int j = 0; j < problemInputParameterDto.size(); j++) {
            if (!languageSupports.contains(problemInputParameterDto.get(j).getLanguage())) {
                throw new BadRequestException("Your problem doesn't support this language",
                        "Your problem doesn't support this language");
            }
            templateLanguage.put(problemInputParameterDto.get(j).getLanguage(),
                    problemInputParameterDto.get(j).getTemplateCode().getCode());
            List<InputParameterDto> inputParameters = problemInputParameterDto.get(j).getParameters();

            addProblemTemplate(problemInputParameterDto.get(j), problemInputParameterDto.get(j).getTemplateCode(),
                    problem, languageSupports);
            for (int i = 0; i < inputParameters.size(); i++) {
                Language language = languageService.findByName(problemInputParameterDto.get(j).getLanguage());
                ProblemInputParameter problemInputParameter = new ProblemInputParameter();
                problemInputParameter.setProblem(problem);
                problemInputParameter.setLanguage(language);
                Map<String, String> inputMap = new HashMap<String, String>();
                inputMap.put("name", inputParameters.get(i).getInputName());
                inputMap.put("type",
                        inputParameters.get(i).getInputType() != null ? inputParameters.get(i).getInputType().toString()
                                : inputParameters.get(i).getOtherInputType());
                if (inputParameters.get(i).getNoDimension() != null)
                    inputMap.put("noDimension", inputParameters.get(i).getNoDimension().toString());
                String parameterJson = gson.toJson(inputMap);
                problemInputParameter.setParameters(parameterJson);
                problemInputParameters.add(problemInputParameter);
                log.info(parameterJson);
            }
        }
        problemInputParameterService.addListInputParameters(problemInputParameters);
    }

    public List<ProblemSolution> addProblemEditorial(ProblemEditorialDto problemEditorialDto, Problem problem,
            List<String> languageSupports) {
        List<ProblemSolution> problemSolutions = new ArrayList();
        ProblemSolution problemSolution = new ProblemSolution();
        problemSolution.setProblem(problem);
        problemSolution.setTitle(problemEditorialDto.getEditorialDtos().getEditorialTitle());
        problemSolution.setTextSolution(problemEditorialDto.getEditorialDtos().getEditorialTextSolution());
        problemSolution
                .setSkills(tagService.getSkillsByNameList(problemEditorialDto.getEditorialDtos().getEditorialSkills()));
        problemSolution.setProblemImplementation(true);
        problemSolution.setCreatedAt(Timestamp.from(Instant.now()));
        problemSolution.setCreatedBy(userService.getCurrentUser());
        problemSolutions.add(problemSolution);
        problemSolutionService.save(problemSolution);
        addProblemEditorialCode(problemEditorialDto.getEditorialDtos(), problemSolution, languageSupports);

        return problemSolutions;
    }

    public void addProblemEditorialCode(EditorialDto editorialDto, ProblemSolution problemSolution,
            List<String> languageSupports) {
        List<SolutionCodeDto> solutionCodeDtos = editorialDto.getSolutionCodes();
        if (languageSupports.size() != solutionCodeDtos.size()) {
            throw new BadRequestException(
                    "Please define a solution code for each of language that your problem support",
                    "Please define a solution code for each of language that your problem support");
        }
        List<SolutionCode> solutionCodes = new ArrayList();
        for (int i = 0; i < solutionCodeDtos.size(); i++) {
            SolutionCode solutionCode = new SolutionCode();
            if (!languageSupports.contains(solutionCodeDtos.get(i).getSolutionLanguage().toString())) {
                throw new BadRequestException("Your problem doesn't support this language",
                        "Your problem doesn't support this language");
            }
            if (templateLanguage.get(solutionCodeDtos.get(i).getSolutionLanguage()) != null) {
                solutionCode.setCode(templateLanguage.get(solutionCodeDtos.get(i).getSolutionLanguage()) + "\n"
                        + solutionCodeDtos.get(i).getSolutionCode());
            } else {
                solutionCode.setCode(solutionCodeDtos.get(i).getSolutionCode());
            }
            Language language = languageService.findByName(solutionCodeDtos.get(i).getSolutionLanguage());
            solutionCode.setLanguage(language);
            solutionCode.setProblem(problemSolution.getProblem());
            solutionCode.setId(new SolutionLanguageId(problemSolution.getId(), language.getId()));
            solutionCode.setSolution(problemSolution);
            solutionCodes.add(solutionCode);

        }
        solutionCodeService.saveAll(solutionCodes);
    }

    public void addProblemTestCase(List<ProblemTestCaseDto> problemTestCaseDtos,
            Problem problem,
            List<ProblemInputParameterDto> problemInputParameterDtos) {
        int k = 0;
        for (int j = 0; j < problemTestCaseDtos.size(); j++) {
            ProblemTestCaseDto problemTestCaseDto = problemTestCaseDtos.get(j);
            List<TestCaseDto> testCaseDtos = problemTestCaseDto.getTestCases();
            List<ProblemTestCase> problemTestCases = new ArrayList<>();
            for (int i = 0; i < testCaseDtos.size(); i++) {
                ProblemTestCase problemTestCase = new ProblemTestCase();
                problemTestCase.setLanguage(languageService.findByName(problemTestCaseDto.getLanguage()));
                problemTestCase
                        .setInput(generateInputJson(problemInputParameterDtos, problemTestCaseDto, k));
                k++;
                // if (problemInputParameterDtos.get(0).getReturnType() == InputType.STRING) {
                // problemTestCase.setExpectedOutput("\"" +
                // testCaseDtos.get(i).getExpectedOutput() + "\"");
                // } else {
                // problemTestCase.setExpectedOutput(testCaseDtos.get(i).getExpectedOutput());
                // }
                problemTestCase.setExpectedOutput(testCaseDtos.get(i).getExpectedOutput());

                problemTestCase.setProblem(problem);
                problemTestCase.setSample(testCaseDtos.get(i).getIsSample().booleanValue());
                problemTestCases.add(problemTestCase);
            }
            problemTestCaseService.saveListTestCase(problemTestCases);
        }
    }

    private ProblemInputParameterDto generateTemplate(ProblemInputParameterDto problemInputParameterDto) {
        TemplateCode templateCode = problemInputParameterDto.getTemplateCode();
        StringBuilder templateBuilder;
        String template = "";
        templateBuilder = new StringBuilder();
        templateBuilder.append(templateCode.getCode());
        templateBuilder.append("//Function \n ");
        if (templateCode.getLanguage().equals("Java")) {
            templateBuilder.append("public static ");
            templateBuilder
                    .append(getJavaStringForReturnType(
                            problemInputParameterDto.getReturnType() == null
                                    ? problemInputParameterDto.getOtherReturnType()
                                    : problemInputParameterDto.getReturnType().toString(),
                            problemInputParameterDto.getNoDimension()))
                    .append(" ")
                    .append(problemInputParameterDto.getFunctionSignature()).append("(");
            List<InputParameterDto> inputs = problemInputParameterDto.getParameters();
            for (int j = 0; j < inputs.size(); j++) {
                templateBuilder
                        .append(getJavaStringForReturnType(
                                inputs.get(j).getInputType() == null ? inputs.get(j).getOtherInputType()
                                        : inputs.get(j).getInputType().toString(),
                                inputs.get(j).getNoDimension()))
                        .append(" ")
                        .append(inputs.get(j).getInputName());
                if (inputs.size() - j > 1) {
                    templateBuilder.append(", ");
                }
            }
            templateBuilder.append(") {\n}");
            template = templateBuilder.toString();
            templateCode.setCode(template);
        } else if (templateCode.getLanguage().equals("C")) {
            templateBuilder
                    .append(getCStringForReturnType(
                            problemInputParameterDto.getReturnType() == null
                                    ? problemInputParameterDto.getOtherReturnType()
                                    : problemInputParameterDto.getReturnType().toString(),
                            problemInputParameterDto.getNoDimension()))
                    .append(" ")
                    .append(problemInputParameterDto.getFunctionSignature()).append("(");
            List<InputParameterDto> inputs = problemInputParameterDto.getParameters();
            for (int j = 0; j < inputs.size(); j++) {
                templateBuilder
                        .append(getCStringForReturnType(
                                inputs.get(j).getInputType() == null ? inputs.get(j).getOtherInputType()
                                        : inputs.get(j).getInputType().toString(),
                                inputs.get(j).getNoDimension()))
                        .append(" ")
                        .append(inputs.get(j).getInputName());
                if (inputs.size() - j > 1) {
                    templateBuilder.append(", ");
                }
            }
            templateBuilder.append(") {\n}");
            template = templateBuilder.toString();
            templateCode.setCode(template);
        }

        problemInputParameterDto.setTemplateCode(templateCode);
        return problemInputParameterDto;
    }

    private String getCStringForReturnType(String type, Integer noDimension) {
        String dimension = "";
        if (noDimension != null) {
            for (int i = 0; i < noDimension; i++) {
                dimension += "*";
            }
        }
        switch (type) {
            case "CHAR":
                return "char";
            case "ARR_INT":
                return "int" + dimension;
            case "ARR_DOUBLE":
                return "double" + dimension;
            case "ARR_STRING":
                return "char" + dimension;
            case "ARR_OBJECT":
                return "void**";

            case "MAP":
                return "HashMap*"; // Cần tự định nghĩa hoặc sử dụng thư viện ngoài
            case "LIST":
                return "LinkedList*"; // Cần tự định nghĩa danh sách liên kết

            case "INT":
                return "int";
            case "LONG":
                return "long";
            case "DOUBLE":
                return "double";
            case "BOOLEAN":
                return "bool"; // Trong C, không có kiểu boolean, thường dùng int (0: false, 1: true)
            case "ARR_CHAR":
                return "char*";
            case "STRING":
                return "string"; // Chuỗi trong C là con trỏ đến mảng ký tự
            case "OBJECT":
                return "void*"; // Con trỏ void cho kiểu dữ liệu không xác định

            default:
                return type;
        }
    }

    private String getJavaStringForReturnType(String type, Integer noDimension) {
        String dimension = "";
        if (noDimension != null) {
            for (int i = 0; i < noDimension; i++) {
                dimension += "[]";
            }
        }
        switch (type) {
            case "ARR_INT":
                return "int" + dimension;
            case "ARR_DOUBLE":
                return "double" + dimension;

            case "ARR_STRING":
                return "String" + dimension;

            case "ARR_OBJECT":
                return "Object" + dimension;
            case "ARR_CHAR":
                return "char" + dimension;
            case "MAP":
                return "Map";
            case "LIST":
                return "List";
            case "SET":
                return "Set";
            case "INT":
                return "int";
            case "LONG":
                return "long";
            case "DOUBLE":
                return "double";
            case "BOOLEAN":
                return "boolean";
            case "STRING":
                return "String";
            case "OBJECT":
                return "Object";
            case "CHAR":
                return "char";
            default:
                return type;
        }
    }

    private boolean checkTestCase(
            ProblemEditorialDto problemEditorialDto,
            List<ProblemTestCaseDto> problemTestCaseDtos,
            List<ProblemInputParameterDto> problemInputParameterDtos) {
        inputs = new ArrayList();

        for (int k = 0; k < problemInputParameterDtos.size(); k++) {
            ProblemInputParameterDto problemInputParameterDto = problemInputParameterDtos.get(k);
            ProblemTestCaseDto problemTestCaseDto = new ProblemTestCaseDto();
            for (int m = 0; m < problemTestCaseDtos.size(); m++) {
                if (problemTestCaseDtos.get(m).getLanguage().equals(problemInputParameterDto.getLanguage())) {
                    problemTestCaseDto = problemTestCaseDtos.get(m);

                    break;
                }
            }
            List<InputParameterDto> inputDtos = problemInputParameterDto.getParameters();
            List<TestCase> testCases = new ArrayList<>();
            List<TestCaseDto> testCaseDtos = problemTestCaseDto.getTestCases();
            for (int i = 0; i < testCaseDtos.size(); i++) {
                List<InputVariable> inputVariables = new ArrayList();
                Map<String, String> inputName = testCaseDtos.get(i).getInput();
                for (int j = 0; j < inputDtos.size(); j++) {
                    String rawValue = inputName.get(inputDtos.get(j).getInputName());
                    // Object parsedValue = parseValue(rawValue, inputDtos.get(j).getInputType(),
                    // gson);
                    Object parsedValue = rawValue;
                    InputVariable input = new InputVariable(
                            inputDtos.get(j).getInputName(),
                            inputDtos.get(j).getInputType().toString(),
                            parsedValue,
                            inputDtos.get(j).getNoDimension());

                    inputVariables.add(input);
                }
                TestCase testCase = new TestCase();
                testCase.setInput(inputVariables);
                inputs.add(inputVariables);
                // if (problemInputParameterDto.getReturnType() == InputType.STRING) {
                // testCase.setExpectedOutput("\"" + testCaseDtos.get(i).getExpectedOutput() +
                // "\"");
                // } else {
                // testCase.setExpectedOutput(testCaseDtos.get(i).getExpectedOutput());
                // }
                testCase.setExpectedOutput(testCaseDtos.get(i).getExpectedOutput());

                testCases.add(testCase);
            }

            List<SolutionCodeDto> solutionCodes = problemEditorialDto.getEditorialDtos().getSolutionCodes();
            SolutionCodeDto solutionCodeDto = new SolutionCodeDto();
            for (int j = 0; j < solutionCodes.size(); j++) {
                if (solutionCodes.get(j).getSolutionLanguage().equals(problemInputParameterDto.getLanguage())) {
                    solutionCodeDto = solutionCodes.get(j);
                }
            }
            log.info(testCases);
            LambdaRequest lambdaRequest = new LambdaRequest();
            lambdaRequest.setCode(solutionCodeDto.getSolutionCode());
            lambdaRequest.setLanguage(solutionCodeDto.getSolutionLanguage());
            lambdaRequest.setFunctionSignature(problemInputParameterDto.getFunctionSignature());
            if (solutionCodeDto.getSolutionLanguage().equals("C")) {
                lambdaRequest.setReturnType(getCStringForReturnType(
                        problemInputParameterDto.getReturnType() == null ? problemInputParameterDto.getOtherReturnType()
                                : problemInputParameterDto.getReturnType().toString(),
                        problemInputParameterDto.getNoDimension()));
            } else if (solutionCodeDto.getSolutionLanguage().equals("Java")) {
                lambdaRequest.setReturnType(getJavaStringForReturnType(
                        problemInputParameterDto.getReturnType() == null ? problemInputParameterDto.getOtherReturnType()
                                : problemInputParameterDto.getReturnType().toString(),
                        problemInputParameterDto.getNoDimension()));
            }
            lambdaRequest.setTestCases(testCases);
            log.info(lambdaRequest);

            String result = lambdaService.invokeLambdaFunction(lambdaRequest);
            String status = "";
            ResponseResult responseResult = new ResponseResult();
            submissionResponseDto = null;
            try {
                responseResult = gson.fromJson(result, ResponseResult.class);
                log.info(result);
                log.info(responseResult);
                if (responseResult.isAccepted()) {
                    status = "ACCEPTED";
                } else {
                    status = "FAILED";
                }
            } catch (Exception e) {
                log.info(e.getMessage());
                status = result;
            }
            switch (status) {
                case "ACCEPTED":
                    submissionResponseDto = new AcceptedSubmissionResponseDto(
                            responseResult.getTime(),
                            responseResult.getMemoryUsage(),
                            solutionCodeDto.getSolutionCode(),
                            solutionCodeDto.getSolutionLanguage().toLowerCase(),
                            responseResult.getNoSuccessTestcase(),
                            Timestamp.from(Instant.now()),
                            SubmissionStatus.SUCCESS, 0L);
                    break;
                case "FAILED":
                    submissionResponseDto = new FailedSubmissionResponseDto(
                            responseResult.getNoSuccessTestcase(),
                            testCases.size(),
                            responseResult.getInputWrong(),
                            solutionCodeDto.getSolutionCode(),
                            solutionCodeDto.getSolutionLanguage().toLowerCase(),
                            Timestamp.from(Instant.now()),
                            SubmissionStatus.FAILED, 0L);
                    return false;
                default:
                    submissionResponseDto = new CompileErrorResposneDto(
                            status,
                            solutionCodeDto.getSolutionCode(),
                            solutionCodeDto.getSolutionLanguage().toLowerCase(),
                            Timestamp.from(Instant.now()),
                            SubmissionStatus.FAILED, 0L);
                    return false;
            }

        }
        return true;

    }

    private String generateInputJson(List<ProblemInputParameterDto> problemInputParameterDto,
            ProblemTestCaseDto problemTestCaseDto,
            int index) {
        return gson.toJson(inputs.get(index));

    }

    public Object parseMultiDimArray(String rawValue, String type) {
        JsonElement jsonElement = JsonParser.parseString(rawValue);
        return parseJsonArray(jsonElement, type);
    }

    // Xử lý đệ quy để xác định cấp độ sâu của mảng
    private Object parseJsonArray(JsonElement jsonElement, String typeData) {
        if (!jsonElement.isJsonArray()) {
            throw new IllegalArgumentException("Input is not a JSON array!");
        }

        JsonArray jsonArray = jsonElement.getAsJsonArray();
        // Nếu phần tử đầu tiên là một mảng → Đệ quy
        if (jsonArray.size() > 0 && jsonArray.get(0).isJsonArray()) {
            JsonElement el = jsonArray.get(0);
            int noDimension = 1;
            while (el.isJsonArray()) {
                el = el.getAsJsonArray().get(0);
                noDimension++;
            }
            if (noDimension == 2) {
                if (typeData.equals("INT")) {
                    Type type = new TypeToken<List<List<Integer>>>() {
                    }.getType();
                    return gson.fromJson(jsonArray, type);
                } else if (typeData.equals("DOUBLE")) {
                    Type type = new TypeToken<List<List<Double>>>() {
                    }.getType();
                    return gson.fromJson(jsonArray, type);
                } else if (typeData.equals("STRING")) {
                    Type type = new TypeToken<List<List<String>>>() {
                    }.getType();
                    return gson.fromJson(jsonArray, type);
                } else {
                    Type type = new TypeToken<List<List<Object>>>() {
                    }.getType();
                    return gson.fromJson(jsonArray, type);
                }
            } else if (noDimension == 3) {
                if (typeData.equals("INT")) {

                    Type type = new TypeToken<List<List<List<Integer>>>>() {
                    }.getType();
                    return gson.fromJson(jsonArray, type);
                } else if (typeData.equals("DOUBLE")) {
                    Type type = new TypeToken<List<List<List<Double>>>>() {
                    }.getType();
                    return gson.fromJson(jsonArray, type);
                } else if (typeData.equals("STRING")) {
                    Type type = new TypeToken<List<List<List<String>>>>() {
                    }.getType();
                    return gson.fromJson(jsonArray, type);
                } else {
                    Type type = new TypeToken<List<List<List<Object>>>>() {
                    }.getType();
                    return gson.fromJson(jsonArray, type);
                }
            } else {
                throw new BadRequestException("System only take three dimensional array",
                        "System only take three dimensional array");
            }
        } else {
            if (typeData.equals("INT")) {
                Type type = new TypeToken<List<Integer>>() {
                }.getType();
                return gson.fromJson(jsonArray, type);
            } else if (typeData.equals("DOUBLE")) {
                Type type = new TypeToken<List<Double>>() {
                }.getType();
                return gson.fromJson(jsonArray, type);
            } else if (typeData.equals("STRING")) {
                Type type = new TypeToken<List<String>>() {
                }.getType();
                return gson.fromJson(jsonArray, type);
            } else {
                Type type = new TypeToken<List<Object>>() {
                }.getType();
                return gson.fromJson(jsonArray, type);
            }
        }

    }

    public Object parseValue(String rawValue, InputType type, Gson gson) {
        if (rawValue == null || rawValue.equalsIgnoreCase("null")) {
            return null;
        }
        switch (type) {
            case ARR_INT:
                log.info("HIHI: " + parseMultiDimArray(rawValue, "INT"));
                return parseMultiDimArray(rawValue, "INT");
            case ARR_DOUBLE:
                return parseMultiDimArray(rawValue, "DOUBLE");
            case ARR_STRING:
                return parseMultiDimArray(rawValue, "STRING");
            case ARR_OBJECT:
                return parseMultiDimArray(rawValue, "OBJECT");
            case MAP:
                return gson.fromJson(rawValue, new TypeToken<Map<String, Object>>() {
                }.getType());
            case LIST:
                return gson.fromJson(rawValue, new TypeToken<List<Object>>() {
                }.getType());
            case SET:
                return gson.fromJson(rawValue, new TypeToken<Set>() {
                }.getType());
            case INT:
                return Integer.parseInt(rawValue);
            case LONG:
                return Long.parseLong(rawValue);
            case DOUBLE:
                return Double.parseDouble(rawValue);
            case BOOLEAN:
                return Boolean.parseBoolean(rawValue);
            case CHAR:
                return rawValue.charAt(0);
            case STRING:
                return rawValue; // Không cần parse, giữ nguyên chuỗi
            case OBJECT:
                return gson.fromJson(rawValue, Object.class);
            default:
                throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }

    @Override
    public void editProblem(String link, ProblemBasicAddDto problemBasicAddDto,
            ProblemEditorialDto problemEditorialDto,
            List<ProblemInputParameterDto> problemInputParameterDto,
            MultipartFile excelFile) {
        Problem problem = getProblemByLink(link);
        if (checkTitleExistedForUpdate(problemBasicAddDto.getTitle(), problem.getTitle())) {
            throw new BadRequestException("Title has already existed", "Title has already existed");
        }
        Set<Language> languages = languageService.getLanguagesByNameList(problemBasicAddDto.getLanguageSupport());
        List<ProblemTestCaseDto> problemTestCaseDtos = new ArrayList();
        for (Language language : languages) {
            List<String> inputNames = getInputName(problemInputParameterDto, language.getName());
            problemTestCaseDtos.addAll(excelService.readTestCaseExcel(excelFile, inputNames,
                    language.getName()));
        }
        if (problemTestCaseDtos.isEmpty()) {
            throw new BadRequestException("Excel file doesn't contain any test case",
                    "Excel file doesn't contain any test case");
        }
        if (!checkTestCase(problemEditorialDto, problemTestCaseDtos, problemInputParameterDto)) {
            throw new TestCaseNotPassedException("Your solution code doesn't pass all test case. Please check again",
                    submissionResponseDto);
        }
        deleteProblemDependency(problem);
        editProblemBasic(problemBasicAddDto, problem, languages);
        addProblemInputParameter(problemInputParameterDto, problem, problemBasicAddDto.getLanguageSupport());
        addProblemEditorial(problemEditorialDto, problem, problemBasicAddDto.getLanguageSupport());
        addProblemTestCase(problemTestCaseDtos, problem, problemInputParameterDto);
    }

    private boolean checkTitleExistedForUpdate(String newTitle, String oldTitle) {
        if (!oldTitle.equals(newTitle)) {
            return problemRepository.isTitleExisted(newTitle);
        }
        return false;
    }

    @Transactional
    private void deleteProblemDependency(Problem problem) {
        solutionCodeService.deleteAllEditorialCodeByProblem(problem);
        problemSolutionService.deleteEditorialByProblem(problem);
        problemTemplateService.deleteTemplatesByProblem(problem);
        problemInputParameterService.deleteProblemInputParameters(problem);
        problemTestCaseService.removeTestCaseByProblem(problem);
    }

    @Override
    public void activateProblem(String link) {
        Users currentUser = userService.getCurrentUser();
        Problem problem = getProblemByLink(link);
        if (problem.isActive()) {
            throw new com.g44.kodeholik.exception.BadRequestException("Problem is already active",
                    "Problem is already active");
        }
        problem.setActive(true);
        problem.setUpdatedAt(Timestamp.from(Instant.now()));
        problem.setUpdatedBy(currentUser);
        problemRepository.save(problem);
    }

    @Override
    public void deactivateProblem(String link) {
        Users currentUser = userService.getCurrentUser();
        Problem problem = getProblemByLink(link);
        if (!problem.isActive()) {
            throw new com.g44.kodeholik.exception.BadRequestException("Problem is already active",
                    "Problem is already active");
        }
        problem.setActive(false);
        problem.setUpdatedAt(Timestamp.from(Instant.now()));
        problem.setUpdatedBy(currentUser);
        problemRepository.save(problem);
    }

    @Override
    public boolean checkTitleExisted(String title) {
        return problemRepository.isTitleExisted(title);
    }

    @Override
    public ProblemBasicResponseDto getProblemBasicResponseDto(String link) {
        Problem problem = getProblemByLink(link);
        ProblemBasicResponseDto problemBasicResponseDto = problemBasicResponseMapper.mapFrom(problem);
        List<String> skills = new ArrayList<>();
        for (Skill skill : problem.getSkills()) {
            skills.add(skill.getName());
        }
        List<String> topics = new ArrayList<>();

        for (Topic topic : problem.getTopics()) {
            topics.add(topic.getName());
        }
        problemBasicResponseDto.setTopics(topics);
        problemBasicResponseDto.setSkills(skills);
        return problemBasicResponseDto;
    }

    private String getLinkForTitle(String title) {
        String link = "";
        title = StringUtils.removeSpecialChars(title);
        link = title.toLowerCase().replaceAll(" ", "-");
        return link;
    }

    @Override
    public ProblemEditorialResponseDto getProblemEditorialDtoList(String link) {
        Problem problem = getActivePublicProblemByLink(link);
        ProblemEditorialResponseDto problemEditorialResponseDto = new ProblemEditorialResponseDto();
        List<ProblemSolution> problemSolutions = problemSolutionService.findEditorialByProblem(problem);
        if (!problemSolutions.isEmpty()) {
            ProblemSolution problemSolution = problemSolutions.get(0);
            EditorialResponseDto editorialResponseDto = new EditorialResponseDto();
            List<String> skills = new ArrayList<>();
            for (Skill skill : problemSolution.getSkills()) {
                skills.add(skill.getName());
            }
            editorialResponseDto.setId(problemSolution.getId());
            editorialResponseDto.setProblem(problemResponseMapper.mapFrom(problemSolution.getProblem()));

            editorialResponseDto.setEditorialSkills(skills);
            editorialResponseDto.setEditorialTitle(problemSolution.getTitle());
            editorialResponseDto.setEditorialTextSolution(problemSolution.getTextSolution());
            List<SolutionCodeDto> solutionCodeDtos = solutionCodeService.findBySolution(problemSolution);
            editorialResponseDto.setSolutionCodes(solutionCodeDtos);
            problemEditorialResponseDto.setEditorialDto(editorialResponseDto);
        }
        return problemEditorialResponseDto;
    }

    @Override
    public ProblemEditorialResponseDto getProblemEditorialDtoListTeacher(String link) {
        Problem problem = getProblemByLink(link);
        ProblemEditorialResponseDto problemEditorialResponseDto = new ProblemEditorialResponseDto();
        List<ProblemSolution> problemSolutions = problemSolutionService.findEditorialByProblem(problem);
        if (!problemSolutions.isEmpty()) {
            ProblemSolution problemSolution = problemSolutions.get(0);
            EditorialResponseDto editorialResponseDto = new EditorialResponseDto();
            List<String> skills = new ArrayList<>();
            for (Skill skill : problemSolution.getSkills()) {
                skills.add(skill.getName());
            }
            editorialResponseDto.setId(problemSolution.getId());
            editorialResponseDto.setProblem(problemResponseMapper.mapFrom(problemSolution.getProblem()));

            editorialResponseDto.setEditorialSkills(skills);
            editorialResponseDto.setEditorialTitle(problemSolution.getTitle());
            editorialResponseDto.setEditorialTextSolution(problemSolution.getTextSolution());
            List<SolutionCodeDto> solutionCodeDtos = solutionCodeService.findBySolution(problemSolution);
            editorialResponseDto.setSolutionCodes(solutionCodeDtos);
            problemEditorialResponseDto.setEditorialDto(editorialResponseDto);
        }
        return problemEditorialResponseDto;
    }

    @Override
    public List<ProblemInputParameterResponseDto> getProblemInputParameterDtoList(String link) {
        Problem problem = getActivePublicProblemByLink(link);
        Set<Language> languageSupports = problem.getLanguageSupport();
        List<ProblemInputParameterResponseDto> results = new ArrayList();
        for (Language language : languageSupports) {
            ProblemInputParameterResponseDto problemInputParameterResponseDto = new ProblemInputParameterResponseDto();
            List<ProblemInputParameter> problemInputParameters = problemInputParameterService
                    .getProblemInputParameters(problem, language);
            ProblemTemplate problemTemplate = problemTemplateService.findByProblemAndLanguage(problem,
                    language.getName());

            TemplateCodeResponseDto templateCode = new TemplateCodeResponseDto();
            String[] codes = generateFunctionCode(problemTemplate.getTemplateCode());
            templateCode.setTemplateCode(codes[0]);
            templateCode.setFunctionCode(codes[1]);
            templateCode.setLanguage(problemTemplate.getLanguage().getName());

            problemInputParameterResponseDto.setTemplateCodes(templateCode);
            problemInputParameterResponseDto.setFunctionSignature(problemTemplate.getFunctionSignature());
            problemInputParameterResponseDto.setReturnType(problemTemplate.getReturnType());

            List<InputParameterDto> inputParameterDtoList = new ArrayList();

            for (int i = 0; i < problemInputParameters.size(); i++) {
                InputParameterDto inputParameterDto = new InputParameterDto();
                InputVariable inputVariables = gson.fromJson(problemInputParameters.get(i).getParameters(),
                        InputVariable.class);
                inputParameterDto.setInputName(inputVariables.getName());
                try {
                    inputParameterDto.setInputType(InputType.valueOf(inputVariables.getType()));
                } catch (IllegalArgumentException e) {
                    inputParameterDto.setOtherInputType(inputVariables.getType());
                }

                inputParameterDtoList.add(inputParameterDto);

            }

            problemInputParameterResponseDto.setParameters(inputParameterDtoList);
            results.add(problemInputParameterResponseDto);

        }
        return results;
    }

    @Override
    public byte[] getExcelFile(String link) {
        Problem problem = getProblemByLink(link);
        log.info(problem);
        List<ProblemTestCase> problemTestCases = problemTestCaseService.getTestCaseByProblemAndAllLanguage(problem);
        return excelService.generateTestCaseFile(problemTestCases, problem);
    }

    @Override
    public Page<SolutionListResponseDto> getProblemListSolution(String link, int page, Integer size, String title,
            String languageName,
            List<String> skillNames,
            String sortBy, Boolean ascending, Pageable pageable) {
        Problem problem = getActivePublicProblemByLink(link);
        Users currentUser = userService.getCurrentUser();
        Set<Skill> skills = tagService.getSkillsByNameList(skillNames);
        if (languageName != null && languageName != "") {
            Language language = languageService.findByName(languageName);
            return problemSolutionService.findOtherSolutionByProblem(problem, page, size, title, skills, language,
                    sortBy,
                    ascending,
                    pageable, currentUser);
        } else {
            return problemSolutionService.findOtherSolutionByProblem(problem, page, size, title, skills, null, sortBy,
                    ascending,
                    pageable, currentUser);
        }
    }

    @Override
    public ProblemSolutionDto getProblemSolutionDetail(Long solutionId) {
        Users currentUser = userService.getCurrentUser();
        ProblemSolution problemSolution = problemSolutionService.findSolutionById(solutionId);
        ProblemSolutionDto problemSolutionDto = new ProblemSolutionDto();
        List<String> skills = new ArrayList<>();
        for (Skill skill : problemSolution.getSkills()) {
            skills.add(skill.getName());
        }
        problemSolutionDto.setId(solutionId);
        problemSolutionDto.setNoUpvote(problemSolution.getNoUpvote());
        problemSolutionDto.setSkills(skills);
        problemSolutionDto.setProblem(problemResponseMapper.mapFrom(problemSolution.getProblem()));
        problemSolutionDto.setTitle(problemSolution.getTitle());
        problemSolutionDto.setTextSolution(problemSolution.getTextSolution());
        List<SolutionCodeDto> solutionCodeDtos = solutionCodeService.findBySolution(problemSolution);
        problemSolutionDto.setSolutionCodes(solutionCodeDtos);
        problemSolutionDto
                .setCurrentUserCreated(currentUser.getId().longValue() == problemSolution.getCreatedBy().getId());
        if (problemSolution.getUserVote().contains(currentUser)) {
            problemSolutionDto.setCurrentUserVoted(true);
        } else {
            problemSolutionDto.setCurrentUserVoted(false);

        }
        if (problemSolution.getCreatedBy() != null) {
            UserResponseDto createdUser = userResponseMapper.mapFrom(problemSolution.getCreatedBy());
            problemSolutionDto.setCreatedBy(createdUser);
        }
        if (problemSolution.getUpdatedBy() != null) {
            UserResponseDto updatedUser = userResponseMapper.mapFrom(problemSolution.getUpdatedBy());
            problemSolutionDto.setUpdatedBy(updatedUser);
        }

        return problemSolutionDto;
    }

    @Override
    public void tagFavouriteProblem(String link) {
        Problem problem = getActivePublicProblemByLink(link);
        Set<Users> userFavourite = problem.getUsersFavourite();
        Users currentUser = userService.getCurrentUser();
        for (Users user : userFavourite) {
            if (user.getEmail().equals(currentUser.getEmail())) {
                throw new BadRequestException("This problem has already in your favourite",
                        "This problem has already in your favourite");
            }
        }
        userFavourite.add(currentUser);
        problemRepository.save(problem);
    }

    @Override
    public void untagFavouriteProblem(String link) {
        Problem problem = getActivePublicProblemByLink(link);
        Set<Users> userFavourite = problem.getUsersFavourite();
        Users currentUser = userService.getCurrentUser();

        boolean isInFavourite = false;
        Iterator<Users> iterator = userFavourite.iterator();
        while (iterator.hasNext()) {
            Users user = iterator.next();
            if (user.getEmail().equals(currentUser.getEmail())) {
                iterator.remove(); // Xóa an toàn trong vòng lặp
                problemRepository.save(problem);
                isInFavourite = true;
                break;
            }
        }

        if (!isInFavourite) {
            throw new BadRequestException("This problem is not in your favourite",
                    "This problem is not in your favourite");
        }
    }

    @Override
    public void upvoteSolution(Long solutionId) {
        Users user = userService.getCurrentUser();
        problemSolutionService.upvoteSolution(solutionId, user);
    }

    @Override
    public void unupvoteSolution(Long solutionId) {
        Users user = userService.getCurrentUser();
        problemSolutionService.unupvoteSolution(solutionId, user);
    }

    @Override
    public List<SubmissionListResponseDto> getSubmissionListByUserAndProblem(String link) {
        Problem problem = getActivePublicProblemByLink(link);
        Users user = userService.getCurrentUser();
        return problemSubmissionService.getListSubmission(problem, user);
    }

    @Override
    public ProblemSolutionDto postSolution(ShareSolutionRequestDto shareSolutionRequestDto) {
        Users user = userService.getCurrentUser();
        List<ProblemSubmission> problemSubmissions = new ArrayList<>();
        Problem problem = getActivePublicProblemByLink(shareSolutionRequestDto.getLink());
        shareSolutionRequestDto.setProblem(problem);
        List<Long> submissionIds = shareSolutionRequestDto.getSubmissionId();
        for (int i = 0; i < submissionIds.size(); i++) {
            problemSubmissions.add(problemSubmissionService.getProblemSubmissionById(submissionIds.get(i)));
        }
        shareSolutionRequestDto.setSubmissions(problemSubmissions);

        return problemSolutionService.postSolution(shareSolutionRequestDto, user);
    }

    @Override
    public SubmissionResponseDto getSubmissionDetail(Long submissionId) {
        Users currentUser = userService.getCurrentUser();
        ProblemSubmission problemSubmission = problemSubmissionService.getProblemSubmissionById(submissionId);
        int noTestCase = problemTestCaseService.getNoTestCaseByProblem(problemSubmission.getProblem());
        return problemSubmissionService.getSubmissionDetail(problemSubmission, noTestCase, currentUser);
    }

    @Override
    public List<SuccessSubmissionListResponseDto> getSuccessSubmissionList(String link, List<Long> excludes) {
        Problem problem = getActivePublicProblemByLink(link);
        Users currentUser = userService.getCurrentUser();
        return problemSubmissionService.getSuccessSubmissionList(excludes, problem, currentUser);
    }

    @Override
    public ProblemSolutionDto editSolution(Long solutionId, ShareSolutionRequestDto shareSolutionRequestDto) {
        Users user = userService.getCurrentUser();
        Set<Skill> skills = tagService.getSkillsByNameList(shareSolutionRequestDto.getSkills());
        List<ProblemSubmission> problemSubmissions = new ArrayList<>();
        List<Long> submissionIds = shareSolutionRequestDto.getSubmissionId();
        for (int i = 0; i < submissionIds.size(); i++) {
            problemSubmissions.add(problemSubmissionService.getProblemSubmissionById(submissionIds.get(i)));
        }
        shareSolutionRequestDto.setSubmissions(problemSubmissions);
        return problemSolutionService.editSolution(shareSolutionRequestDto, user, solutionId, skills);
    }

    @Override
    public Page<SubmissionListResponseDto> getListSubmission(String link, SubmissionStatus status, Date start,
            Date end, int page, Integer size, String sortBy, Boolean ascending) {
        Users user = userService.getCurrentUser();
        Problem problem = null;
        if (link != null) {
            problem = getActivePublicProblemByLink(link);
        }
        return problemSubmissionService.getListSubmission(user, problem, status, start, end, page, size, sortBy,
                ascending);
    }

    @Override
    public Map<String, String> getAllProblemHasSubmitted() {
        Users currentUser = userService.getCurrentUser();
        return problemSubmissionService.getAllProblemHasSubmitted(currentUser);
    }

    @Override
    public Page<ProblemResponseDto> findAllProblemUserFavourite(int page, Integer size) {
        if (page < 0) {
            throw new BadRequestException("Page must be greater than 0", "Page must be greater than 0");
        }
        Pageable pageable = PageRequest.of(page, size != null ? size.intValue() : 5);
        Users currentUser = userService.getCurrentUser();
        Page<Problem> problems = problemRepository.findByUsersFavouriteContains(currentUser, pageable);
        return problems.map(problemResponseMapper::mapFrom);
    }

    @Override
    public Page<ProblemProgressResponseDto> findLastSubmittedByUser(int page,
            SubmissionStatus status, Integer size, String sortBy, Boolean ascending) {
        Users currentUser = userService.getCurrentUser();
        return problemSubmissionService.findLastSubmittedByUser(currentUser, status, page, size, sortBy, ascending);
    }

    @Override
    public List<String> getLanguageSupportByProblem(String link) {
        List<String> languageSupport = new ArrayList();
        Problem problem = null;
        if (link != null) {
            problem = getActivePublicProblemByLink(link);
            Set<Language> languageSet = problem.getLanguageSupport();
            for (Language language : languageSet) {
                languageSupport.add(language.getName());
            }
        }
        return languageSupport;
    }

    @Override
    public List<ProblemShortResponseDto> getPrivateProblemShortResponseDto() {
        log.info("test");
        List<ProblemShortResponseDto> result = new ArrayList();
        List<Problem> problems = problemRepository.findByStatusAndIsActive(ProblemStatus.PRIVATE, true);
        for (int i = 0; i < problems.size(); i++) {
            ProblemShortResponseDto problemShortResponseDto = new ProblemShortResponseDto();
            problemShortResponseDto.setId(problems.get(i).getId());
            problemShortResponseDto.setLink(problems.get(i).getLink());
            problemShortResponseDto.setTitle(problems.get(i).getTitle());
            result.add(problemShortResponseDto);
        }
        return result;
    }

    @Override
    public Problem getProblemByExamProblemRequest(ExamProblemRequestDto request) {
        return problemRepository.findByLinkAndStatusAndIsActive(request.getProblemLink(),
                ProblemStatus.PRIVATE, true)
                .orElseThrow(() -> new NotFoundException("Problem not found", "Problem not found"));
    }

    public ProblemTemplate findByAllProblemAndLanguage(String link, String languageName) {
        Problem problem = getProblemByLink(link);
        return problemTemplateService.findByProblemAndLanguage(problem, languageName);
    }

    @Override
    public ExamResultOverviewResponseDto submitExam(List<SubmitExamRequestDto> submitExamRequestDto,
            Users currentUser) {
        ExamResultOverviewResponseDto result = new ExamResultOverviewResponseDto();
        double grade = 0;

        List<ProblemResultOverviewResponseDto> problemResultDetails = new ArrayList();
        for (int i = 0; i < submitExamRequestDto.size(); i++) {
            ProblemCompileRequestDto problemCompileRequestDto = new ProblemCompileRequestDto();
            problemCompileRequestDto.setCode(submitExamRequestDto.get(i).getCode());
            problemCompileRequestDto.setLanguageName(submitExamRequestDto.get(i).getLanguageName());
            Problem problem = getProblemByLink(submitExamRequestDto.get(i).getProblemLink());
            ProblemResultOverviewResponseDto problemResultDetailResponseDto = problemSubmissionService.submitExam(
                    problem,
                    problemCompileRequestDto,
                    problemTestCaseService.getTestCaseByProblemAndLanguage(problem,
                            languageService.findByName(submitExamRequestDto.get(i).getLanguageName())),
                    findByAllProblemAndLanguage(problem.getLink(), submitExamRequestDto.get(i).getLanguageName()),
                    submitExamRequestDto.get(i).getPoint(), currentUser);
            grade += problemResultDetailResponseDto.getPoint();
            problemResultDetails.add(problemResultDetailResponseDto);
        }
        result.setGrade(grade);
        result.setProblemResults(problemResultDetails);
        return result;
    }

    @Override
    public RunProblemResponseDto runExam(String link, ProblemCompileRequestDto problemCompileRequestDto) {
        Problem problem = getProblemByLink(link);
        Language language = languageService.findByName(problemCompileRequestDto.getLanguageName());
        return problemSubmissionService.run(problem, problemCompileRequestDto,
                problemTestCaseService.getSampleTestCaseByProblemAndLanguage(problem, language),
                findByAllProblemAndLanguage(link, problemCompileRequestDto.getLanguageName()));
    }

    @Override
    public Page<ListProblemAdminDto> getListProblemForAdmin(FilterProblemRequestAdminDto filterProblemRequestAdminDto) {
        int page = filterProblemRequestAdminDto.getPage();
        Integer size = filterProblemRequestAdminDto.getSize();
        String sortBy = filterProblemRequestAdminDto.getSortBy();
        Boolean ascending = filterProblemRequestAdminDto.getAscending();
        Sort sort;
        if (sortBy != null && (sortBy.equals("acceptanceRate") || sortBy.equals("noSubmission"))) {
            sort = ascending != null && ascending.booleanValue() ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();
        } else {
            sort = Sort.by("createdAt").descending();
        }
        Pageable pageable = PageRequest.of(page, size == null ? 5 : size.intValue(), sort);
        log.info(filterProblemRequestAdminDto);
        Page<Problem> problems = problemRepository.findByTitleContainsAndDifficultyAndStatusAndIsActive(
                filterProblemRequestAdminDto.getTitle(), filterProblemRequestAdminDto.getDifficulty(),
                filterProblemRequestAdminDto.getStatus(), filterProblemRequestAdminDto.getIsActive(), pageable);
        log.info(problems);
        return problems.map(listProblemAdminResponseMapper::mapFrom);
    }

    public static String[] generateFunctionCode(String templateCode) {
        String split = "//Function \n ";
        int index = templateCode.indexOf(split);
        if (index == -1) {
            return new String[] { templateCode, "" }; // Trả về toàn bộ a nếu không tìm thấy b
        }
        String before = templateCode.substring(0, index);
        String after = templateCode.substring(index + split.length());
        return new String[] { before, after };
    }

    @Override
    public List<ProblemInputParameterResponseDto> getProblemInputParameterDtoListTeacher(String link) {
        Problem problem = getProblemByLink(link);
        Set<Language> languageSupports = problem.getLanguageSupport();
        List<ProblemInputParameterResponseDto> results = new ArrayList();
        for (Language language : languageSupports) {
            ProblemInputParameterResponseDto problemInputParameterResponseDto = new ProblemInputParameterResponseDto();
            List<ProblemInputParameter> problemInputParameters = problemInputParameterService
                    .getProblemInputParameters(problem, language);
            ProblemTemplate problemTemplate = problemTemplateService.findByProblemAndLanguage(problem,
                    language.getName());

            TemplateCodeResponseDto templateCode = new TemplateCodeResponseDto();
            String[] codes = generateFunctionCode(problemTemplate.getTemplateCode());
            templateCode.setTemplateCode(codes[0]);
            templateCode.setFunctionCode(codes[1]);
            templateCode.setLanguage(problemTemplate.getLanguage().getName());

            problemInputParameterResponseDto.setLanguage(language.getName());
            problemInputParameterResponseDto.setTemplateCodes(templateCode);
            problemInputParameterResponseDto.setFunctionSignature(problemTemplate.getFunctionSignature());
            problemInputParameterResponseDto.setReturnType(problemTemplate.getReturnType());

            List<InputParameterDto> inputParameterDtoList = new ArrayList();

            for (int i = 0; i < problemInputParameters.size(); i++) {
                InputParameterDto inputParameterDto = new InputParameterDto();
                log.info(problemInputParameters.get(i).getParameters());
                InputVariable inputVariables = new InputVariable();
                try {
                    inputVariables = objectMapper.readValue(problemInputParameters.get(i).getParameters(),
                            InputVariable.class);
                    inputParameterDto.setInputName(inputVariables.getName());
                    inputParameterDto.setInputType(InputType.valueOf(inputVariables.getType()));
                    inputParameterDto.setNoDimension(inputVariables.getNoDimension());
                } catch (Exception e) {
                    if (inputVariables != null)
                        inputParameterDto.setOtherInputType(inputVariables.getType());
                }

                inputParameterDtoList.add(inputParameterDto);

            }

            problemInputParameterResponseDto.setParameters(inputParameterDtoList);
            results.add(problemInputParameterResponseDto);
        }
        return results;
    }

    @Override
    public List<Map<String, String>> getNumberSkillUserSolved(Level level) {
        Users currentUser = userService.getCurrentUser();
        return problemSubmissionService.getNumberSkillUserSolved(currentUser, level);
    }

    @Override
    public List<Map<String, String>> getNumberTopicUserSolved() {
        Users currentUser = userService.getCurrentUser();
        return problemSubmissionService.getNumberTopicUserSolved(currentUser);
    }

    @Override
    public List<Map<String, String>> getNumberLanguageUserSolved() {
        Users currentUser = userService.getCurrentUser();
        return problemSubmissionService.getNumberLanguageUserSolved(currentUser);
    }

    @Override
    public Map<String, String> getAcceptanceRateAndNoSubmissionByUser() {
        Users currentUser = userService.getCurrentUser();
        return problemSubmissionService.getAcceptanceRateAndNoSubmissionByUser(currentUser);
    }

    private List<NoAchivedInformationResponseDto> getListNoAchievedInformationOverview(List<Problem> problems,
            Timestamp start,
            Timestamp end) {
        long problemSize = problems.size();
        List<NoAchivedInformationResponseDto> result = new ArrayList<>();
        NoAchivedInformationResponseDto achievedEasy = getNoAchievedOverview(Difficulty.EASY, start, end);
        NoAchivedInformationResponseDto achievedMedium = getNoAchievedOverview(Difficulty.MEDIUM, start, end);
        NoAchivedInformationResponseDto achievedHard = getNoAchievedOverview(Difficulty.HARD, start, end);
        NoAchivedInformationResponseDto achievedAll = new NoAchivedInformationResponseDto("ALL",
                achievedEasy.getNoAchived() + achievedMedium.getNoAchived() + achievedHard.getNoAchived(),
                problemSize);
        result.add(achievedEasy);
        result.add(achievedMedium);
        result.add(achievedHard);
        result.add(achievedAll);
        return result;
    }

    private NoAchivedInformationResponseDto getNoAchievedOverview(Difficulty difficulty, Timestamp start,
            Timestamp end) {
        List<Problem> problems;
        if (start == null && end == null) {
            problems = problemRepository.findByDifficulty(difficulty);
        } else {
            problems = problemRepository
                    .findByCreatedAtBetweenAndDifficultyOrderByNoSubmissionDescAcceptanceRateDesc(start, end,
                            difficulty);
        }
        long solvedProblems = problemSubmissionService.countByIsAcceptedAndProblemIn(true, problems);
        return new NoAchivedInformationResponseDto(difficulty.toString(), solvedProblems, problems.size());
    }

    @Override
    public ProblemOverviewReportDto getProblemOverviewReport(Timestamp start, Timestamp end) {
        ProblemOverviewReportDto result = new ProblemOverviewReportDto();
        List<ProblemInfoOverviewDto> topPopularProblems = new ArrayList<>();
        List<ProblemInfoOverviewDto> topFlopProblems = new ArrayList<>();

        long totalProblemCount = 0, totalSubmissions = 0;
        double avgAcceptanceRate = 0;

        if (start == null && end == null) {
            List<Problem> problems = problemRepository.findAllByOrderByNoSubmissionDescAcceptanceRateDesc();
            totalProblemCount = problems.size();
            for (int i = 0; i < totalProblemCount; i++) {
                totalSubmissions += problems.get(i).getNoSubmission();
                avgAcceptanceRate += problems.get(i).getAcceptanceRate();
                if (i < 5) {
                    topPopularProblems.add(mapProblemToProblemInfo(problems.get(i)));
                } else if (totalProblemCount >= 10 && i >= totalProblemCount - 5) {
                    topFlopProblems.add(mapProblemToProblemInfo(problems.get(i)));
                }
            }
            if (totalSubmissions != 0) {
                avgAcceptanceRate = Math.round((double) avgAcceptanceRate / totalProblemCount * 100) / 100.0;

            }

            result.setNoAchivedInformationList(getListNoAchievedInformationOverview(problems, start, end));
            result.setTopPopularProblems(topPopularProblems);
            result.setTopFlopProblems(topFlopProblems);
            result.setTotalProblemCount(totalProblemCount);
            result.setAvgAcceptanceRate(avgAcceptanceRate);
            result.setTotalSubmissions(totalSubmissions);
        } else {
            if (start == null || end == null || start.after(end)) {
                throw new BadRequestException("Please provide both valid start and end time",
                        "Please provide both valid start and end time");
            }
            List<Problem> problems = problemRepository
                    .findByCreatedAtBetweenOrderByNoSubmissionDescAcceptanceRateDesc(start, end);
            totalProblemCount = problems.size();
            List<ProblemSubmission> problemSubmissions = problemSubmissionService.getSubmissionsByTimeBetween(start,
                    end);
            totalSubmissions = problemSubmissions.size();
            long totalSuccessSubmission = 0;
            for (int i = 0; i < totalSubmissions; i++) {
                if (problemSubmissions.get(i).isAccepted()) {
                    totalSuccessSubmission += 1;
                }
            }
            if (totalSubmissions != 0) {
                avgAcceptanceRate = Math.round((double) totalSuccessSubmission / totalSubmissions * 100) / 100.0;
            }

            for (int i = 0; i < totalProblemCount; i++) {
                if (i < 5) {
                    topPopularProblems.add(mapProblemToProblemInfo(problems.get(i)));
                } else if (totalProblemCount >= 10 && i >= totalProblemCount - 5) {
                    topFlopProblems.add(mapProblemToProblemInfo(problems.get(i)));
                }
            }

            result.setNoAchivedInformationList(getListNoAchievedInformationOverview(problems, start, end));
            result.setTopPopularProblems(topPopularProblems);
            result.setTopFlopProblems(topFlopProblems);
            result.setTotalProblemCount(totalProblemCount);
            result.setAvgAcceptanceRate(avgAcceptanceRate);
            result.setTotalSubmissions(totalSubmissions);
        }
        return result;
    }

    private ProblemInfoOverviewDto mapProblemToProblemInfo(Problem problem) {
        ProblemInfoOverviewDto problemInfoOverviewDto = new ProblemInfoOverviewDto();
        problemInfoOverviewDto.setLink(problem.getLink());
        problemInfoOverviewDto.setTitle(problem.getTitle());
        problemInfoOverviewDto.setTotalSubmissions(problem.getNoSubmission());
        problemInfoOverviewDto.setAvgAcceptanceRate(problem.getAcceptanceRate());
        return problemInfoOverviewDto;
    }
}
