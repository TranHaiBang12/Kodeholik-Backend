package com.g44.kodeholik.repository.problem;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.SolutionCode;
import com.g44.kodeholik.model.entity.problem.SolutionLanguageId;
import java.util.List;

public interface SolutionCodeRepository extends JpaRepository<SolutionCode, SolutionLanguageId> {
    List<SolutionCode> findByProblem(Problem problem);

    void deleteAllByProblem(Problem problem);
}
