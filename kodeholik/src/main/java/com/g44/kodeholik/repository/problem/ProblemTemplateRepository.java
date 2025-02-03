package com.g44.kodeholik.repository.problem;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemTemplate;
import com.g44.kodeholik.model.entity.setting.Language;

public interface ProblemTemplateRepository extends JpaRepository<ProblemTemplate, Long> {
    public Optional<ProblemTemplate> findByProblemAndLanguage(Problem problem, Language language);

    public Optional<ProblemTemplate> findByProblem(Problem problem);
}
