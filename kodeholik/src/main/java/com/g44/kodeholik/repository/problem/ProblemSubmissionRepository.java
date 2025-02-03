package com.g44.kodeholik.repository.problem;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.problem.ProblemSubmission;

public interface ProblemSubmissionRepository extends JpaRepository<ProblemSubmission, Long> {

}
