package com.g44.kodeholik.model.dto.response.problem.solution;

import java.util.List;

import com.g44.kodeholik.model.dto.request.problem.add.SolutionCodeDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemResponseDto;
import com.g44.kodeholik.model.dto.response.user.UserResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProblemSolutionDto {
    private Long id;

    private ProblemResponseDto problem;

    private String title;

    private String textSolution;

    private boolean isProblemImplementation;

    private List<String> skills;

    private List<SolutionCodeDto> solutionCodes;

    private boolean isCurrentUserCreated;

    private UserResponseDto createdBy;

    private UserResponseDto updatedBy;
}
