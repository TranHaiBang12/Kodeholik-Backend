package com.g44.kodeholik.model.dto.response.problem;

import com.g44.kodeholik.model.enums.problem.Difficulty;
import com.g44.kodeholik.model.enums.problem.ProblemStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemResponseDto {
    private Long id;

    private String title;

    private String description;

    private Difficulty difficulty;

    private float acceptanceRate;

    private int noSubmission;

    private ProblemStatus status;
}
