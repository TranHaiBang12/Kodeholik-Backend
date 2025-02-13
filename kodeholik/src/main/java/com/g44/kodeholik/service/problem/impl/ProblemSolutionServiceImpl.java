package com.g44.kodeholik.service.problem.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSolution;
import com.g44.kodeholik.repository.problem.ProblemSolutionRepository;
import com.g44.kodeholik.service.problem.ProblemSolutionService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProblemSolutionServiceImpl implements ProblemSolutionService {

    private final ProblemSolutionRepository problemSolutionRepository;

    @Override
    public ProblemSolution save(ProblemSolution problemSolution) {
        return problemSolutionRepository.save(problemSolution);
    }

    @Override
    public List<ProblemSolution> saveAll(List<ProblemSolution> problemSolutions) {
        return problemSolutionRepository.saveAll(problemSolutions);
    }

    @Override
    public List<ProblemSolution> findByProblem(Problem problem) {
        return problemSolutionRepository.findByProblem(problem);
    }

    @Transactional
    @Override
    public void deleteByProblem(Problem problem) {
        problemSolutionRepository.deleteAllByProblem(problem);
    }

}
