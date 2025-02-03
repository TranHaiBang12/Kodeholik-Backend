package com.g44.kodeholik.service.problem.impl;

import org.springframework.stereotype.Service;

import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemTemplate;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.repository.problem.ProblemTemplateRepository;
import com.g44.kodeholik.service.problem.ProblemTemplateService;
import com.g44.kodeholik.service.setting.LanguageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProblemTemplateServiceImpl implements ProblemTemplateService {
    private final ProblemTemplateRepository problemTemplateRepository;
    private final LanguageService languageService;

    @Override
    public ProblemTemplate findByProblemAndLanguage(Problem problem, String languageName) {
        Language language = languageService.findByName(languageName);
        return problemTemplateRepository.findByProblemAndLanguage(problem, language)
                .orElseThrow(() -> new NotFoundException("Problem not found", "Problem not found"));
    }

}
