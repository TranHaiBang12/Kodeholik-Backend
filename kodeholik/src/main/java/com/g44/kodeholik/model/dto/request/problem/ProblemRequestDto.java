package com.g44.kodeholik.model.dto.request.problem;

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
public class ProblemRequestDto {
    private String title;

    private String description;

    private Difficulty difficulty;

    private ProblemStatus status;
}
