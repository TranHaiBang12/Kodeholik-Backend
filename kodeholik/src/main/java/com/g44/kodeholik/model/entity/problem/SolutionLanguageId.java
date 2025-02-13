package com.g44.kodeholik.model.entity.problem;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolutionLanguageId implements Serializable {
    private Long solutionId;
    private Long languageId;
}
