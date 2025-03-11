package com.g44.kodeholik.model.dto.request.problem.add;

import java.util.List;

import com.g44.kodeholik.model.enums.problem.Difficulty;
import com.g44.kodeholik.model.enums.problem.ProblemStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProblemBasicAddDto {
    @NotNull(message = "MSG34")
    @NotBlank(message = "MSG34")
    @Size(min = 10, max = 200, message = "MSG34")
    private String title;

    @NotNull(message = "MSG02")
    private Difficulty difficulty;

    @NotNull(message = "MSG29")
    @NotBlank(message = "MSG29")
    @Size(min = 10, max = 5000, message = "MSG29")
    private String description;

    @NotNull(message = "MSG02")
    private ProblemStatus status;

    private List<String> topics;

    private List<String> skills;

    @NotNull(message = "{MSG02}")
    private Boolean isActive;

    private List<String> languageSupport;
}
