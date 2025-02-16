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
    @NotBlank(message = "Please give a input for this test case")
    private Map<String, String> input;

    @NotBlank(message = "Please give an expected output for this test case")
    private String expectedOutput;

    @NotBlank(message = "Please give a value to know if this test case is sample or not")
    private Boolean isSample;
}
