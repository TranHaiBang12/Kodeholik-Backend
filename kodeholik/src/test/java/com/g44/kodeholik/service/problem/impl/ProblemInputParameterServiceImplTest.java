package com.g44.kodeholik.service.problem.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemInputParameter;
import com.g44.kodeholik.repository.problem.ProblemInputParameterRepository;

class ProblemInputParameterServiceImplTest {

    @Mock
    private ProblemInputParameterRepository problemInputParameterRepository;

    @InjectMocks
    private ProblemInputParameterServiceImpl problemInputParameterServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProblemInputParameters() {
        Problem problem = new Problem();
        List<ProblemInputParameter> expectedParameters = Arrays.asList(new ProblemInputParameter(),
                new ProblemInputParameter());
        when(problemInputParameterRepository.findByProblem(problem)).thenReturn(expectedParameters);

        List<ProblemInputParameter> actualParameters = problemInputParameterServiceImpl
                .getProblemInputParameters(problem);

        verify(problemInputParameterRepository).findByProblem(problem);
        assertEquals(expectedParameters, actualParameters);
    }

    @Test
    void testAddListInputParameters() {
        List<ProblemInputParameter> listInputParameters = Arrays.asList(new ProblemInputParameter(),
                new ProblemInputParameter());

        problemInputParameterServiceImpl.addListInputParameters(listInputParameters);

        verify(problemInputParameterRepository).saveAll(listInputParameters);
    }

    @Test
    void testDeleteProblemInputParameters() {
        Problem problem = new Problem();

        problemInputParameterServiceImpl.deleteProblemInputParameters(problem);

        verify(problemInputParameterRepository).deleteAllByProblem(problem);
    }
}