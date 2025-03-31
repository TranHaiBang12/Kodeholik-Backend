package com.g44.kodeholik.service.problem.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g44.kodeholik.model.dto.request.lambda.InputVariable;
import com.g44.kodeholik.model.dto.request.lambda.TestCase;
import com.g44.kodeholik.model.dto.response.exam.student.ExamCompileInformationResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemCompileResponseDto;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemTemplate;
import com.g44.kodeholik.model.entity.problem.ProblemTestCase;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.repository.problem.ProblemTestCaseRepository;
import com.g44.kodeholik.service.problem.ProblemTemplateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ProblemTestCaseServiceImplTest {

    @Mock
    private ProblemTestCaseRepository problemTestCaseRepository;

    @Mock
    private ProblemTemplateService problemTemplateService;

    @InjectMocks
    private ProblemTestCaseServiceImpl problemTestCaseServiceImpl;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetProblemCompileInformationByProblem() {
        Problem problem = new Problem();
        Language language = new Language();
        language.setName("Java");

        ProblemTemplate problemTemplate = new ProblemTemplate();
        problemTemplate.setTemplateCode("templateCode");

        when(problemTemplateService.findByProblemAndLanguage(problem, "Java"))
                .thenReturn(problemTemplate);

        ProblemCompileResponseDto response = problemTestCaseServiceImpl.getProblemCompileInformationByProblem(problem,
                language);

        assertEquals("templateCode", response.getTemplate());
    }

    @Test
    public void testGetTestCaseByProblem() {
        Problem problem = new Problem();
        List<Language> languages = new ArrayList<>();
        Language language = new Language();
        languages.add(language);

        List<ProblemTestCase> problemTestCases = new ArrayList<>();
        ProblemTestCase problemTestCase = new ProblemTestCase();
        problemTestCase.setInput("[{\"name\":\"input1\",\"type\":\"String\"}]");
        problemTestCase.setExpectedOutput("output");
        problemTestCases.add(problemTestCase);

        when(problemTestCaseRepository.findByProblemAndLanguage(problem, language)).thenReturn(problemTestCases);

        List<List<TestCase>> response = problemTestCaseServiceImpl.getTestCaseByProblem(problem, languages);

        assertEquals(1, response.size());
        assertEquals(1, response.get(0).size());
        assertEquals("output", response.get(0).get(0).getExpectedOutput());
    }

    @Test
    public void testSaveListTestCase() {
        List<ProblemTestCase> problemTestCases = new ArrayList<>();
        problemTestCaseServiceImpl.saveListTestCase(problemTestCases);
        verify(problemTestCaseRepository, times(1)).saveAll(problemTestCases);
    }

    @Test
    public void testRemoveTestCaseByProblem() {
        Problem problem = new Problem();
        problemTestCaseServiceImpl.removeTestCaseByProblem(problem);
        verify(problemTestCaseRepository, times(1)).deleteAllByProblem(problem);
    }

    @Test
    public void testGetNoTestCaseByProblem() {
        Problem problem = new Problem();
        when(problemTestCaseRepository.countByProblem(problem)).thenReturn(5);
        int count = problemTestCaseServiceImpl.getNoTestCaseByProblem(problem);
        assertEquals(5, count);
    }

    @Test
    public void testGetExamProblemCompileInformationByProblem() {
        Problem problem = new Problem();
        Language language = new Language();
        language.setName("Java");

        ProblemTemplate problemTemplate = new ProblemTemplate();
        problemTemplate.setTemplateCode("templateCode");

        when(problemTemplateService.findByProblemAndLanguage(problem, "Java"))
                .thenReturn(problemTemplate);

        ExamCompileInformationResponseDto response = problemTestCaseServiceImpl
                .getExamProblemCompileInformationByProblem(problem, language);

        assertEquals("Java", response.getLanguage());
        assertEquals("templateCode", response.getTemplate());
    }
}