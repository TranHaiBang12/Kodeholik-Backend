package com.g44.kodeholik.model.dto.request.problem.add;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProblemAddRequestDto {
    private ProblemBasicAddDto problemBasicAddDto;

    private ProblemInputParameterDto problemInputParameterDto;

    private ProblemEditorialDto problemEditorialDto;

    private ProblemTestCaseDto problemTestCaseDto;
}
