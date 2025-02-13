package com.g44.kodeholik.service.problem;

import java.util.List;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.SolutionCode;

public interface SolutionCodeService {
    public void saveAll(List<SolutionCode> solutionCodes);

    public void save(SolutionCode solutionCode);

    public List<SolutionCode> getSolutionCodesByProblem(Problem problem);

    public void deleteByProblem(Problem problem);
}
