package com.g44.kodeholik.service.problem;

import java.util.List;

import com.g44.kodeholik.model.dto.request.lambda.TestCase;
import com.g44.kodeholik.model.dto.response.exam.student.ExamCompileInformationResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemCompileResponseDto;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemTestCase;
import com.g44.kodeholik.model.entity.setting.Language;

public interface ProblemTestCaseService {
    public List<List<TestCase>> getTestCaseByProblem(Problem problem, List<Language> language);

    public List<TestCase> getTestCaseByProblemAndLanguage(Problem problem, Language language);

    public List<ProblemTestCase> getTestCaseByProblemAndAllLanguage(Problem problem);

    public List<ProblemTestCase> getProblemTestCaseByProblem(Problem problem, Language language);

    public List<List<TestCase>> getSampleTestCaseByProblem(Problem problem, List<Language> language);

    public List<TestCase> getSampleTestCaseByProblemAndLanguage(Problem problem, Language language);

    public ProblemCompileResponseDto getProblemCompileInformationByProblem(Problem problem, Language language);

    public ExamCompileInformationResponseDto getExamProblemCompileInformationByProblem(Problem problem,
            Language language);

    public void saveListTestCase(List<ProblemTestCase> problemTestCases);

    public void removeTestCaseByProblem(Problem problem);

    public List<TestCase> getSampleTestCaseByProblemWithFormat(Problem problem, Language language);

    public int getNoTestCaseByProblem(Problem problem);
}
