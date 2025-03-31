package com.g44.kodeholik.repository.problem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemTestCase;
import com.g44.kodeholik.model.entity.setting.Language;

public interface ProblemTestCaseRepository extends JpaRepository<ProblemTestCase, Long> {
    List<ProblemTestCase> findByProblemAndIsSampleAndLanguage(Problem problem, boolean isSample, Language language);

    List<ProblemTestCase> findByProblemAndLanguage(Problem problem, Language language);

    List<ProblemTestCase> findByProblem(Problem problem);

    int countByProblem(Problem problem);

    void deleteAllByProblem(Problem problem);
}
