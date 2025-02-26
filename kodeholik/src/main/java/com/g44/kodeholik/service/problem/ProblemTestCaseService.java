package com.g44.kodeholik.service.problem;

import java.util.List;

import com.g44.kodeholik.model.dto.request.lambda.TestCase;
import com.g44.kodeholik.model.dto.response.problem.ProblemCompileResponseDto;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemTestCase;

public interface ProblemTestCaseService {
    public List<TestCase> getTestCaseByProblem(Problem problem);

    public List<ProblemTestCase> getProblemTestCaseByProblem(Problem problem);

    public List<TestCase> getSampleTestCaseByProblem(Problem problem);

    public ProblemCompileResponseDto getProblemCompileInformationByProblem(Problem problem, String languageName);

    public void saveListTestCase(List<ProblemTestCase> problemTestCases);

    public void removeTestCaseByProblem(Problem problem);

    public List<TestCase> getSampleTestCaseByProblemWithFormat(Problem problem);

    public int getNoTestCaseByProblem(Problem problem);
}
