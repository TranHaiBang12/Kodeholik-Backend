package com.g44.kodeholik.model.dto.response.problem.solution;

import java.util.List;

import com.g44.kodeholik.model.dto.response.problem.ProblemResponseDto;

public class ProblemSolutionDto {
    private Long id;

    private ProblemResponseDto problem;

    private String title;

    private String textSolution;

    private boolean isProblemImplementation;

    private List<String> skills;
}
