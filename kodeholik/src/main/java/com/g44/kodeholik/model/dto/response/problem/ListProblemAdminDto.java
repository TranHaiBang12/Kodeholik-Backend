package com.g44.kodeholik.model.dto.response.problem;

import com.g44.kodeholik.model.enums.problem.Difficulty;
import com.g44.kodeholik.model.enums.problem.ProblemStatus;
import com.google.auto.value.AutoValue.Builder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ListProblemAdminDto {
    private String title;

    private String description;

    private String link;

    private Difficulty difficulty;

    private float acceptanceRate;

    private float noSubmission;

    private ProblemStatus status;

    private boolean isActive;
}
