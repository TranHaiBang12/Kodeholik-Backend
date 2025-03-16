package com.g44.kodeholik.model.dto.request.problem.add;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.g44.kodeholik.model.entity.problem.SolutionLanguageId;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SolutionCodeDto {
    @JsonIgnore
    private SolutionLanguageId id;

    @NotNull(message = "MSG02")
    private String solutionLanguage;

    @NotNull(message = "MSG02")
    private String solutionCode;

    private Long submissionId;
}
