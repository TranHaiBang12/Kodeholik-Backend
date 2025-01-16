package com.g44.kodeholik.repository.problem;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.problem.Problem;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

}
