package com.g44.kodeholik.service.problem;

import java.util.List;

import com.g44.kodeholik.model.dto.request.problem.add.SolutionCodeDto;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSolution;
import com.g44.kodeholik.model.entity.problem.SolutionCode;

public interface SolutionCodeService {
    public void saveAll(List<SolutionCode> solutionCodes);

    public void save(SolutionCode solutionCode);

    public List<SolutionCode> getSolutionCodesByProblem(Problem problem);

    public void deleteAllEditorialCodeByProblem(Problem problem);

    public List<SolutionCodeDto> findBySolution(ProblemSolution problemSolution);
}
