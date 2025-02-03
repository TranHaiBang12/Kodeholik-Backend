package com.g44.kodeholik.service.problem;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemTemplate;

public interface ProblemTemplateService {
    public ProblemTemplate findByProblemAndLanguage(Problem problem, String languageName);

}
