package com.g44.kodeholik.repository.problem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemTestCase;

public interface ProblemTestCaseRepository extends JpaRepository<ProblemTestCase, Long> {
    List<ProblemTestCase> findByProblemAndIsSample(Problem problem, boolean isSample);

}
