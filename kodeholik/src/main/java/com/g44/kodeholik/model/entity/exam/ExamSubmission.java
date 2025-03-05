package com.g44.kodeholik.model.entity.exam;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSubmission;
import com.g44.kodeholik.model.entity.user.Users;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.experimental.FieldNameConstants;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@FieldNameConstants
@Table(name = "exam_submission", schema = "schema_exam")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ExamSubmission {

    @EmbeddedId
    private ExamSubmissionId id;

    @ManyToOne
    @MapsId("examParticipantId")
    @JoinColumns({
            @JoinColumn(name = "exam_id", referencedColumnName = "exam_id"),
            @JoinColumn(name = "participant_id", referencedColumnName = "participant_id")
    })
    private ExamParticipant examParticipant;

    @ManyToOne
    @MapsId("problemId")
    @JoinColumn(name = "problem_id", referencedColumnName = "id")
    private Problem problem;

    @ManyToOne
    @JoinColumn(name = "submission_id", referencedColumnName = "id")
    private ProblemSubmission problemSubmission;

    private double point;
}
