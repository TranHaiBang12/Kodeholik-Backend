package com.g44.kodeholik.repository.exam;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.g44.kodeholik.model.entity.exam.Exam;
import com.g44.kodeholik.model.enums.exam.ExamStatus;

public interface ExamRepository extends JpaRepository<Exam, Long> {
        public Optional<Exam> findByCode(String code);

        @Query("SELECT e FROM Exam e WHERE (cast(:title as text) IS NULL OR (LOWER(e.title) LIKE '%' || cast(:title as text) || '%')) AND (COALESCE(:status, e.status) = e.status) AND (e.startTime >= :start AND e.endTime <= :end)")
        public Page<Exam> searchExam(
                String title,
                ExamStatus status,
                Timestamp start,
                Timestamp end,
                Pageable pageable);

        @Query("SELECT e.code FROM Exam e WHERE e.noParticipant > 0 AND e.startTime >= :now AND e.startTime <= :maxTime")
        List<String> getCodeFromExamReadyToStarted(@Param("now") Timestamp now, @Param("maxTime") Timestamp maxTime);

        @Query("SELECT e FROM Exam e WHERE (e.status = 'NOT_STARTED' OR e.status = 'IN_PROGRESS') AND e.endTime <= :now")
        List<Exam> getExamAlreadyEnded(@Param("now") Timestamp now);

        @Query("SELECT e FROM Exam e WHERE e.status = 'NOT_STARTED' AND (e.startTime >= :now AND e.startTime <= :maxTime)")
        List<Exam> getExamAboutToStart(@Param("now") Timestamp now, @Param("maxTime") Timestamp maxTime);

        List<Exam> findByStatus(ExamStatus status);

}
