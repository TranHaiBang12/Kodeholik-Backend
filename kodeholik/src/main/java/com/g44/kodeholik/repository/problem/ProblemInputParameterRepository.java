package com.g44.kodeholik.repository.problem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemInputParameter;

public interface ProblemInputParameterRepository extends JpaRepository<ProblemInputParameter, Long> {
    public List<ProblemInputParameter> findByProblem(Problem problem);

    public void deleteAllByProblem(Problem problem);
}
