package com.g44.kodeholik.service.problem;

import java.util.List;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemInputParameter;

public interface ProblemInputParameterService {
    public List<ProblemInputParameter> getProblemInputParameters(Problem problem);

    public void addListInputParameters(List<ProblemInputParameter> listInputParameters);

    public void deleteProblemInputParameters(Problem problem);
}
