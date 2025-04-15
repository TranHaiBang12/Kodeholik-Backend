package com.g44.kodeholik.repository.exam;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.g44.kodeholik.model.entity.exam.Exam;
import com.g44.kodeholik.model.entity.exam.ExamParticipant;
import com.g44.kodeholik.model.entity.exam.ExamParticipantId;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.exam.ExamStatus;

public interface ExamParticipantRepository extends JpaRepository<ExamParticipant, ExamParticipantId> {
        public List<ExamParticipant> findByExam(Exam exam);

        public Optional<ExamParticipant> findByExamAndParticipant(Exam exam, Users participant);

        @Query("SELECT e FROM ExamParticipant e WHERE e.exam.code != :code AND e.exam.status = 'NOT_STARTED' AND e.participant = :user AND NOT (e.exam.startTime > :end OR e.exam.endTime < :start)")
        List<ExamParticipant> checkDuplicateTimeExam(
                        String code,
                        Timestamp start,
                        Timestamp end,
                        Users user);

        @Query("SELECT e FROM ExamParticipant e WHERE e.participant = :user AND (:title IS NULL OR LOWER(e.exam.title) LIKE '%' || LOWER(:title) || '%') AND (COALESCE(:status, e.exam.status) = e.exam.status) AND (e.exam.startTime >= :start AND e.exam.endTime <= :end)")
        public Page<ExamParticipant> findByStatus(
                ExamStatus status,
                Users user,
                String title,
                Timestamp start,
                Timestamp end,
                Pageable pageable);

        public List<ExamParticipant> findByParticipant(Users participant);

        @Query("SELECT e.grade, COUNT(e.grade) FROM ExamParticipant e WHERE e.exam = :exam GROUP BY e.grade")
        public List<Object[]> getGradeDistribution(Exam exam);
}
