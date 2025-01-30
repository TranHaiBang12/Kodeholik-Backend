package com.g44.kodeholik.service.problem;

import java.util.List;

import com.g44.kodeholik.model.entity.problem.ProblemInputParameter;

public interface ProblemInputParameterService {
    public List<ProblemInputParameter> getProblemInputParameters(Long problemId);
}
