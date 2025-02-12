package com.g44.kodeholik.service.problem.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.response.problem.NoAchivedInformationResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemDescriptionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemResponseDto;
import com.g44.kodeholik.model.elasticsearch.ProblemElasticsearch;
import com.g44.kodeholik.model.entity.discussion.Comment;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.problem.Difficulty;
import com.g44.kodeholik.repository.elasticsearch.ProblemElasticsearchRepository;
import com.g44.kodeholik.repository.problem.ProblemRepository;
import com.g44.kodeholik.repository.problem.ProblemSubmissionRepository;
import com.g44.kodeholik.repository.user.UserRepository;
import com.g44.kodeholik.service.problem.ProblemSubmissionService;
import com.g44.kodeholik.service.problem.ProblemTemplateService;
import com.g44.kodeholik.service.problem.ProblemTestCaseService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.mapper.request.problem.ProblemRequestMapper;
import com.g44.kodeholik.util.mapper.response.problem.ProblemDescriptionMapper;
import com.g44.kodeholik.util.mapper.response.problem.ProblemResponseMapper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

@ExtendWith(MockitoExtension.class)
public class ProblemServiceImplTest {

    @Mock
    private ProblemRepository problemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProblemRequestMapper problemRequestMapper;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ProblemResponseMapper problemResponseMapper;

    @Mock
    private ProblemDescriptionMapper problemDescriptionMapper;

    @Mock
    private ProblemSubmissionRepository problemSubmissionRepository;

    @Mock
    private ProblemSubmissionService problemSubmissionService;

    @Mock
    private ProblemElasticsearchRepository problemElasticsearchRepository;

    @Mock
    private ElasticsearchClient elasticsearchClient;

    @Mock
    private ProblemTemplateService problemTemplateService;

    @Mock
    private ProblemTestCaseService problemTestCaseService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ProblemServiceImpl underTest;

    @Test
    @DisplayName("Should sync problems to Elasticsearch successfully")
    void testSyncProblemsToElasticsearch() {
        // given
        Problem problem = new Problem();
        problem.setId(1L);
        problem.setTitle("Test");
        problem.setDifficulty(Difficulty.EASY);
        problem.setAcceptanceRate(0);
        problem.setNoSubmission(0);
        problem.setTopics(new HashSet());
        problem.setSkills(new HashSet());

        List<Problem> problems = Arrays.asList(problem);
        when(problemRepository.findAll()).thenReturn(problems);

        // when
        underTest.syncProblemsToElasticsearch();

        // then
        ArgumentCaptor<List<ProblemElasticsearch>> captor = ArgumentCaptor.forClass(List.class);
        verify(problemElasticsearchRepository).saveAll(captor.capture());

        List<ProblemElasticsearch> savedProblems = captor.getValue();
        assertNotNull(savedProblems);
        assertEquals(1, savedProblems.size());
        assertEquals("Test", savedProblems.get(0).getTitle());
    }

    @Test
    @DisplayName("Should get all problems successfully")
    void testGetAllProblems() {
        // given
        Problem problem = new Problem();
        problem.setId(1L);
        problem.setTitle("Test");

        List<Problem> problems = Arrays.asList(problem);
        when(problemRepository.findAll()).thenReturn(problems);

        ProblemResponseDto responseDto = new ProblemResponseDto();
        responseDto.setId(1L);
        responseDto.setTitle("Test");
        when(problemResponseMapper.mapFrom(any(Problem.class))).thenReturn(responseDto);

        // when
        List<ProblemResponseDto> result = underTest.getAllProblems();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test", result.get(0).getTitle());
    }

    @Test
    @DisplayName("Should get problem by ID successfully")
    void testGetProblemResponseDtoById() {
        // given
        Problem problem = new Problem();
        problem.setId(1L);
        problem.setTitle("Test");

        when(problemRepository.findById(1L)).thenReturn(Optional.of(problem));

        ProblemResponseDto responseDto = new ProblemResponseDto();
        responseDto.setId(1L);
        responseDto.setTitle("Test");
        when(problemResponseMapper.mapFrom(any(Problem.class))).thenReturn(responseDto);

        // when
        ProblemResponseDto result = underTest.getProblemResponseDtoById(1L);

        // then
        assertNotNull(result);
        assertEquals("Test", result.getTitle());
    }

    @Test
    @DisplayName("Should throw NotFoundException when problem not found by ID")
    void testGetProblemResponseDtoByIdNotFound() {
        // given
        when(problemRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> underTest.getProblemResponseDtoById(1L));

        assertEquals("Problem not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should delete problem successfully")
    void testDeleteProblemSuccess() {
        // given
        Problem existingProblem = new Problem();
        existingProblem.setId(1L);
        existingProblem.setTitle("Test");

        when(problemRepository.findById(1L)).thenReturn(Optional.of(existingProblem));

        // when
        underTest.deleteProblem(1L);

        // then
        verify(problemRepository).delete(existingProblem);
    }

    @Test
    @DisplayName("Should throw NotFoundException when problem not found during delete")
    void testDeleteProblemNotFound() {
        // given
        when(problemRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> underTest.deleteProblem(1L));

        assertEquals("Problem not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should get problem description by ID successfully")
    void testGetProblemDescriptionById() {
        // given
        long problemId = 1L;
        Problem problem = new Problem();
        problem.setId(problemId);

        Set<Comment> comments = new HashSet<>(Arrays.asList(new Comment(), new Comment(), new Comment()));
        problem.setComments(comments);

        ProblemDescriptionResponseDto dto = new ProblemDescriptionResponseDto();
        dto.setId(problemId);
        dto.setNoAccepted(5L);
        dto.setNoComment(comments.size());

        when(problemRepository.findById(problemId)).thenReturn(Optional.of(problem));
        when(problemDescriptionMapper.mapFrom(any(Problem.class))).thenReturn(dto);
        when(problemSubmissionService.countByIsAcceptedAndProblem(true, problem)).thenReturn(5L);

        // when
        ProblemDescriptionResponseDto responseDto = underTest.getProblemDescriptionById(problemId);

        // then
        assertNotNull(responseDto);
        assertEquals(problemId, responseDto.getId());
        assertEquals(comments.size(), responseDto.getNoComment());
        assertEquals(5L, responseDto.getNoAccepted());
    }

    @Test
    @DisplayName("Should get list of no achieved information by current user successfully")
    void testGetListNoAchievedInformationByCurrentUser() {
        // given
        Users user = new Users();
        user.setId(1L);

        Problem problem = new Problem();
        problem.setId(1L);
        problem.setDifficulty(Difficulty.EASY);

        List<Problem> problems = Arrays.asList(problem);
        when(userService.getCurrentUser()).thenReturn(user);
        when(problemRepository.findAll()).thenReturn(problems);
        when(problemRepository.findByDifficulty(Difficulty.EASY)).thenReturn(problems);
        when(problemSubmissionService.countByUserAndIsAcceptedAndProblemIn(user, true, problems)).thenReturn(1L);

        // when
        List<NoAchivedInformationResponseDto> result = underTest.getListNoAchievedInformationByCurrentUser();

        // then
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals("EASY", result.get(0).getName());
        assertEquals(1L, result.get(0).getNoAchived());
    }
}
