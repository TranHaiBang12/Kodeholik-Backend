package com.g44.kodeholik.service.problem.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemInputParameter;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.repository.problem.ProblemInputParameterRepository;
import com.g44.kodeholik.service.problem.ProblemInputParameterService;
import com.g44.kodeholik.service.problem.ProblemService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProblemInputParameterServiceImpl implements ProblemInputParameterService {
    private final ProblemInputParameterRepository problemInputParameterRepository;

    @Override
    public List<ProblemInputParameter> getProblemInputParameters(Problem problem) {
        return problemInputParameterRepository.findByProblem(problem);
    }

    @Override
    public List<ProblemInputParameter> getProblemInputParameters(Problem problem, Language language) {
        return problemInputParameterRepository.findByProblemAndLanguage(problem, language);
    }

    @Override
    public void addListInputParameters(List<ProblemInputParameter> listInputParameters) {
        problemInputParameterRepository.saveAll(listInputParameters);
    }

    @Transactional
    @Override
    public void deleteProblemInputParameters(Problem problem) {
        problemInputParameterRepository.deleteAllByProblem(problem);
    }

}
