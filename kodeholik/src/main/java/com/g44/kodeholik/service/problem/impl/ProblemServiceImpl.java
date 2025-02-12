package com.g44.kodeholik.service.problem.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.lambda.TestCase;
import com.g44.kodeholik.model.dto.request.problem.ProblemCompileRequestDto;
import com.g44.kodeholik.model.dto.request.problem.ProblemRequestDto;
import com.g44.kodeholik.model.dto.request.problem.search.ProblemSortField;
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
import com.g44.kodeholik.model.entity.setting.Skill;
import com.g44.kodeholik.model.entity.setting.Topic;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.problem.Difficulty;
import com.g44.kodeholik.repository.elasticsearch.ProblemElasticsearchRepository;
import com.g44.kodeholik.repository.problem.ProblemRepository;
import com.g44.kodeholik.repository.problem.ProblemSubmissionRepository;
import com.g44.kodeholik.repository.user.UserRepository;
import com.g44.kodeholik.service.problem.ProblemService;
import com.g44.kodeholik.service.problem.ProblemSubmissionService;
import com.g44.kodeholik.service.problem.ProblemTemplateService;
import com.g44.kodeholik.service.problem.ProblemTestCaseService;
import com.g44.kodeholik.service.setting.TagService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.mapper.request.problem.ProblemRequestMapper;
import com.g44.kodeholik.util.mapper.response.problem.ProblemDescriptionMapper;
import com.g44.kodeholik.util.mapper.response.problem.ProblemResponseMapper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.security.User;
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

    private final UserService userService;

    @Scheduled(fixedRate = 600000)
    public void syncProblemsToElasticsearch() {
        List<ProblemElasticsearch> problems = problemRepository.findAll().stream()
                .map(problem -> ProblemElasticsearch.builder()
                        .id(problem.getId())
                        .title(problem.getTitle())
                        .titleSearchAndSort(problem.getTitle())
                        .difficulty(problem.getDifficulty().toString())
                        .acceptanceRate(problem.getAcceptanceRate())
                        .noSubmission(problem.getNoSubmission())
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
    public ProblemResponseDto getProblemResponseDtoById(Long id) {
        Problem problem = problemRepository.findById(id)
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
    public ProblemResponseDto updateProblem(Long id, ProblemRequestDto problemRequest) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Problem not found", "Problem not found"));
        modelMapper.map(problemRequest, problem);
        problem.setUpdatedAt(Timestamp.from(Instant.now()));
        problem.setUpdatedBy(userRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("User not found", "User not found")));
        problem = problemRepository.save(problem);
        return problemResponseMapper.mapFrom(problem);
    }

    @Override
    public void deleteProblem(Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Problem not found", "Problem not found"));
        problemRepository.delete(problem);
    }

    @Override
    public ProblemDescriptionResponseDto getProblemDescriptionById(Long id) {
        ProblemDescriptionResponseDto problemDescriptionResponseDto = new ProblemDescriptionResponseDto();
        Problem problem = getProblemById(id);
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
        syncProblemsToElasticsearch();
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
                                            return m.match(t -> t
                                                    .field("title")
                                                    .query(searchProblemRequestDto.getTitle())
                                                    .fuzziness("AUTO"));
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
                                                .prefix(p -> p
                                                        .field("titleSearchAndSort")
                                                        .value(searchText) // Từ tìm kiếm từ người dùng
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
    public SubmissionResponseDto submitProblem(Long problemId, ProblemCompileRequestDto problemCompileRequestDto) {
        Problem problem = getProblemById(problemId);
        return problemSubmissionService.submitProblem(problem, problemCompileRequestDto,
                getTestCaseByProblem(problemId),
                findByProblemAndLanguage(problemId, problemCompileRequestDto.getLanguageName()));
    }

    @Override
    public RunProblemResponseDto run(Long problemId, ProblemCompileRequestDto problemCompileRequestDto) {
        Problem problem = getProblemById(problemId);
        return problemSubmissionService.run(problem, problemCompileRequestDto, getSampleTestCaseByProblem(problemId),
                findByProblemAndLanguage(problemId, problemCompileRequestDto.getLanguageName()));
    }

    @Override
    public ProblemTemplate findByProblemAndLanguage(Long problemId, String languageName) {
        Problem problem = getProblemById(problemId);
        return problemTemplateService.findByProblemAndLanguage(problem, languageName);
    }

    @Override
    public List<TestCase> getTestCaseByProblem(Long problemId) {
        Problem problem = getProblemById(problemId);
        return problemTestCaseService.getTestCaseByProblem(problem);
    }

    @Override
    public List<TestCase> getSampleTestCaseByProblem(Long problemId) {
        Problem problem = getProblemById(problemId);
        return problemTestCaseService.getSampleTestCaseByProblem(problem);

    }

    @Override
    public ProblemCompileResponseDto getProblemCompileInformationById(Long problemId, String languageName) {
        Problem problem = getProblemById(problemId);
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
        log.info(difficulty + " " + solvedProblems);
        return new NoAchivedInformationResponseDto(difficulty.toString(), solvedProblems, problems.size());
    }

}
