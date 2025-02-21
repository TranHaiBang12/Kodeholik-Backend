package com.g44.kodeholik.repository.problem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSolution;
import com.g44.kodeholik.model.entity.setting.Language;

import java.util.List;

public interface ProblemSolutionRepository extends JpaRepository<ProblemSolution, Long> {

    List<ProblemSolution> findByProblemAndIsProblemImplementation(Problem problem, boolean isProblemImplementation);

    void deleteAllByProblemAndIsProblemImplementation(Problem problem, boolean isProblemImplementation);

    Page<ProblemSolution> findByProblem(Problem problem, Pageable pageable);

    @Query("SELECT p FROM ProblemSolution p JOIN SolutionCode s ON p = s.solution WHERE (cast(:title as text) IS NULL OR (p.title LIKE '%' || cast(:title as text) || '%')) AND (:language IS NULL OR s.language= :language) AND p.isProblemImplementation = :isProblemImplementation")
    Page<ProblemSolution> findByProblemAndIsProblemImplementationAndTitleContain(Problem problem,
            String title,
            Language language,
            boolean isProblemImplementation,
            Pageable pageable);

}
