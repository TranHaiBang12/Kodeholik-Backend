package com.g44.kodeholik.service.problem.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.g44.kodeholik.model.dto.request.problem.add.SolutionCodeDto;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSolution;
import com.g44.kodeholik.model.entity.problem.SolutionCode;
import com.g44.kodeholik.repository.problem.SolutionCodeRepository;
import com.g44.kodeholik.service.problem.SolutionCodeService;
import com.g44.kodeholik.util.mapper.response.problem.SolutionCodeMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SolutionCodeServiceImpl implements SolutionCodeService {

    private final SolutionCodeRepository solutionCodeRepository;

    private final SolutionCodeMapper solutionCodeMapper;

    @Override
    public void saveAll(List<SolutionCode> solutionCodes) {
        solutionCodeRepository.saveAll(solutionCodes);
    }

    @Override
    public void save(SolutionCode solutionCode) {
        solutionCodeRepository.save(solutionCode);
    }

    @Override
    public List<SolutionCode> getSolutionCodesByProblem(Problem problem) {
        return solutionCodeRepository.findByProblem(problem);
    }

    @Transactional
    @Override
    public void deleteAllEditorialCodeByProblem(Problem problem) {
        solutionCodeRepository.deleteAllEditorialCodeByProblem(problem);
    }

    @Override
    public List<SolutionCodeDto> findBySolution(ProblemSolution problemSolution) {
        List<SolutionCode> solutionCodes = solutionCodeRepository.findBySolution(problemSolution);
        return solutionCodes.stream()
                .map(solutionCodeMapper::mapFrom)
                .collect(Collectors.toList());
    }

}
