package com.g44.kodeholik.service.problem.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.exception.TestCaseNotPassedException;
import com.g44.kodeholik.model.dto.request.lambda.InputVariable;
import com.g44.kodeholik.model.dto.request.lambda.LambdaRequest;
import com.g44.kodeholik.model.dto.request.lambda.ResponseResult;
import com.g44.kodeholik.model.dto.request.lambda.TestCase;
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
import com.g44.kodeholik.model.dto.response.problem.EditorialResponseDto;
import com.g44.kodeholik.model.dto.response.problem.NoAchivedInformationResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemBasicResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemCompileResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemDescriptionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemEditorialResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemInputParameterResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemResponseDto;
import com.g44.kodeholik.model.dto.response.problem.solution.ProblemSolutionDto;
import com.g44.kodeholik.model.dto.response.problem.solution.SolutionListResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.SubmissionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.run.RunProblemResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.AcceptedSubmissionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.CompileErrorResposneDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.FailedSubmissionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.SubmissionListResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.SuccessSubmissionListResponseDto;
import com.g44.kodeholik.model.elasticsearch.ProblemElasticsearch;
import com.g44.kodeholik.model.entity.discussion.Comment;
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
import com.g44.kodeholik.repository.elasticsearch.ProblemElasticsearchRepository;
import com.g44.kodeholik.repository.problem.ProblemRepository;
import com.g44.kodeholik.repository.problem.ProblemSubmissionRepository;
import com.g44.kodeholik.repository.user.UserRepository;
import com.g44.kodeholik.service.CompileService;
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
import com.g44.kodeholik.util.string.StringUtils;
import com.g44.kodeholik.util.mapper.request.problem.ProblemRequestMapper;
import com.g44.kodeholik.util.mapper.response.problem.ProblemBasicResponseMapper;
import com.g44.kodeholik.util.mapper.response.problem.ProblemDescriptionMapper;
import com.g44.kodeholik.util.mapper.response.problem.ProblemResponseMapper;
import com.g44.kodeholik.util.mapper.response.problem.SolutionCodeMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.security.User;
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

    private final SolutionCodeMapper solutionCodeMapper;

    private List<List<InputVariable>> inputs = new ArrayList<>();

    private Gson gson = new Gson();

    private SubmissionResponseDto submissionResponseDto;

    @Async("s3TaskExecutor")
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
        problemDescriptionResponseDto.setNoComment(problem.getComments().size());
        problemDescriptionResponseDto.setTopicList(topics);
        problemDescriptionResponseDto
                .setNoAccepted(problemSubmissionService.countByIsAcceptedAndProblem(true, problem));
        problemDescriptionResponseDto.setSkillList(skills);
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
                                                    .field("title")
                                                    .value("*" + searchProblemRequestDto.getTitle() + "*")
                                                    .caseInsensitive(true));
                                        } else {
                                            return m.matchAll(ma -> ma);
                                        }
                                    }) // Fuzzy Search
                                    .must(m -> {
                                        if (searchProblemRequestDto.getDifficulty() != null
                                                && !searchProblemRequestDto.getDifficulty().isEmpty()) {
                                            return m.match(t -> t
                                                    .field("difficulty")
                                                    .query(String.join(" ", searchProblemRequestDto.getDifficulty())));
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

            List<ProblemElasticsearch> content = searchResponse.hits().hits().stream()
                    .map(h -> h.source())
                    .toList();
            log.info(content);
            Problem problem = problemRepository.findById(24L).get();
            log.info(problem.getLink());
            long totalHits = searchResponse.hits().total() != null ? searchResponse.hits().total().value() : 0;
            return new PageImpl<>(content, pageable, totalHits);

        } catch (IOException e) {
            throw new RuntimeException("Error querying Elasticsearch", e);
        }

    }

    @Override
    public List<String> getAutocompleteSuggestionsForProblemTitle(String searchText) {
        try {
            syncProblemsToElasticsearch();
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

                // Lấy các gợi ý từ kết quả trả về
                return searchResponse.hits().hits().stream()
                        .map(hit -> hit.source().getTitle()) // Lấy title từ kết quả
                        .collect(Collectors.toList());
            }
            return new ArrayList<>();
        } catch (Exception e) {
            throw new RuntimeException("Error querying Elasticsearch", e);
        }
    }

    @Override
    public SubmissionResponseDto submitProblem(String link, ProblemCompileRequestDto problemCompileRequestDto) {
        Problem problem = getActivePublicProblemByLink(link);
        return problemSubmissionService.submitProblem(problem, problemCompileRequestDto,
                getTestCaseByProblem(link),
                findByProblemAndLanguage(link, problemCompileRequestDto.getLanguageName()));
    }

    @Override
    public RunProblemResponseDto run(String link, ProblemCompileRequestDto problemCompileRequestDto) {
        Problem problem = getActivePublicProblemByLink(link);
        return problemSubmissionService.run(problem, problemCompileRequestDto, getSampleTestCaseByProblem(link),
                findByProblemAndLanguage(link, problemCompileRequestDto.getLanguageName()));
    }

    @Override
    public ProblemTemplate findByProblemAndLanguage(String link, String languageName) {
        Problem problem = getActivePublicProblemByLink(link);
        return problemTemplateService.findByProblemAndLanguage(problem, languageName);
    }

    @Override
    public List<TestCase> getTestCaseByProblem(String link) {
        Problem problem = getActivePublicProblemByLink(link);
        return problemTestCaseService.getTestCaseByProblem(problem);
    }

    @Override
    public List<TestCase> getSampleTestCaseByProblem(String link) {
        Problem problem = getActivePublicProblemByLink(link);
        return problemTestCaseService.getSampleTestCaseByProblem(problem);

    }

    @Override
    public ProblemCompileResponseDto getProblemCompileInformationById(String link, String languageName) {
        Problem problem = getActivePublicProblemByLink(link);
        return problemTestCaseService.getProblemCompileInformationByProblem(problem, languageName);
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
        List<String> inputNames = getInputName(problemInputParameterDto);
        ProblemTestCaseDto problemTestCaseDto = excelService.readTestCaseExcel(excelFile, inputNames);
        if (!checkTestCase(problemEditorialDto, problemTestCaseDto, problemInputParameterDto)) {
            throw new TestCaseNotPassedException("Your solution code doesn't pass all test case. Please check again",
                    submissionResponseDto);
        }
        Problem problem = addProblemBasic(problemBasicAddDto);
        addProblemInputParameter(problemInputParameterDto, problem);
        addProblemTemplate(problemInputParameterDto, problem);
        addProblemEditorial(problemEditorialDto, problem);
        addProblemTestCase(problemTestCaseDto, problem, problemInputParameterDto);
        syncProblemsToElasticsearch();
    }

    private Problem addProblemBasic(ProblemBasicAddDto problemBasicAddDto) {
        Users currentUsers = userService.getCurrentUser();

        Problem problem = new Problem();
        problem.setTitle(problemBasicAddDto.getTitle());
        problem.setDifficulty(problemBasicAddDto.getDifficulty());
        problem.setDescription(problemBasicAddDto.getDescription());
        problem.setAcceptanceRate(0);
        problem.setNoSubmission(0);
        problem.setCreatedAt(Timestamp.from(Instant.now()));
        problem.setCreatedBy(currentUsers);
        problem.setStatus(problemBasicAddDto.getStatus());
        problem.setLink(getLinkForTitle(problemBasicAddDto.getTitle()));
        problem.setActive(problemBasicAddDto.getIsActive().booleanValue());
        Set<Topic> topics = tagService.getTopicsByNameList(problemBasicAddDto.getTopics());
        Set<Skill> skills = tagService.getSkillsByNameList(problemBasicAddDto.getSkills());

        problem.setTopics(topics);
        problem.setSkills(skills);

        return problemRepository.save(problem);
    }

    private Problem editProblemBasic(ProblemBasicAddDto problemBasicAddDto, Problem problem) {
        Users currentUsers = userService.getCurrentUser();

        problem.setTitle(problemBasicAddDto.getTitle());
        problem.setDifficulty(problemBasicAddDto.getDifficulty());
        problem.setDescription(problemBasicAddDto.getDescription());
        problem.setAcceptanceRate(0);
        problem.setNoSubmission(0);
        problem.setUpdatedAt(Timestamp.from(Instant.now()));
        problem.setLink(getLinkForTitle(problemBasicAddDto.getTitle()));
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

    private void addProblemTemplate(List<ProblemInputParameterDto> problemInputParameterDtos, Problem problem) {
        List<ProblemTemplate> templates = new ArrayList();
        for (int j = 0; j < problemInputParameterDtos.size(); j++) {
            ProblemInputParameterDto problemInputParameterDto = problemInputParameterDtos.get(j);
            problemInputParameterDto = generateTemplate(problemInputParameterDto);
            List<TemplateCode> templateCodes = problemInputParameterDto.getTemplateCodes();
            for (int i = 0; i < templateCodes.size(); i++) {
                ProblemTemplate template = new ProblemTemplate();
                log.info(templateCodes.get(i).getCode());
                template.setProblem(problem);
                template.setLanguage(languageService.findByName(templateCodes.get(i).getLanguage()));
                template.setTemplateCode(templateCodes.get(i).getCode());
                template.setFunctionSignature(problemInputParameterDto.getFunctionSignature());
                template.setReturnType(problemInputParameterDto.getReturnType());
                templates.add(template);
            }
            problemTemplateService.addListTemplate(templates);
        }
    }

    private List<String> getInputName(List<ProblemInputParameterDto> problemInputParameterDtos) {
        List<String> inputNames = new ArrayList<>();
        for (int i = 0; i < problemInputParameterDtos.size(); i++) {
            List<InputParameterDto> inputParameters = problemInputParameterDtos.get(i).getParameters();
            for (InputParameterDto inputParameterDto : inputParameters) {
                if (!inputNames.contains(inputParameterDto.getInputName())) {
                    inputNames.add(inputParameterDto.getInputName());
                }
            }
        }
        return inputNames;
    }

    private void addProblemInputParameter(List<ProblemInputParameterDto> problemInputParameterDto, Problem problem) {
        List<ProblemInputParameter> problemInputParameters = new ArrayList();
        for (int j = 0; j < problemInputParameterDto.size(); j++) {
            List<InputParameterDto> inputParameters = problemInputParameterDto.get(j).getParameters();
            log.info(inputParameters);
            for (int i = 0; i < inputParameters.size(); i++) {
                Language language = languageService.findByName(problemInputParameterDto.get(j).getLanguage());
                ProblemInputParameter problemInputParameter = new ProblemInputParameter();
                problemInputParameter.setProblem(problem);
                problemInputParameter.setLanguage(language);
                Map<String, String> inputMap = new HashMap<String, String>();
                inputMap.put("name", inputParameters.get(i).getInputName());
                inputMap.put("type", inputParameters.get(i).getInputType().toString());

                String parameterJson = gson.toJson(inputMap);
                log.info("Parameter: " + parameterJson);
                problemInputParameter.setParameters(parameterJson);
                problemInputParameters.add(problemInputParameter);
            }
        }
        problemInputParameterService.addListInputParameters(problemInputParameters);
    }

    public List<ProblemSolution> addProblemEditorial(ProblemEditorialDto problemEditorialDto, Problem problem) {
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
        addProblemEditorialCode(problemEditorialDto.getEditorialDtos(), problemSolution);

        return problemSolutions;
    }

    public void addProblemEditorialCode(EditorialDto editorialDto, ProblemSolution problemSolution) {
        List<SolutionCodeDto> solutionCodeDtos = editorialDto.getSolutionCodes();
        List<SolutionCode> solutionCodes = new ArrayList();
        for (int i = 0; i < solutionCodeDtos.size(); i++) {
            SolutionCode solutionCode = new SolutionCode();
            solutionCode.setCode(solutionCodeDtos.get(i).getSolutionCode());
            Language language = languageService.findByName(solutionCodeDtos.get(i).getSolutionLanguage());
            solutionCode.setLanguage(language);
            solutionCode.setProblem(problemSolution.getProblem());
            solutionCode.setId(new SolutionLanguageId(problemSolution.getId(), language.getId()));
            solutionCode.setSolution(problemSolution);
            solutionCodes.add(solutionCode);

        }
        solutionCodeService.saveAll(solutionCodes);
    }

    public void addProblemTestCase(ProblemTestCaseDto problemTestCaseDto,
            Problem problem,
            List<ProblemInputParameterDto> problemInputParameterDtos) {
        List<TestCaseDto> testCaseDtos = problemTestCaseDto.getTestCases();
        List<ProblemTestCase> problemTestCases = new ArrayList<>();
        for (int i = 0; i < testCaseDtos.size(); i++) {
            ProblemTestCase problemTestCase = new ProblemTestCase();
            problemTestCase
                    .setInput(generateInputJson(problemInputParameterDtos, problemTestCaseDto, i));
            if (problemInputParameterDtos.get(0).getReturnType() == InputType.STRING) {
                problemTestCase.setExpectedOutput("\"" + testCaseDtos.get(i).getExpectedOutput() + "\"");
            } else {
                problemTestCase.setExpectedOutput(testCaseDtos.get(i).getExpectedOutput());
            }
            problemTestCase.setProblem(problem);
            problemTestCase.setSample(testCaseDtos.get(i).getIsSample().booleanValue());
            problemTestCases.add(problemTestCase);
        }
        problemTestCaseService.saveListTestCase(problemTestCases);
    }

    private ProblemInputParameterDto generateTemplate(ProblemInputParameterDto problemInputParameterDto) {
        List<TemplateCode> templates = problemInputParameterDto.getTemplateCodes();
        StringBuilder templateBuilder;
        for (int i = 0; i < templates.size(); i++) {
            String template = "";
            templateBuilder = new StringBuilder();
            if (templates.get(i).getLanguage().equals("Java")) {
                templateBuilder.append(templates.get(i).getCode()).append("\n public static ");
                templateBuilder.append(getStringForInputType(problemInputParameterDto.getReturnType())).append(" ")
                        .append(problemInputParameterDto.getFunctionSignature()).append("(");
                List<InputParameterDto> inputs = problemInputParameterDto.getParameters();
                for (int j = 0; j < inputs.size(); j++) {
                    templateBuilder.append(getStringForInputType(inputs.get(j).getInputType())).append(" ")
                            .append(inputs.get(j).getInputName());
                    if (inputs.size() - j > 1) {
                        templateBuilder.append(", ");
                    }
                }
                templateBuilder.append(") {\n}");
                template = templateBuilder.toString();
                templates.get(i).setCode(template);
            } else if (templates.get(i).getLanguage().equals("C")) {
                templateBuilder.append(templates.get(i).getCode()).append("\n public ");
                templateBuilder.append(getCStringForInputType(problemInputParameterDto.getReturnType())).append(" ")
                        .append(problemInputParameterDto.getFunctionSignature()).append("(");
                List<InputParameterDto> inputs = problemInputParameterDto.getParameters();
                for (int j = 0; j < inputs.size(); j++) {
                    templateBuilder.append(getCStringForInputType(inputs.get(j).getInputType())).append(" ")
                            .append(inputs.get(j).getInputName());
                    if (inputs.size() - j > 1) {
                        templateBuilder.append(", ");
                    }
                }
                templateBuilder.append(") {\n}");
                template = templateBuilder.toString();
                templates.get(i).setCode(template);
            }
        }
        problemInputParameterDto.setTemplateCodes(templates);
        return problemInputParameterDto;
    }

    private String getCStringForInputType(InputType type) {
        switch (type) {
            case CHAR:
                return "char";
            case ARR_INT:
                return "int*"; // Mảng trong C được biểu diễn bằng con trỏ
            case ARR_DOUBLE:
                return "double*";
            case ARR_STRING:
                return "char**"; // Mảng chuỗi trong C là mảng của con trỏ char
            case ARR_OBJECT:
                return "void**"; // Mảng object không rõ kiểu trong C có thể dùng con trỏ void

            case MAP:
                return "HashMap*"; // Cần tự định nghĩa hoặc sử dụng thư viện ngoài
            case LIST:
                return "LinkedList*"; // Cần tự định nghĩa danh sách liên kết

            case INT:
                return "int";
            case LONG:
                return "long";
            case DOUBLE:
                return "double";
            case BOOLEAN:
                return "int"; // Trong C, không có kiểu boolean, thường dùng int (0: false, 1: true)
            case STRING:
                return "char*"; // Chuỗi trong C là con trỏ đến mảng ký tự
            case OBJECT:
                return "void*"; // Con trỏ void cho kiểu dữ liệu không xác định

            default:
                return "char*";
        }
    }

    private String getStringForInputType(InputType type) {
        switch (type) {
            case ARR_INT:
                return "int[]";
            case ARR_DOUBLE:
                return "double[]";

            case ARR_STRING:
                return "String[]";

            case ARR_OBJECT:
                return "Object[]";

            case MAP:
                return "Map";
            case LIST:
                return "List";

            case INT:
                return "int";
            case LONG:
                return "long";
            case DOUBLE:
                return "double";
            case BOOLEAN:
                return "boolean";
            case STRING:
                return "String";
            case OBJECT:
                return "Object";
            default:
                return "String";
        }
    }

    private boolean checkTestCase(
            ProblemEditorialDto problemEditorialDto,
            ProblemTestCaseDto problemTestCaseDto,
            List<ProblemInputParameterDto> problemInputParameterDtos) {
        inputs = new ArrayList();

        for (int k = 0; k < problemInputParameterDtos.size(); k++) {
            ProblemInputParameterDto problemInputParameterDto = problemInputParameterDtos.get(k);
            List<InputParameterDto> inputDtos = problemInputParameterDto.getParameters();
            List<TestCase> testCases = new ArrayList<>();
            List<TestCaseDto> testCaseDtos = problemTestCaseDto.getTestCases();
            for (int i = 0; i < testCaseDtos.size(); i++) {
                List<InputVariable> inputVariables = new ArrayList();
                Map<String, String> inputName = testCaseDtos.get(i).getInput();
                for (int j = 0; j < inputDtos.size(); j++) {
                    String rawValue = inputName.get(inputDtos.get(j).getInputName());
                    Object parsedValue = parseValue(rawValue, inputDtos.get(j).getInputType(), gson);

                    InputVariable input = new InputVariable(
                            inputDtos.get(j).getInputName(),
                            inputDtos.get(j).getInputType().toString(),
                            parsedValue);

                    inputVariables.add(input);
                }
                // log.info(inputs);
                TestCase testCase = new TestCase();
                testCase.setInput(inputVariables);
                inputs.add(inputVariables);
                if (problemInputParameterDto.getReturnType() == InputType.STRING) {
                    testCase.setExpectedOutput("\"" + testCaseDtos.get(i).getExpectedOutput() + "\"");
                } else {
                    testCase.setExpectedOutput(testCaseDtos.get(i).getExpectedOutput());
                }
                testCases.add(testCase);
            }

            List<SolutionCodeDto> solutionCodes = problemEditorialDto.getEditorialDtos().getSolutionCodes();
            for (int j = 0; j < solutionCodes.size(); j++) {
                LambdaRequest lambdaRequest = new LambdaRequest();
                lambdaRequest.setCode(solutionCodes.get(j).getSolutionCode());
                lambdaRequest.setLanguage(solutionCodes.get(j).getSolutionLanguage());
                lambdaRequest.setFunctionSignature(problemInputParameterDto.getFunctionSignature());
                lambdaRequest.setReturnType(problemInputParameterDto.getReturnType().toString());
                lambdaRequest.setTestCases(testCases);
                // log.info(lambdaRequest);

                try {
                    CompileService.compileAndRun(lambdaRequest.getCode(), testCases,
                            lambdaRequest.getLanguage(),
                            lambdaRequest.getFunctionSignature(),
                            lambdaRequest.getReturnType().toString());
                } catch (Exception e) {

                }

                String result = lambdaService.invokeLambdaFunction(lambdaRequest);
                String status = "";
                ResponseResult responseResult = new ResponseResult();
                submissionResponseDto = null;
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
                                solutionCodes.get(j).getSolutionCode(),
                                solutionCodes.get(j).getSolutionLanguage().toLowerCase(),
                                responseResult.getNoSuccessTestcase(),
                                Timestamp.from(Instant.now()),
                                SubmissionStatus.SUCCESS);
                        break;
                    case "FAILED":
                        submissionResponseDto = new FailedSubmissionResponseDto(
                                responseResult.getNoSuccessTestcase(),
                                testCases.size(),
                                responseResult.getInputWrong(),
                                solutionCodes.get(j).getSolutionCode(),
                                solutionCodes.get(j).getSolutionLanguage().toLowerCase(),
                                Timestamp.from(Instant.now()),
                                SubmissionStatus.FAILED);
                        return false;
                    default:
                        submissionResponseDto = new CompileErrorResposneDto(
                                status,
                                solutionCodes.get(j).getSolutionCode(),
                                solutionCodes.get(j).getSolutionLanguage().toLowerCase(),
                                Timestamp.from(Instant.now()),
                                SubmissionStatus.FAILED);
                        return false;
                }
            }

        }
        return true;
    }

    private String generateInputJson(List<ProblemInputParameterDto> problemInputParameterDto,
            ProblemTestCaseDto problemTestCaseDto,
            int index) {
        return gson.toJson(inputs.get(index));

    }

    public Object parseValue(String rawValue, InputType type, Gson gson) {
        if (rawValue == null || rawValue.equalsIgnoreCase("null")) {
            return null;
        }

        switch (type) {
            case ARR_INT:
                return gson.fromJson(rawValue, new TypeToken<List<Integer>>() {
                }.getType());
            case ARR_DOUBLE:
                return gson.fromJson(rawValue, new TypeToken<List<Double>>() {
                }.getType());
            case ARR_STRING:
                return gson.fromJson(rawValue, new TypeToken<List<String>>() {
                }.getType());
            case ARR_OBJECT:
                return gson.fromJson(rawValue, new TypeToken<List<Object>>() {
                }.getType());
            case MAP:
                return gson.fromJson(rawValue, new TypeToken<Map<String, Object>>() {
                }.getType());
            case INT:
                return Integer.parseInt(rawValue);
            case LONG:
                return Long.parseLong(rawValue);
            case DOUBLE:
                return Double.parseDouble(rawValue);
            case BOOLEAN:
                return Boolean.parseBoolean(rawValue);
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
        List<String> inputNames = getInputName(problemInputParameterDto);
        ProblemTestCaseDto problemTestCaseDto = excelService.readTestCaseExcel(excelFile, inputNames);
        if (!checkTestCase(problemEditorialDto, problemTestCaseDto, problemInputParameterDto)) {
            throw new TestCaseNotPassedException("Your solution code doesn't pass all test case. Please check again",
                    submissionResponseDto);
        }
        deleteProblemDependency(problem);
        editProblemBasic(problemBasicAddDto, problem);
        addProblemInputParameter(problemInputParameterDto, problem);
        addProblemTemplate(problemInputParameterDto, problem);
        addProblemEditorial(problemEditorialDto, problem);
        addProblemTestCase(problemTestCaseDto, problem, problemInputParameterDto);
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
        List<EditorialResponseDto> responseDtoList = new ArrayList();
        ProblemEditorialResponseDto problemEditorialResponseDto = new ProblemEditorialResponseDto();
        List<ProblemSolution> problemSolutions = problemSolutionService.findEditorialByProblem(problem);
        for (ProblemSolution problemSolution : problemSolutions) {
            EditorialResponseDto editorialResponseDto = new EditorialResponseDto();
            List<String> skills = new ArrayList<>();
            for (Skill skill : problemSolution.getSkills()) {
                skills.add(skill.getName());
            }
            editorialResponseDto.setEditorialSkills(skills);
            editorialResponseDto.setEditorialTitle(problemSolution.getTitle());
            editorialResponseDto.setEditorialTextSolution(problemSolution.getTextSolution());
            List<SolutionCodeDto> solutionCodeDtos = solutionCodeService.findBySolution(problemSolution);
            editorialResponseDto.setSolutionCodes(solutionCodeDtos);
            responseDtoList.add(editorialResponseDto);
        }
        problemEditorialResponseDto.setEditorialDtos(responseDtoList);
        return problemEditorialResponseDto;
    }

    @Override
    public ProblemInputParameterResponseDto getProblemInputParameterDtoList(String link) {
        Problem problem = getActivePublicProblemByLink(link);
        List<ProblemInputParameter> problemInputParameters = problemInputParameterService
                .getProblemInputParameters(problem);
        List<ProblemTemplate> problemTemplates = problemTemplateService.getAllTemplatesByProblem(problem);

        ProblemInputParameterResponseDto problemInputParameterResponseDto = new ProblemInputParameterResponseDto();
        if (problemTemplates.isEmpty()) {
            throw new BadRequestException("This problem doesn't has template. Please input it",
                    "This problem doesn't has template. Please input it");
        }
        problemInputParameterResponseDto.setFunctionSignature(problemTemplates.get(0).getFunctionSignature());
        problemInputParameterResponseDto.setReturnType(problemTemplates.get(0).getReturnType());
        List<TemplateCode> templateCodes = new ArrayList();
        List<InputParameterDto> inputParameterDtoList = new ArrayList();

        for (int i = 0; i < problemInputParameters.size(); i++) {
            InputParameterDto inputParameterDto = new InputParameterDto();
            List<InputVariable> inputVariables = gson.fromJson(problemInputParameters.get(i).getParameters(),
                    new TypeToken<List<InputVariable>>() {
                    }.getType());
            for (int j = 0; j < inputVariables.size(); j++) {
                inputParameterDto.setInputName(inputVariables.get(j).getName());
                inputParameterDto.setInputType(InputType.valueOf(inputVariables.get(j).getType()));
                inputParameterDtoList.add(inputParameterDto);
            }

        }

        for (int i = 0; i < problemTemplates.size(); i++) {
            TemplateCode templateCode = new TemplateCode();
            templateCode.setCode(problemTemplates.get(i).getTemplateCode());
            templateCode.setLanguage(problemTemplates.get(i).getLanguage().getName());
            templateCodes.add(templateCode);
        }

        problemInputParameterResponseDto.setParameters(inputParameterDtoList);
        problemInputParameterResponseDto.setTemplateCodes(templateCodes);
        return problemInputParameterResponseDto;
    }

    @Override
    public byte[] getExcelFile(String link) {
        Problem problem = getProblemByLink(link);
        List<ProblemTestCase> problemTestCases = problemTestCaseService.getProblemTestCaseByProblem(problem);
        return excelService.generateExcelFile(problemTestCases);
    }

    @Override
    public Page<SolutionListResponseDto> getProblemListSolution(String link, int page, Integer size, String title,
            String languageName,
            String sortBy, Boolean ascending, Pageable pageable) {
        Problem problem = getActivePublicProblemByLink(link);
        if (languageName != null && languageName != "") {
            Language language = languageService.findByName(languageName);
            return problemSolutionService.findOtherSolutionByProblem(problem, page, size, title, language, sortBy,
                    ascending,
                    pageable);
        } else {
            return problemSolutionService.findOtherSolutionByProblem(problem, page, size, title, null, sortBy,
                    ascending,
                    pageable);
        }
    }

    @Override
    public ProblemSolutionDto getProblemSolutionDetail(Long solutionId) {
        ProblemSolution problemSolution = problemSolutionService.findSolutionById(solutionId);
        ProblemSolutionDto problemSolutionDto = new ProblemSolutionDto();
        List<String> skills = new ArrayList<>();
        for (Skill skill : problemSolution.getSkills()) {
            skills.add(skill.getName());
        }
        problemSolutionDto.setId(solutionId);
        problemSolutionDto.setSkills(skills);
        problemSolutionDto.setProblem(problemResponseMapper.mapFrom(problemSolution.getProblem()));
        problemSolutionDto.setTitle(problemSolution.getTitle());
        problemSolutionDto.setTextSolution(problemSolution.getTextSolution());
        List<SolutionCodeDto> solutionCodeDtos = solutionCodeService.findBySolution(problemSolution);
        problemSolutionDto.setSolutionCodes(solutionCodeDtos);
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
        for (Users user : userFavourite) {
            if (user.getEmail().equals(currentUser.getEmail())) {
                userFavourite.remove(currentUser);
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
    public void postSolution(List<ShareSolutionRequestDto> shareSolutionRequestDto) {
        Users user = userService.getCurrentUser();
        for (ShareSolutionRequestDto shareSolution : shareSolutionRequestDto) {
            List<ProblemSubmission> problemSubmissions = new ArrayList<>();
            Problem problem = getActivePublicProblemByLink(shareSolution.getLink());
            shareSolution.setProblem(problem);
            List<Long> submissionIds = shareSolution.getSubmissionId();
            for (int i = 0; i < submissionIds.size(); i++) {
                problemSubmissions.add(problemSubmissionService.getProblemSubmissionById(submissionIds.get(i)));
            }
            shareSolution.setSubmissions(problemSubmissions);
        }
        problemSolutionService.postSolution(shareSolutionRequestDto, user);
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

}
