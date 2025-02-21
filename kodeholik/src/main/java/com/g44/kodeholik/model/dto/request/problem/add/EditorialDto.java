package com.g44.kodeholik.model.dto.request.problem.add;

import java.util.List;

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
public class EditorialDto {
    @NotNull(message = "MSG02")
    @NotBlank(message = "MSG34")
    @Size(min = 10, max = 200, message = "MSG34")
    private String editorialTitle;

    @NotNull(message = "MSG02")
    @NotBlank(message = "MSG29")
    @Size(min = 10, max = 5000, message = "MSG29")
    private String editorialTextSolution;

    private List<String> editorialSkills;

    @NotNull(message = "MSG02")
    private List<SolutionCodeDto> solutionCodes;
}
