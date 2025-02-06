package com.g44.kodeholik.service.problem;

import com.g44.kodeholik.model.dto.request.problem.ProblemCompileRequestDto;
import com.g44.kodeholik.model.dto.response.problem.submission.SubmissionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.run.RunProblemResponseDto;
import com.g44.kodeholik.model.entity.problem.Problem;

public interface ProblemSubmissionService {
    public SubmissionResponseDto submitProblem(Long problemId, ProblemCompileRequestDto problemCompileRequestDto);

    public RunProblemResponseDto run(Long problemId, ProblemCompileRequestDto problemCompileRequestDto);

    public long getNumberAcceptedSubmission(Long problemId);
}
