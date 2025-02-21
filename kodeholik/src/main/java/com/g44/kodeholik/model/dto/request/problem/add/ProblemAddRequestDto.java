package com.g44.kodeholik.model.dto.request.problem.add;

import org.springframework.web.multipart.MultipartFile;

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

    private MultipartFile testCaseFile;

    private ProblemTestCaseDto problemTestCaseDto;
}
