package com.g44.kodeholik.repository.problem;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.enums.problem.Difficulty;
import com.g44.kodeholik.model.enums.problem.ProblemStatus;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    public List<Problem> findByDifficulty(Difficulty difficulty);

    public List<Problem> findByStatusAndIsActive(ProblemStatus status, boolean isActive);

    public Optional<Problem> findByIdAndStatusAndIsActive(Long id, ProblemStatus status, boolean isActive);
}
