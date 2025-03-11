package com.g44.kodeholik.model.dto.request.problem;

import com.g44.kodeholik.model.enums.problem.Difficulty;
import com.g44.kodeholik.model.enums.problem.ProblemStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FilterProblemRequestAdminDto {
    private String title;

    private Difficulty difficulty;

    private ProblemStatus status;

    private Boolean isActive;

    @NotNull
    @Min(0)
    private int page;

    private Integer size;

    private String sortBy;

    private Boolean ascending;
}
