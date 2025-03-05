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

    @Query("SELECT e FROM ExamParticipant e WHERE e.exam.status = :status AND e.participant = :user")
    public Page<ExamParticipant> findByStatus(ExamStatus status, Users user, Pageable pageable);
}
