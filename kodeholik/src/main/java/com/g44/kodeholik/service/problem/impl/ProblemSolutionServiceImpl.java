package com.g44.kodeholik.service.problem.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.response.problem.solution.ProblemSolutionDto;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSolution;
import com.g44.kodeholik.repository.problem.ProblemSolutionRepository;
import com.g44.kodeholik.service.problem.ProblemSolutionService;
import com.g44.kodeholik.util.mapper.response.problem.ProblemSolutionMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProblemSolutionServiceImpl implements ProblemSolutionService {

    private final ProblemSolutionRepository problemSolutionRepository;

    private final ProblemSolutionMapper problemSolutionMapper;

    @Override
    public ProblemSolution save(ProblemSolution problemSolution) {
        return problemSolutionRepository.save(problemSolution);
    }

    @Override
    public List<ProblemSolution> saveAll(List<ProblemSolution> problemSolutions) {
        return problemSolutionRepository.saveAll(problemSolutions);
    }

    @Override
    public List<ProblemSolution> findEditorialByProblem(Problem problem) {
        return problemSolutionRepository.findByProblemAndIsProblemImplementation(problem, true);
    }

    @Transactional
    @Override
    public void deleteEditorialByProblem(Problem problem) {
        problemSolutionRepository.deleteAllByProblemAndIsProblemImplementation(problem, true);
    }

    @Override
    public Page<ProblemSolutionDto> findListSolutionByProblem(Problem problem, Pageable pageable) {
        Page<ProblemSolution> solution = problemSolutionRepository.findByProblem(problem, pageable);
        return solution.map(problemSolutionMapper::mapFrom);
    }

    @Override
    public ProblemSolutionDto findSolutionDtoById(Long id) {
        ProblemSolution solution = problemSolutionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Solution not found", "Solution not found"));
        return problemSolutionMapper.mapFrom(solution);
    }

    @Override
    public ProblemSolution findSolutionById(Long id) {
        ProblemSolution solution = problemSolutionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Solution not found", "Solution not found"));
        return solution;
    }

}
