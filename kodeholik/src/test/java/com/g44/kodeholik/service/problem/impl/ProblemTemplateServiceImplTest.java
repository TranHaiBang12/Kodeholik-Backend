package com.g44.kodeholik.service.problem.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemTemplate;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.repository.problem.ProblemTemplateRepository;
import com.g44.kodeholik.service.setting.LanguageService;

class ProblemTemplateServiceImplTest {

    @Mock
    private ProblemTemplateRepository problemTemplateRepository;

    @Mock
    private LanguageService languageService;

    @InjectMocks
    private ProblemTemplateServiceImpl problemTemplateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByProblemAndLanguage() {
        Problem problem = new Problem();
        Language language = new Language();
        language.setName("English");
        ProblemTemplate template = new ProblemTemplate();

        when(languageService.findByName("English")).thenReturn(language);
        when(problemTemplateRepository.findByProblemAndLanguage(problem, language)).thenReturn(Optional.of(template));

        ProblemTemplate result = problemTemplateService.findByProblemAndLanguage(problem, "English");

        assertEquals(template, result);
    }

    @Test
    void testFindByProblemAndLanguage_NotFound() {
        Problem problem = new Problem();
        Language language = new Language();
        language.setName("English");

        when(languageService.findByName("English")).thenReturn(language);
        when(problemTemplateRepository.findByProblemAndLanguage(problem, language)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> problemTemplateService.findByProblemAndLanguage(problem, "English"));
    }

    @Test
    void testAddListTemplate() {
        List<ProblemTemplate> templates = List.of(new ProblemTemplate(), new ProblemTemplate());

        problemTemplateService.addListTemplate(templates);

        verify(problemTemplateRepository).saveAll(templates);
    }

    @Test
    void testGetAllTemplatesByProblem() {
        Problem problem = new Problem();
        List<ProblemTemplate> templates = List.of(new ProblemTemplate(), new ProblemTemplate());

        when(problemTemplateRepository.findByProblem(problem)).thenReturn(templates);

        List<ProblemTemplate> result = problemTemplateService.getAllTemplatesByProblem(problem);

        assertEquals(templates, result);
    }

    @Test
    void testDeleteTemplatesByProblem() {
        Problem problem = new Problem();

        problemTemplateService.deleteTemplatesByProblem(problem);

        verify(problemTemplateRepository).deleteAllByProblem(problem);
    }

    @Test
    void testAddTemplate() {
        ProblemTemplate template = new ProblemTemplate();

        problemTemplateService.addTemplate(template);

        verify(problemTemplateRepository).save(template);
    }
}