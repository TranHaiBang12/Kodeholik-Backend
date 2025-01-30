package com.g44.kodeholik.service.problem.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.response.problem.ProblemTestCaseResponseDto;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemTestCase;
import com.g44.kodeholik.repository.problem.ProblemRepository;
import com.g44.kodeholik.repository.problem.ProblemTestCaseRepository;
import com.g44.kodeholik.service.problem.ProblemTestCaseService;
import com.g44.kodeholik.util.mapper.response.problem.ProblemTestCaseResponseMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProblemTestCaseServiceImpl implements ProblemTestCaseService {
    private final ProblemTestCaseRepository problemTestCaseRepository;
    private final ProblemRepository problemRepository;
    private final ProblemTestCaseResponseMapper problemTestCaseResponseMapper;

    @Override
    public List<ProblemTestCaseResponseDto> getProblemSampleTestCaseById(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new NotFoundException("Problem not found", "Problem not found"));
        List<ProblemTestCase> problemTestCase = problemTestCaseRepository.findByProblemAndIsSample(problem, true);
        return problemTestCase.stream()
                .map(problemTestCaseResponseMapper::mapFrom)
                .collect(Collectors.toList());
    }

}
