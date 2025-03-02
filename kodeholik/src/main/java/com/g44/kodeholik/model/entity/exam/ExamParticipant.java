package com.g44.kodeholik.model.entity.exam;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.g44.kodeholik.model.entity.user.Users;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Entity
@FieldNameConstants
@Table(name = "exam_participant", schema = "schema_exam")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ExamParticipant {
    @EmbeddedId
    private ExamParticipantId id;

    @ManyToOne
    @MapsId("examId")
    @JoinColumn(name = "exam_id", referencedColumnName = "id")
    private Exam exam;

    @ManyToOne
    @MapsId("participantId")
    @JoinColumn(name = "participant_id", referencedColumnName = "id")
    private Users participantId;

    private double grade;
}
