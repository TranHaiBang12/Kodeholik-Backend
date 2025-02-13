package com.g44.kodeholik.repository.problem;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSolution;
import java.util.List;

public interface ProblemSolutionRepository extends JpaRepository<ProblemSolution, Long> {

    List<ProblemSolution> findByProblem(Problem problem);

    void deleteAllByProblem(Problem problem);
}
