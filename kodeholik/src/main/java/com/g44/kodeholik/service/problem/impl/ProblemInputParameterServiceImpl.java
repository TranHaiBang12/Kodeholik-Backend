package com.g44.kodeholik.service.problem.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemInputParameter;
import com.g44.kodeholik.repository.problem.ProblemInputParameterRepository;
import com.g44.kodeholik.service.problem.ProblemInputParameterService;
import com.g44.kodeholik.service.problem.ProblemService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProblemInputParameterServiceImpl implements ProblemInputParameterService {
    private final ProblemInputParameterRepository problemInputParameterRepository;
    private final ProblemService problemService;

    @Override
    public List<ProblemInputParameter> getProblemInputParameters(Long problemId) {
        Problem problem = problemService.getProblemById(problemId);
        return problemInputParameterRepository.findByProblem(problem);
    }

}
