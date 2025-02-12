package com.g44.kodeholik.repository.problem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.enums.problem.Difficulty;
import com.g44.kodeholik.model.enums.problem.ProblemStatus;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    public List<Problem> findByDifficulty(Difficulty difficulty);

    public List<Problem> findByStatus(ProblemStatus status);
}
