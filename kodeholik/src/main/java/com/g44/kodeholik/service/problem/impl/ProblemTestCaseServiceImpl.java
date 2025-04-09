package com.g44.kodeholik.service.problem.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g44.kodeholik.model.dto.request.lambda.InputVariable;
import com.g44.kodeholik.model.dto.request.lambda.TestCase;
import com.g44.kodeholik.model.dto.response.exam.student.ExamCompileInformationResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemCompileResponseDto;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemTestCase;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.repository.problem.ProblemTestCaseRepository;
import com.g44.kodeholik.service.problem.ProblemService;
import com.g44.kodeholik.service.problem.ProblemTemplateService;
import com.g44.kodeholik.service.problem.ProblemTestCaseService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class ProblemTestCaseServiceImpl implements ProblemTestCaseService {
    private final ProblemTestCaseRepository problemTestCaseRepository;
    private final ProblemTemplateService problemTemplateService;
    private final ObjectMapper objectMapper;

    @Override
    public ProblemCompileResponseDto getProblemCompileInformationByProblem(Problem problem, Language language) {
        ProblemCompileResponseDto problemCompileResponseDto = new ProblemCompileResponseDto();
        problemCompileResponseDto
                .setTemplate(
                        problemTemplateService.findByProblemAndLanguage(problem, language.getName()).getTemplateCode());
        List<TestCase> testCases = getSampleTestCaseByProblemWithFormat(problem, language);
        problemCompileResponseDto.setTestCases(testCases);

        List<String> importCommands = new ArrayList<>();
        if (language.getName().equals("Java")) {
            importCommands.add("import java.util.*;");
            importCommands.add("import java.math.*;");
            importCommands.add("import java.io.*;");
            importCommands.add("import static java.util.Map.entry;");
        } else if (language.getName().equals("C")) {
            importCommands.add("#include <stdio.h>");
            importCommands.add("#include <stdlib.h>");
            importCommands.add("#include <string.h>");
            importCommands.add("#include <stdbool.h>");
            importCommands.add("#include <ctype.h>");
            importCommands.add("#include <limits.h>");
            importCommands.add("#include <math.h>");
        }
        problemCompileResponseDto.setImportCommands(importCommands);
        return problemCompileResponseDto;
    }

    @Override
    public List<List<TestCase>> getTestCaseByProblem(Problem problem, List<Language> language) {
        List<List<TestCase>> listTestCase = new ArrayList();
        for (int j = 0; j < language.size(); j++) {
            List<ProblemTestCase> problemTestCase = problemTestCaseRepository.findByProblemAndLanguage(problem,
                    language.get(j));
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
            listTestCase.add(testCases);
        }
        return listTestCase;
    }

    @Override
    public List<List<TestCase>> getSampleTestCaseByProblem(Problem problem, List<Language> language) {
        List<List<TestCase>> listTestCase = new ArrayList();
        for (int j = 0; j < language.size(); j++) {
            List<ProblemTestCase> problemTestCase = problemTestCaseRepository
                    .findByProblemAndIsSampleAndLanguage(problem, true, language.get(j));
            List<TestCase> testCases = new ArrayList<>();
            for (int i = 0; i < problemTestCase.size(); i++) {
                List<InputVariable> inputs = new ArrayList<>();
                Object output = null;
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
            listTestCase.add(testCases);
        }
        return listTestCase;
    }

    @Override
    public List<TestCase> getSampleTestCaseByProblemWithFormat(Problem problem, Language language) {
        List<ProblemTestCase> problemTestCase = problemTestCaseRepository.findByProblemAndIsSampleAndLanguage(problem,
                true, language);
        List<TestCase> testCases = new ArrayList<>();
        for (int i = 0; i < problemTestCase.size(); i++) {
            List<InputVariable> inputs = new ArrayList<>();
            Object output = null;
            try {
                inputs = objectMapper.readValue(
                        problemTestCase.get(i).getInput(),
                        new TypeReference<List<InputVariable>>() {
                        });
                output = problemTestCase.get(i).getExpectedOutput();

            } catch (Exception e) {
                log.info(e.getMessage());
            }
            testCases.add(new TestCase(inputs, output));
        }
        return testCases;
    }

    @Override
    public void saveListTestCase(List<ProblemTestCase> problemTestCases) {
        problemTestCaseRepository.saveAll(problemTestCases);
    }

    @Transactional
    @Override
    public void removeTestCaseByProblem(Problem problem) {
        problemTestCaseRepository.deleteAllByProblem(problem);
    }

    @Override
    public List<ProblemTestCase> getProblemTestCaseByProblem(Problem problem, Language language) {
        return problemTestCaseRepository.findByProblemAndLanguage(problem, language);
    }

    @Override
    public int getNoTestCaseByProblem(Problem problem) {
        return problemTestCaseRepository.countByProblem(problem);
    }

    @Override
    public List<TestCase> getTestCaseByProblemAndLanguage(Problem problem, Language language) {
        List<ProblemTestCase> problemTestCase = problemTestCaseRepository
                .findByProblemAndLanguage(problem, language);
        List<TestCase> testCases = new ArrayList<>();
        for (int i = 0; i < problemTestCase.size(); i++) {
            List<InputVariable> inputs = new ArrayList<>();
            Object output = null;
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
    public List<TestCase> getSampleTestCaseByProblemAndLanguage(Problem problem, Language language) {
        List<ProblemTestCase> problemTestCase = problemTestCaseRepository
                .findByProblemAndIsSampleAndLanguage(problem, true, language);
        List<TestCase> testCases = new ArrayList<>();
        for (int i = 0; i < problemTestCase.size(); i++) {
            List<InputVariable> inputs = new ArrayList<>();
            Object output = null;
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
    public List<ProblemTestCase> getTestCaseByProblemAndAllLanguage(Problem problem) {
        log.info(problemTestCaseRepository
                .findByProblem(problem));
        return problemTestCaseRepository
                .findByProblem(problem);
    }

    @Override
    public ExamCompileInformationResponseDto getExamProblemCompileInformationByProblem(Problem problem,
            Language language) {
        ExamCompileInformationResponseDto examCompileInformationResponseDto = new ExamCompileInformationResponseDto();
        examCompileInformationResponseDto.setLanguage(language.getName());
        examCompileInformationResponseDto
                .setTemplate(
                        problemTemplateService.findByProblemAndLanguage(problem, language.getName()).getTemplateCode());
        List<TestCase> testCases = getSampleTestCaseByProblemWithFormat(problem, language);
        examCompileInformationResponseDto.setTestCases(testCases);
        List<String> importCommands = new ArrayList<>();
        if (language.getName().equals("Java")) {
            importCommands.add("import java.util.*;");
            importCommands.add("import java.math.*;");
            importCommands.add("import java.io.*;");
            importCommands.add("import static java.util.Map.entry;");
        } else if (language.getName().equals("C")) {
            importCommands.add("#include <stdio.h>");
            importCommands.add("#include <stdlib.h>");
            importCommands.add("#include <string.h>");
            importCommands.add("#include <stdbool.h>");
            importCommands.add("#include <ctype.h>");
            importCommands.add("#include <limits.h>");
            importCommands.add("#include <math.h>");
        }
        examCompileInformationResponseDto.setImportCommands(importCommands);
        return examCompileInformationResponseDto;
    }

}
