package com.g44.kodeholik.service.problem;

import java.util.List;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemTemplate;

public interface ProblemTemplateService {
    public ProblemTemplate findByProblemAndLanguage(Problem problem, String languageName);

    public void addListTemplate(List<ProblemTemplate> templates);

    public List<ProblemTemplate> getAllTemplatesByProblem(Problem problem);

    public void deleteTemplatesByProblem(Problem problem);

}
