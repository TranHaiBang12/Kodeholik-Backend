package com.g44.kodeholik.service.problem.impl;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.g44.kodeholik.repository.problem.ProblemRepository;
import com.g44.kodeholik.service.problem.ProblemService;
import com.g44.kodeholik.util.mapper.request.problem.ProblemRequestMapper;
import com.g44.kodeholik.util.mapper.response.problem.ProblemResponseMapper;

@ExtendWith(MockitoExtension.class)
public class ProblemServiceImplTest {

    @Mock
    private ProblemRepository problemRepository;

    @Mock
    private ProblemRequestMapper problemRequestMapper;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ProblemResponseMapper problemResponseMapper;

    @InjectMocks
    private ProblemServiceImpl underTest;

    @Test
    @Disabled
    void testCreateProblem() {

    }

    @Test
    @Disabled
    void testDeleteProblem() {

    }

    @Test
    void testGetAllProblems() {
        // when
        underTest.getAllProblems();

        // then
        verify(problemRepository).findAll();
    }

    @Test
    @Disabled
    void testGetProblemById() {

    }

    @Test
    @Disabled
    void testUpdateProblem() {

    }
}
