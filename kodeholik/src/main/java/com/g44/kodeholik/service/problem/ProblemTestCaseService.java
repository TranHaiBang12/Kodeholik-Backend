package com.g44.kodeholik.service.problem;

import java.util.List;

import com.g44.kodeholik.model.dto.request.lambda.TestCase;
import com.g44.kodeholik.model.dto.response.problem.ProblemCompileResponseDto;

public interface ProblemTestCaseService {
    public List<TestCase> getTestCaseByProblemId(Long problemId);

    public ProblemCompileResponseDto getProblemCompileInformationById(Long problemId, String languageName);
}
