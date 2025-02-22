package com.g44.kodeholik.model.dto.request.problem.add;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ShareSolutionRequestDto {

    private Long problemId;

    private String title;

    private String textSolution;

    private boolean isProblemImplementation;

    private List<String> skills;

    private List<SolutionCodeDto> solutionCodes;
}
