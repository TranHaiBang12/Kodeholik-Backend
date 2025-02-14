package com.g44.kodeholik.repository.problem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSolution;
import java.util.List;

public interface ProblemSolutionRepository extends JpaRepository<ProblemSolution, Long> {

    List<ProblemSolution> findByProblemAndIsProblemImplementation(Problem problem, boolean isProblemImplementation);

    void deleteAllByProblemAndIsProblemImplementation(Problem problem, boolean isProblemImplementation);

    Page<ProblemSolution> findByProblem(Problem problem, Pageable pageable);

}
