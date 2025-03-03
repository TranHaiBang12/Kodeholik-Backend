package com.g44.kodeholik.repository.exam;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.exam.ExamParticipant;
import com.g44.kodeholik.model.entity.exam.ExamParticipantId;

public interface ExamParticipantRepository extends JpaRepository<ExamParticipant, ExamParticipantId> {

}
