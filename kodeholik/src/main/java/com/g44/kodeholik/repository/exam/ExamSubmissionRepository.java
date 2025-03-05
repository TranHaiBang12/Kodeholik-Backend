package com.g44.kodeholik.repository.exam;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.exam.ExamParticipant;
import com.g44.kodeholik.model.entity.exam.ExamSubmission;
import com.g44.kodeholik.model.entity.exam.ExamSubmissionId;
import com.g44.kodeholik.model.entity.problem.Problem;

public interface ExamSubmissionRepository extends JpaRepository<ExamSubmission, ExamSubmissionId> {
    public Optional<ExamSubmission> findByExamParticipantAndProblem(ExamParticipant examParticipant, Problem problem);
}
