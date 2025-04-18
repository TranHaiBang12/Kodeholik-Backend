package com.g44.kodeholik.model.dto.request.problem.add;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSubmission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class ShareSolutionRequestDto {
    private String link;

    @JsonIgnore
    private Problem problem;

    @NotNull(message = "MSG34")
    @NotBlank(message = "MSG34")
    @Size(min = 10, max = 200, message = "MSG34")
    private String title;

    @NotNull(message = "MSG29")
    @NotBlank(message = "MSG34")
    @Size(min = 10, max = 5000, message = "MSG29")
    private String textSolution;

    private List<String> skills;

    private List<Long> submissionId;

    @JsonIgnore
    private List<ProblemSubmission> submissions;
}
