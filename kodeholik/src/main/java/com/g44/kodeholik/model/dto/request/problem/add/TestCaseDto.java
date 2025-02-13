package com.g44.kodeholik.model.dto.request.problem.add;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TestCaseDto {
    private String input;

    private String expectedOutput;

    private Boolean isSample;
}
