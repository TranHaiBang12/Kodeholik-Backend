package com.g44.kodeholik.service.problem;

import java.util.List;

import com.g44.kodeholik.model.dto.request.problem.ProblemRequestDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemResponseDto;

public interface ProblemService {
    public List<ProblemResponseDto> getAllProblems();

    public ProblemResponseDto getProblemById(Long id);

    public ProblemResponseDto createProblem(ProblemRequestDto problemRequest);

    public ProblemResponseDto updateProblem(Long id, ProblemRequestDto problemRequest);

    public void deleteProblem(Long id);
}
