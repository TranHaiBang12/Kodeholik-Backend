package com.g44.kodeholik.model.dto.request.problem.add;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TestCaseDto {
    @NotBlank(message = "MSG02")
    private Map<String, String> input;

    @NotBlank(message = "MSG02")
    private String expectedOutput;

    @NotBlank(message = "MSG02")
    private Boolean isSample;
}
