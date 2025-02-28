package com.g44.kodeholik.model.dto.response.problem;

import java.util.List;

import com.g44.kodeholik.model.dto.request.problem.add.SolutionCodeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EditorialResponseDto {

    private Long id;

    private ProblemResponseDto problem;

    private String editorialTitle;

    private String editorialTextSolution;

    private List<String> editorialSkills;

    private List<SolutionCodeDto> solutionCodes;
}
