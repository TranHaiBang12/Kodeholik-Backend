package com.g44.kodeholik.service.problem.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.g44.kodeholik.model.dto.request.problem.add.SolutionCodeDto;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSolution;
import com.g44.kodeholik.model.entity.problem.SolutionCode;
import com.g44.kodeholik.repository.problem.SolutionCodeRepository;
import com.g44.kodeholik.util.mapper.response.problem.SolutionCodeMapper;

public class SolutionCodeServiceImplTest {

    @InjectMocks
    private SolutionCodeServiceImpl solutionCodeService;

    @Mock
    private SolutionCodeRepository solutionCodeRepository;

    @Mock
    private SolutionCodeMapper solutionCodeMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveAll() {
        List<SolutionCode> solutionCodes = Arrays.asList(new SolutionCode(), new SolutionCode());
        solutionCodeService.saveAll(solutionCodes);
        verify(solutionCodeRepository).saveAll(solutionCodes);
    }

    @Test
    public void testSave() {
        SolutionCode solutionCode = new SolutionCode();
        solutionCodeService.save(solutionCode);
        verify(solutionCodeRepository).save(solutionCode);
    }

    @Test
    public void testGetSolutionCodesByProblem() {
        Problem problem = new Problem();
        List<SolutionCode> expectedSolutionCodes = Arrays.asList(new SolutionCode(), new SolutionCode());
        when(solutionCodeRepository.findByProblem(problem)).thenReturn(expectedSolutionCodes);

        List<SolutionCode> actualSolutionCodes = solutionCodeService.getSolutionCodesByProblem(problem);
        assertEquals(expectedSolutionCodes, actualSolutionCodes);
    }

    @Test
    public void testDeleteAllEditorialCodeByProblem() {
        Problem problem = new Problem();
        solutionCodeService.deleteAllEditorialCodeByProblem(problem);
        verify(solutionCodeRepository).deleteAllEditorialCodeByProblem(problem);
    }

    @Test
    public void testFindBySolution() {
        ProblemSolution problemSolution = new ProblemSolution();
        List<SolutionCode> solutionCodes = Arrays.asList(new SolutionCode(), new SolutionCode());
        List<SolutionCodeDto> expectedSolutionCodeDtos = Arrays.asList(new SolutionCodeDto(), new SolutionCodeDto());

        when(solutionCodeRepository.findBySolution(problemSolution)).thenReturn(solutionCodes);
        when(solutionCodeMapper.mapFrom(solutionCodes.get(0))).thenReturn(expectedSolutionCodeDtos.get(0));
        when(solutionCodeMapper.mapFrom(solutionCodes.get(1))).thenReturn(expectedSolutionCodeDtos.get(1));

        List<SolutionCodeDto> actualSolutionCodeDtos = solutionCodeService.findBySolution(problemSolution);
        assertEquals(expectedSolutionCodeDtos, actualSolutionCodeDtos);
    }
}