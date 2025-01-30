package com.g44.kodeholik.model.dto.response.problem;

import java.util.List;

import com.g44.kodeholik.model.dto.request.lambda.InputVariable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProblemTestCaseResponseDto {
    private Long id;
    private List<InputVariable> input;
    private Object expectedOutput;
}
