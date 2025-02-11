package com.g44.kodeholik.service.problem.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.problem.ProblemRequestDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemDescriptionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemResponseDto;
import com.g44.kodeholik.model.entity.discussion.Comment;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.problem.Difficulty;
import com.g44.kodeholik.model.enums.problem.ProblemStatus;
import com.g44.kodeholik.repository.problem.ProblemRepository;
import com.g44.kodeholik.repository.problem.ProblemSubmissionRepository;
import com.g44.kodeholik.repository.user.UserRepository;
import com.g44.kodeholik.service.problem.ProblemService;
import com.g44.kodeholik.util.mapper.request.problem.ProblemRequestMapper;
import com.g44.kodeholik.util.mapper.response.problem.ProblemDescriptionMapper;
import com.g44.kodeholik.util.mapper.response.problem.ProblemResponseMapper;

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

    @InjectMocks
    private ProblemServiceImpl underTest;

    // test truong hop create problem thanh cong
    @Test
    @DisplayName("Should create new problem successfully")
    void testCanCreateProblemSuccess() {
        // given
        ProblemRequestDto problemRequest = new ProblemRequestDto();
        problemRequest.setTitle("Test");
        problemRequest.setDescription("Test");
        problemRequest.setDifficulty(Difficulty.EASY);
        problemRequest.setStatus(ProblemStatus.PRIVATE);

        Problem mappedProblem = new Problem();
        mappedProblem.setTitle("Test");
        mappedProblem.setDescription("Test");
        mappedProblem.setDifficulty(Difficulty.EASY);
        mappedProblem.setStatus(ProblemStatus.PRIVATE);

        Users mockUser = new Users();
        mockUser.setId(1L);
        mockUser.setUsername("TestUser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // mock gia lap viec bat cu cau lenh nao mapTo ma object thuoc ve class problem
        // request dto deu se tra ve mapped problem
        when(problemRequestMapper.mapTo(any(ProblemRequestDto.class))).thenReturn(mappedProblem);

        // mock gia lap bat cu cau lenh nao save problem deu se tra ve mapped problem
        when(problemRepository.save(any(Problem.class))).thenReturn(mappedProblem);

        // when
        underTest.createProblem(problemRequest);

        // then
        ArgumentCaptor<Problem> problemCaptor = ArgumentCaptor.forClass(Problem.class);
        verify(problemRepository).save(problemCaptor.capture());

        Problem problemSaved = problemCaptor.getValue();

        assertNotNull(problemSaved, "Problem should not be null");
        assertEquals("Test", problemSaved.getTitle());
        assertEquals("Test", problemSaved.getDescription());
        assertEquals(Difficulty.EASY, problemSaved.getDifficulty());
        assertEquals(ProblemStatus.PRIVATE, problemSaved.getStatus());
    }

    // test truong hop create problem nhung ma k tim thay id user
    @Test
    @DisplayName("Should create new problem failed because user not found")
    void testCreateProblemUserNotFound() {
        // given
        ProblemRequestDto problemRequest = new ProblemRequestDto();
        problemRequest.setTitle("Test");
        problemRequest.setDescription("Test");
        problemRequest.setDifficulty(Difficulty.EASY);
        problemRequest.setStatus(ProblemStatus.PRIVATE);

        Problem mappedProblem = new Problem();
        mappedProblem.setTitle("Test");
        mappedProblem.setDescription("Test");
        mappedProblem.setDifficulty(Difficulty.EASY);
        mappedProblem.setStatus(ProblemStatus.PRIVATE);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // mock gia lap viec bat cu cau lenh nao mapTo ma object thuoc ve class problem
        // request dto deu se tra ve mapped problem
        when(problemRequestMapper.mapTo(any(ProblemRequestDto.class))).thenReturn(mappedProblem);

        // then
        // mock gia lap test se tra ve exception not found
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> underTest.createProblem(problemRequest));

        assertEquals("User not found", exception.getMessage());

        // kbh goi ham save cua problem repository
        verify(problemRepository, never()).save(any());
    }

    // test truong hop xoa problem thanh cong
    @Test
    @DisplayName("Should delete problem successfully")
    void testDeleteProblemSuccess() {
        // given
        Problem mockProblem = new Problem();
        mockProblem.setId(1L);
        mockProblem.setTitle("Test");
        mockProblem.setDescription("Test");
        mockProblem.setDifficulty(Difficulty.EASY);
        mockProblem.setStatus(ProblemStatus.PRIVATE);

        when(problemRepository.findById(1L)).thenReturn(Optional.of(mockProblem));
        // when
        underTest.deleteProblem(mockProblem.getId());
        // then
        verify(problemRepository).delete(mockProblem);
    }

    // test truong hop xoa problem ma id problem k ton tai
    @Test
    @DisplayName("Should delete problem failed because problem not found")
    void testDeleteProblemButIdNotFound() {
        // given
        Problem mockProblem = new Problem();
        mockProblem.setId(1L);
        mockProblem.setTitle("Test");
        mockProblem.setDescription("Test");
        mockProblem.setDifficulty(Difficulty.EASY);
        mockProblem.setStatus(ProblemStatus.PRIVATE);

        when(problemRepository.findById(1L)).thenReturn(Optional.empty());
        // then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> underTest.deleteProblem(mockProblem.getId()));

        assertEquals("Problem not found", exception.getMessage());

        // kbh goi ham save cua problem repository
        verify(problemRepository, never()).save(any());
    }

    // test lay danh sach problem
    @Test
    @DisplayName("Should get a list of problem successfully")
    void testGetAllProblems() {
        // when
        underTest.getAllProblems();

        // then
        verify(problemRepository).findAll();
    }

    // test truong hop cap nhat problem thanh cong
    @Test
    @DisplayName("Should update problem successfully")
    void testUpdateProblemSuccess() {
        Problem mockProblem = new Problem();
        mockProblem.setId(1L);
        mockProblem.setTitle("Test");
        mockProblem.setDescription("Test");
        mockProblem.setDifficulty(Difficulty.EASY);
        mockProblem.setStatus(ProblemStatus.PRIVATE);

        ProblemRequestDto problemRequest = new ProblemRequestDto();
        problemRequest.setTitle("Test");
        problemRequest.setDescription("Test");
        problemRequest.setDifficulty(Difficulty.EASY);
        problemRequest.setStatus(ProblemStatus.PRIVATE);

        ProblemResponseDto problemResponseDto = new ProblemResponseDto();
        problemResponseDto.setId(1L);
        problemResponseDto.setTitle("Test");
        problemResponseDto.setDescription("Test");
        problemResponseDto.setDifficulty(Difficulty.EASY);
        problemResponseDto.setStatus(ProblemStatus.PRIVATE);

        Users mockUser = new Users();
        mockUser.setId(1L);
        mockUser.setUsername("TestUser");

        when(problemRepository.findById(mockProblem.getId())).thenReturn(Optional.of(mockProblem));
        when(problemRepository.save(any(Problem.class))).thenReturn(mockProblem);
        when(problemResponseMapper.mapFrom(any(Problem.class))).thenReturn(problemResponseDto);
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));

        // when
        ProblemResponseDto problemResponseDtoSaved = underTest.updateProblem(mockProblem.getId(), problemRequest);

        // then
        assertNotNull(problemResponseDtoSaved);
        assertEquals(problemResponseDto.getId(), problemResponseDtoSaved.getId());
        assertEquals(problemResponseDto.getTitle(), problemResponseDtoSaved.getTitle());
        assertEquals(problemResponseDto.getDescription(), problemResponseDtoSaved.getDescription());
        assertEquals(problemResponseDto.getDifficulty(), problemResponseDtoSaved.getDifficulty());
        assertEquals(problemResponseDto.getStatus(), problemResponseDtoSaved.getStatus());

        verify(problemRepository).findById(mockProblem.getId());
        verify(problemRepository).save(any(Problem.class));
        verify(problemResponseMapper).mapFrom(any(Problem.class));

        // kiem tra de chac chan k co mock nao dc goi ngoai nhg mock dc truyen` vao`
        verifyNoMoreInteractions(problemRepository, problemResponseMapper);

    }

    // test truong hop cap nhat problem ma id problem k ton tai
    @Test
    @DisplayName("Should update problem failed because problem not found")
    void testUpdateProblemProblemNotFound() {
        // given
        Problem mockProblem = new Problem();
        mockProblem.setId(1L);
        mockProblem.setTitle("Test");
        mockProblem.setDescription("Test");
        mockProblem.setDifficulty(Difficulty.EASY);
        mockProblem.setStatus(ProblemStatus.PRIVATE);

        when(problemRepository.findById(mockProblem.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> underTest.getProblemById(mockProblem.getId()));

        assertEquals("Problem not found", exception.getMessage());
        verifyNoMoreInteractions(problemRepository);
    }

    // test truong hop cap nhat problem ma id user k ton tai
    @Test
    @DisplayName("Should update problem failed because user not found")
    void testUpdateProblemUserNotFound() {
        // given
        Problem mockProblem = new Problem();
        mockProblem.setId(1L);
        mockProblem.setTitle("Test");
        mockProblem.setDescription("Test");
        mockProblem.setDifficulty(Difficulty.EASY);
        mockProblem.setStatus(ProblemStatus.PRIVATE);

        ProblemRequestDto problemRequest = new ProblemRequestDto();
        problemRequest.setTitle("Test");
        problemRequest.setDescription("Test");
        problemRequest.setDifficulty(Difficulty.EASY);
        problemRequest.setStatus(ProblemStatus.PRIVATE);

        ProblemResponseDto problemResponseDto = new ProblemResponseDto();
        problemResponseDto.setId(1L);
        problemResponseDto.setTitle("Test");
        problemResponseDto.setDescription("Test");
        problemResponseDto.setDifficulty(Difficulty.EASY);
        problemResponseDto.setStatus(ProblemStatus.PRIVATE);

        Users mockUser = new Users();
        mockUser.setId(1L);
        mockUser.setUsername("TestUser");

        when(problemRepository.findById(mockProblem.getId())).thenReturn(Optional.of(mockProblem));
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> underTest.updateProblem(mockProblem.getId(), problemRequest));

        assertEquals("User not found", exception.getMessage());
        verifyNoMoreInteractions(problemRepository);
    }

    @Test
    void testGetProblemDescriptionById() {
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
        when(problemSubmissionRepository.countByIsAcceptedAndProblem(true, problem)).thenReturn(5L);

        ProblemDescriptionResponseDto responseDto = underTest.getProblemDescriptionById(problemId);

        assertNotNull(responseDto);
        assertEquals(problemId, responseDto.getId());
        assertEquals(comments.size(), responseDto.getNoComment());
        assertEquals(5L, responseDto.getNoAccepted());

        verify(problemRepository).findById(problemId);
        verify(problemDescriptionMapper).mapFrom(problem);
        verify(problemSubmissionRepository).countByIsAcceptedAndProblem(true, problem);
    }
}
