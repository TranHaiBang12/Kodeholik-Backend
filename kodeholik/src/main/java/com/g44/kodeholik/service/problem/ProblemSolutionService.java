package com.g44.kodeholik.service.problem;

import java.util.List;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSolution;

public interface ProblemSolutionService {
    public ProblemSolution save(ProblemSolution problemSolution);

    public List<ProblemSolution> saveAll(List<ProblemSolution> problemSolutions);

    public List<ProblemSolution> findByProblem(Problem problem);

    public void deleteByProblem(Problem problem);
}
