package com.g44.kodeholik.service.problem.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.exception.ForbiddenException;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.problem.add.ShareSolutionRequestDto;
import com.g44.kodeholik.model.dto.response.problem.solution.ProblemSolutionDto;
import com.g44.kodeholik.model.dto.response.problem.solution.SolutionListResponseDto;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSolution;
import com.g44.kodeholik.model.entity.problem.ProblemSubmission;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.model.entity.setting.Skill;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.repository.problem.ProblemSolutionRepository;
import com.g44.kodeholik.util.mapper.response.problem.ProblemSolutionMapper;
import com.g44.kodeholik.util.mapper.response.problem.SolutionListResponseMapper;

class ProblemSolutionServiceImplTest {

    @Mock
    private ProblemSolutionRepository problemSolutionRepository;

    @Mock
    private ProblemSolutionMapper problemSolutionMapper;

    @Mock
    private SolutionListResponseMapper solutionListResponseMapper;

    @InjectMocks
    private ProblemSolutionServiceImpl problemSolutionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        ProblemSolution problemSolution = new ProblemSolution();
        when(problemSolutionRepository.save(any(ProblemSolution.class))).thenReturn(problemSolution);

        ProblemSolution result = problemSolutionService.save(problemSolution);

        assertEquals(problemSolution, result);
        verify(problemSolutionRepository, times(1)).save(problemSolution);
    }

    @Test
    void testSaveAll() {
        List<ProblemSolution> problemSolutions = new ArrayList<>();
        when(problemSolutionRepository.saveAll(anyList())).thenReturn(problemSolutions);

        List<ProblemSolution> result = problemSolutionService.saveAll(problemSolutions);

        assertEquals(problemSolutions, result);
        verify(problemSolutionRepository, times(1)).saveAll(problemSolutions);
    }

    @Test
    void testFindEditorialByProblem() {
        Problem problem = new Problem();
        List<ProblemSolution> problemSolutions = new ArrayList<>();
        when(problemSolutionRepository.findByProblemAndIsProblemImplementation(problem, true))
                .thenReturn(problemSolutions);

        List<ProblemSolution> result = problemSolutionService.findEditorialByProblem(problem);

        assertEquals(problemSolutions, result);
        verify(problemSolutionRepository, times(1))
                .findByProblemAndIsProblemImplementation(problem, true);
    }

    @Test
    void testDeleteEditorialByProblem() {
        Problem problem = new Problem();

        problemSolutionService.deleteEditorialByProblem(problem);

        verify(problemSolutionRepository, times(1))
                .deleteAllByProblemAndIsProblemImplementation(problem, true);
    }

    @Test
    void testFindListSolutionByProblem() {
        Problem problem = new Problem();
        Pageable pageable = PageRequest.of(0, 10);
        List<ProblemSolution> problemSolutions = new ArrayList<>();
        Page<ProblemSolution> page = new PageImpl<>(problemSolutions);
        when(problemSolutionRepository.findByProblem(problem, pageable)).thenReturn(page);
        when(problemSolutionMapper.mapFrom(any(ProblemSolution.class))).thenReturn(new ProblemSolutionDto());

        Page<ProblemSolutionDto> result = problemSolutionService.findListSolutionByProblem(problem, pageable);

        assertEquals(page.getTotalElements(), result.getTotalElements());
        verify(problemSolutionRepository, times(1)).findByProblem(problem, pageable);
    }

    @Test
    void testFindSolutionDtoById() {
        ProblemSolution problemSolution = new ProblemSolution();
        when(problemSolutionRepository.findById(anyLong())).thenReturn(Optional.of(problemSolution));
        when(problemSolutionMapper.mapFrom(any(ProblemSolution.class))).thenReturn(new ProblemSolutionDto());

        ProblemSolutionDto result = problemSolutionService.findSolutionDtoById(1L);

        assertEquals(new ProblemSolutionDto(), result);
        verify(problemSolutionRepository, times(1)).findById(1L);
    }

    @Test
    void testFindSolutionDtoByIdNotFound() {
        when(problemSolutionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> problemSolutionService.findSolutionDtoById(1L));
    }

    @Test
    void testFindSolutionById() {
        ProblemSolution problemSolution = new ProblemSolution();
        when(problemSolutionRepository.findById(anyLong())).thenReturn(Optional.of(problemSolution));

        ProblemSolution result = problemSolutionService.findSolutionById(1L);

        assertEquals(problemSolution, result);
        verify(problemSolutionRepository, times(1)).findById(1L);
    }

    @Test
    void testFindSolutionByIdNotFound() {
        when(problemSolutionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> problemSolutionService.findSolutionById(1L));
    }

    @Test
    void testUpvoteSolution() {
        ProblemSolution problemSolution = new ProblemSolution();
        problemSolution.setUserVote(new HashSet<>());
        when(problemSolutionRepository.findById(anyLong())).thenReturn(Optional.of(problemSolution));

        Users user = new Users();
        user.setEmail("test@example.com");

        problemSolutionService.upvoteSolution(1L, user);

        assertEquals(1, problemSolution.getNoUpvote());
        verify(problemSolutionRepository, times(1)).save(problemSolution);
    }

    @Test
    void testUpvoteSolutionAlreadyVoted() {
        ProblemSolution problemSolution = new ProblemSolution();
        Users user = new Users();
        user.setEmail("test@example.com");
        Set<Users> usersVote = new HashSet<>();
        usersVote.add(user);
        problemSolution.setUserVote(usersVote);
        when(problemSolutionRepository.findById(anyLong())).thenReturn(Optional.of(problemSolution));

        assertThrows(BadRequestException.class, () -> problemSolutionService.upvoteSolution(1L, user));
    }

    @Test
    void testUnupvoteSolution() {
        ProblemSolution problemSolution = new ProblemSolution();
        Users user = new Users();
        user.setEmail("test@example.com");
        Set<Users> usersVote = new HashSet<>();
        usersVote.add(user);
        problemSolution.setUserVote(usersVote);
        problemSolution.setNoUpvote(1);
        when(problemSolutionRepository.findById(anyLong())).thenReturn(Optional.of(problemSolution));

        problemSolutionService.unupvoteSolution(1L, user);

        assertEquals(0, problemSolution.getNoUpvote());
        verify(problemSolutionRepository, times(1)).save(problemSolution);
    }

    @Test
    void testUnupvoteSolutionNotVoted() {
        ProblemSolution problemSolution = new ProblemSolution();
        problemSolution.setUserVote(new HashSet<>());
        when(problemSolutionRepository.findById(anyLong())).thenReturn(Optional.of(problemSolution));

        Users user = new Users();
        user.setEmail("test@example.com");

        assertThrows(BadRequestException.class, () -> problemSolutionService
                .unupvoteSolution(1L, user));
    }

    @Test
    void testPostSolutionSuccess() {

        Language language = new Language();
        language.setId(1L);

        ProblemSubmission problemSubmission = new ProblemSubmission();
        problemSubmission.setAccepted(true);
        problemSubmission.setLanguage(language);

        ShareSolutionRequestDto shareSolutionRequestDto = new ShareSolutionRequestDto();
        shareSolutionRequestDto.setLink("http://example.com");
        shareSolutionRequestDto.setSubmissionId(Collections.singletonList(1L));
        shareSolutionRequestDto.setProblem(new Problem());
        shareSolutionRequestDto.setTitle("Title");
        shareSolutionRequestDto.setTextSolution("Text Solution");
        shareSolutionRequestDto.setSubmissions(Collections.singletonList(problemSubmission));
        Users user = new Users();
        user.setEmail("test@example.com");

        problemSolutionService.postSolution(shareSolutionRequestDto, user);

        verify(problemSolutionRepository, times(1)).save(any(ProblemSolution.class));
    }

    @Test
    void testPostSolutionNotAcceptedSubmission() {

        Language language = new Language();
        language.setId(1L);

        ProblemSubmission problemSubmission = new ProblemSubmission();
        problemSubmission.setAccepted(false);
        problemSubmission.setLanguage(language);

        ShareSolutionRequestDto shareSolutionRequestDto = new ShareSolutionRequestDto();
        shareSolutionRequestDto.setLink("http://example.com");
        shareSolutionRequestDto.setSubmissionId(Collections.singletonList(1L));
        shareSolutionRequestDto.setProblem(new Problem());
        shareSolutionRequestDto.setTitle("Title");
        shareSolutionRequestDto.setTextSolution("Text Solution");
        shareSolutionRequestDto.setSubmissions(Collections.singletonList(problemSubmission));
        Users user = new Users();
        user.setEmail("test@example.com");

        BadRequestException badRequestException = assertThrows(
                BadRequestException.class,
                () -> problemSolutionService.postSolution(shareSolutionRequestDto, user));
        assertEquals("You can't post a solution that has not been accepted yet", badRequestException.getMessage());
        assertEquals("You can't post a solution that has not been accepted yet", badRequestException.getDetails());
    }

    @Test
    void testPostSolutionDuplicateCode() {

        Language language = new Language();
        language.setId(1L);

        ProblemSubmission problemSubmission = new ProblemSubmission();
        problemSubmission.setAccepted(true);
        problemSubmission.setLanguage(language);
        problemSubmission.setCode("test");

        List<Long> submissionIdsList = new ArrayList<Long>();
        submissionIdsList.add(1L);
        submissionIdsList.add(1L);

        List<ProblemSubmission> submissionsList = new ArrayList<ProblemSubmission>();
        submissionsList.add(problemSubmission);
        submissionsList.add(problemSubmission);

        ShareSolutionRequestDto shareSolutionRequestDto = new ShareSolutionRequestDto();
        shareSolutionRequestDto.setLink("http://example.com");
        shareSolutionRequestDto.setSubmissionId(submissionIdsList);
        shareSolutionRequestDto.setProblem(new Problem());
        shareSolutionRequestDto.setTitle("Title");
        shareSolutionRequestDto.setTextSolution("Text Solution");
        shareSolutionRequestDto.setSubmissions(submissionsList);
        shareSolutionRequestDto.setSubmissionId(submissionIdsList);

        Users user = new Users();
        user.setEmail("test@example.com");

        BadRequestException badRequestException = assertThrows(
                BadRequestException.class,
                () -> problemSolutionService.postSolution(shareSolutionRequestDto, user));
        assertEquals("There are duplicate code in the solution you trying to post. Please try again",
                badRequestException.getMessage());
        assertEquals("There are duplicate code in the solution you trying to post. Please try again",
                badRequestException.getDetails());
    }

    @Test
    void testPostSolutionLinkNull() {
        ShareSolutionRequestDto shareSolutionRequestDto = new ShareSolutionRequestDto();
        shareSolutionRequestDto.setLink(null);

        Users user = new Users();
        user.setEmail("test@example.com");

        assertThrows(BadRequestException.class,
                () -> problemSolutionService.postSolution(shareSolutionRequestDto, user));
    }

    @Test
    void testPostSolutionSubmissionIdEmpty() {
        ShareSolutionRequestDto shareSolutionRequestDto = new ShareSolutionRequestDto();
        shareSolutionRequestDto.setLink("http://example.com");
        shareSolutionRequestDto.setSubmissionId(Collections.emptyList());

        Users user = new Users();
        user.setEmail("test@example.com");

        assertThrows(BadRequestException.class,
                () -> problemSolutionService.postSolution(shareSolutionRequestDto, user));
    }

    @Test
    void testEditSolution() {
        Language language = new Language();
        language.setId(1L);

        ProblemSubmission problemSubmission = new ProblemSubmission();
        problemSubmission.setAccepted(true);
        problemSubmission.setLanguage(language);
        problemSubmission.setCode("test");

        ShareSolutionRequestDto shareSolutionRequestDto = new ShareSolutionRequestDto();
        shareSolutionRequestDto.setSubmissionId(Collections.singletonList(1L));
        shareSolutionRequestDto.setTitle("Title");
        shareSolutionRequestDto.setTextSolution("Text Solution");
        shareSolutionRequestDto.setSubmissions(Collections.singletonList(problemSubmission));

        Users user = new Users();
        user.setEmail("test@example.com");

        ProblemSolution problemSolution = new ProblemSolution();
        problemSolution.setCreatedBy(user);
        problemSolution.setSolutionCodes(new HashSet<>());

        when(problemSolutionRepository.findById(anyLong())).thenReturn(Optional.of(problemSolution));

        problemSolutionService.editSolution(shareSolutionRequestDto, user, 1L, new HashSet<>());

        verify(problemSolutionRepository, times(1)).save(any(ProblemSolution.class));
    }

    @Test
    void testEditSolutionNotOwner() {
        ShareSolutionRequestDto shareSolutionRequestDto = new ShareSolutionRequestDto();
        shareSolutionRequestDto.setSubmissionId(Collections.singletonList(1L));

        Users user = new Users();
        user.setEmail("test@example.com");

        ProblemSolution problemSolution = new ProblemSolution();
        Users anotherUser = new Users();
        anotherUser.setEmail("another@example.com");
        problemSolution.setCreatedBy(anotherUser);
        when(problemSolutionRepository.findById(anyLong())).thenReturn(Optional.of(problemSolution));

        assertThrows(ForbiddenException.class,
                () -> problemSolutionService.editSolution(shareSolutionRequestDto, user, 1L, new HashSet<>()));
    }

    @Test
    void testEditSolutionSubmissionIdEmpty() {
        ShareSolutionRequestDto shareSolutionRequestDto = new ShareSolutionRequestDto();
        shareSolutionRequestDto.setSubmissionId(Collections.emptyList());

        Users user = new Users();
        user.setEmail("test@example.com");

        ProblemSolution problemSolution = new ProblemSolution();
        problemSolution.setCreatedBy(user);
        when(problemSolutionRepository.findById(anyLong())).thenReturn(Optional.of(problemSolution));

        assertThrows(BadRequestException.class,
                () -> problemSolutionService.editSolution(shareSolutionRequestDto, user, 1L, new HashSet<>()));
    }

    @Test
    void testFindOtherSolutionByProblemFilterSkillSuccess() {
        String title = "test";
        int page = 0;

        Language language = new Language();
        language.setId(1L);

        Users user = new Users();
        user.setId(1L);

        Problem problem = new Problem();
        problem.setTitle(title);

        Skill skill = new Skill();
        skill.setId(1L);
        Set<Skill> skillList = Set.of(skill);

        when(problemSolutionRepository
                .findByProblemAndIsProblemImplementationAndTitleContainAndSkillsIn(
                        any(Problem.class),
                        anyString(),
                        any(Language.class),
                        anyBoolean(),
                        any(Set.class),
                        any(Pageable.class)))
                .thenReturn(Page.empty());

        Page<SolutionListResponseDto> result = problemSolutionService
                .findOtherSolutionByProblem(
                        problem,
                        page,
                        null,
                        title,
                        skillList,
                        language,
                        "DATE",
                        true,
                        PageRequest.of(page, 10), user);
        assertNotNull(result);
        verify(problemSolutionRepository, times(1))
                .findByProblemAndIsProblemImplementationAndTitleContainAndSkillsIn(
                        any(Problem.class),
                        anyString(),
                        any(Language.class),
                        anyBoolean(),
                        any(Set.class),
                        any(Pageable.class));
    }

    @Test
    void testFindOtherSolutionByProblemFilterSkillWrongSortField() {

        String title = "test";
        int page = 0;

        Language language = new Language();
        language.setId(1L);

        Users user = new Users();
        user.setId(1L);

        Problem problem = new Problem();
        problem.setTitle(title);

        Skill skill = new Skill();
        skill.setId(1L);
        Set<Skill> skillList = Set.of(skill);

        when(problemSolutionRepository
                .findByProblemAndIsProblemImplementationAndTitleContainAndSkillsIn(
                        any(Problem.class),
                        anyString(),
                        any(Language.class),
                        anyBoolean(),
                        any(Set.class),
                        any(Pageable.class)))
                .thenReturn(Page.empty());

        BadRequestException badRequestException = assertThrows(
                BadRequestException.class,
                () -> problemSolutionService
                        .findOtherSolutionByProblem(
                                problem,
                                page,
                                null,
                                title,
                                skillList,
                                language,
                                "",
                                true,
                                PageRequest.of(page, 10), user));
        assertEquals("Sort field is wrong",
                badRequestException.getMessage());
        assertEquals("Sort field is wrong",
                badRequestException.getDetails());

    }

    @Test
    void testFindOtherSolutionByProblemNotFilterSkillSuccess() {
        String title = "test";
        int page = 0;

        Language language = new Language();
        language.setId(1L);

        Users user = new Users();
        user.setId(1L);

        Problem problem = new Problem();
        problem.setTitle(title);

        when(problemSolutionRepository
                .findByProblemAndIsProblemImplementationAndTitleContain(
                        any(Problem.class),
                        anyString(),
                        any(Language.class),
                        anyBoolean(),
                        any(Pageable.class)))
                .thenReturn(Page.empty());

        Page<SolutionListResponseDto> result = problemSolutionService
                .findOtherSolutionByProblem(
                        problem,
                        page,
                        null,
                        title,
                        null,
                        language,
                        "DATE",
                        true,
                        PageRequest.of(page, 10), user);
        assertNotNull(result);
        verify(problemSolutionRepository, times(1))
                .findByProblemAndIsProblemImplementationAndTitleContain(
                        any(Problem.class),
                        anyString(),
                        any(Language.class),
                        anyBoolean(),
                        any(Pageable.class));
    }
}