package com.g44.kodeholik.service.problem.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.SolutionCode;
import com.g44.kodeholik.repository.problem.SolutionCodeRepository;
import com.g44.kodeholik.service.problem.SolutionCodeService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SolutionCodeServiceImpl implements SolutionCodeService {

    private final SolutionCodeRepository solutionCodeRepository;

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
    public void deleteByProblem(Problem problem) {
        solutionCodeRepository.deleteAllByProblem(problem);
    }

}
