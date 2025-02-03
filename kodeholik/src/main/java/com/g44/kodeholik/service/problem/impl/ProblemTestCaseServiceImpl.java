package com.g44.kodeholik.service.problem.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g44.kodeholik.model.dto.request.lambda.InputVariable;
import com.g44.kodeholik.model.dto.request.lambda.TestCase;
import com.g44.kodeholik.model.dto.response.problem.ProblemCompileResponseDto;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemTestCase;
import com.g44.kodeholik.repository.problem.ProblemTestCaseRepository;
import com.g44.kodeholik.service.problem.ProblemService;
import com.g44.kodeholik.service.problem.ProblemTemplateService;
import com.g44.kodeholik.service.problem.ProblemTestCaseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class ProblemTestCaseServiceImpl implements ProblemTestCaseService {
    private final ProblemTestCaseRepository problemTestCaseRepository;
    private final ProblemService problemService;
    private final ProblemTemplateService problemTemplateService;
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ProblemCompileResponseDto getProblemCompileInformationById(Long problemId, String languageName) {
        ProblemCompileResponseDto problemCompileResponseDto = new ProblemCompileResponseDto();
        Problem problem = problemService.getProblemById(problemId);
        problemCompileResponseDto
                .setTemplate(problemTemplateService.findByProblemAndLanguage(problem, languageName).getTemplateCode());
        List<TestCase> testCases = getSampleTestCaseByProblemId(problemId);
        problemCompileResponseDto.setTestCases(testCases);
        return problemCompileResponseDto;
    }

    @Override
    public List<TestCase> getTestCaseByProblemId(Long problemId) {
        Problem problem = problemService.getProblemById(problemId);
        List<ProblemTestCase> problemTestCase = problemTestCaseRepository.findByProblem(problem);
        List<TestCase> testCases = new ArrayList<>();
        for (int i = 0; i < problemTestCase.size(); i++) {
            List<InputVariable> inputs = new ArrayList<>();
            try {
                inputs = objectMapper.readValue(
                        problemTestCase.get(i).getInput(),
                        new TypeReference<List<InputVariable>>() {
                        });
            } catch (Exception e) {
                log.info(e.getMessage());
            }
            testCases.add(new TestCase(inputs, problemTestCase.get(i).getExpectedOutput()));
        }
        return testCases;
    }

    @Override
    public List<TestCase> getSampleTestCaseByProblemId(Long problemId) {
        Problem problem = problemService.getProblemById(problemId);
        List<ProblemTestCase> problemTestCase = problemTestCaseRepository.findByProblemAndIsSample(problem, true);
        List<TestCase> testCases = new ArrayList<>();
        for (int i = 0; i < problemTestCase.size(); i++) {
            List<InputVariable> inputs = new ArrayList<>();
            try {
                inputs = objectMapper.readValue(
                        problemTestCase.get(i).getInput(),
                        new TypeReference<List<InputVariable>>() {
                        });
            } catch (Exception e) {
                log.info(e.getMessage());
            }
            testCases.add(new TestCase(inputs, problemTestCase.get(i).getExpectedOutput()));
        }
        return testCases;
    }

}
