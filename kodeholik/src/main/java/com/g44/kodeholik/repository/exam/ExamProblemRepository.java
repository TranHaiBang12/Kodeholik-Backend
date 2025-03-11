package com.g44.kodeholik.repository.exam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.g44.kodeholik.model.entity.exam.Exam;
import com.g44.kodeholik.model.entity.exam.ExamProblem;
import com.g44.kodeholik.model.entity.exam.ExamProblemId;
import com.g44.kodeholik.model.entity.problem.Problem;

import java.util.List;
import java.util.Optional;

public interface ExamProblemRepository extends JpaRepository<ExamProblem, ExamProblemId> {

    @Modifying
    @Transactional
    @Query("DELETE FROM ExamProblem ep WHERE ep.exam = :exam")
    public void deleteByExam(Exam exam);

    public List<ExamProblem> findByExam(Exam exam);

    public Optional<ExamProblem> findByExamAndProblem(Exam exam, Problem problem);
}
