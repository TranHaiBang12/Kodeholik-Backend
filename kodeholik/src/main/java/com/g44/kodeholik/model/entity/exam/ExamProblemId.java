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
public class ExamProblemId implements Serializable {
    private Long examId;
    private Long problemId;
}
