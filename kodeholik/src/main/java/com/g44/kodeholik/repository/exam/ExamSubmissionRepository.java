package com.g44.kodeholik.repository.exam;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.g44.kodeholik.model.entity.exam.Exam;
import com.g44.kodeholik.model.entity.exam.ExamParticipant;
import com.g44.kodeholik.model.entity.exam.ExamSubmission;
import com.g44.kodeholik.model.entity.exam.ExamSubmissionId;
import com.g44.kodeholik.model.entity.problem.Problem;

public interface ExamSubmissionRepository extends JpaRepository<ExamSubmission, ExamSubmissionId> {
    public Optional<ExamSubmission> findByExamParticipantAndProblem(ExamParticipant examParticipant, Problem problem);

    public List<ExamSubmission> findByExamParticipant(ExamParticipant examParticipant);

    @Query("SELECT COUNT(e) FROM ExamSubmission e WHERE e.examParticipant.exam = :exam AND e.problem = :problem")
    public int countNoSubmissionProblem(Exam exam, Problem problem);

    @Query("SELECT e FROM ExamSubmission e WHERE e.examParticipant.exam = :exam AND e.problem = :problem")
    public List<ExamSubmission> getListSubmissionByProblem(Exam exam, Problem problem);

}
