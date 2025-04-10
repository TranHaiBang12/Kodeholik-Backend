package com.g44.kodeholik.model.entity.exam;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamSubmissionId implements Serializable {
    private ExamParticipantId examParticipantId;
    private Long problemId;
}
