package com.g44.kodeholik.repository.problem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSolution;
import com.g44.kodeholik.model.entity.problem.SolutionCode;
import com.g44.kodeholik.model.entity.problem.SolutionLanguageId;
import java.util.List;

public interface SolutionCodeRepository extends JpaRepository<SolutionCode, SolutionLanguageId> {
    List<SolutionCode> findByProblem(Problem problem);

    @Modifying
    @Query("DELETE FROM SolutionCode s WHERE s.problem = :problem AND s.solution IN " +
            "(SELECT sol FROM ProblemSolution sol WHERE sol.isProblemImplementation = true)")
    void deleteAllEditorialCodeByProblem(@Param("problem") Problem problem);

    List<SolutionCode> findBySolution(ProblemSolution solution);
}
