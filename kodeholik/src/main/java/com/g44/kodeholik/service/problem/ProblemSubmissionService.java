package com.g44.kodeholik.service.problem;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.g44.kodeholik.model.dto.request.lambda.TestCase;
import com.g44.kodeholik.model.dto.request.problem.ProblemCompileRequestDto;
import com.g44.kodeholik.model.dto.response.problem.submission.ProblemSubmissionDto;
import com.g44.kodeholik.model.dto.response.problem.submission.SubmissionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.run.RunProblemResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.SubmissionListResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.SuccessSubmissionListResponseDto;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSubmission;
import com.g44.kodeholik.model.entity.problem.ProblemTemplate;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.model.entity.user.Users;

public interface ProblemSubmissionService {
        public SubmissionResponseDto submitProblem(Problem problem,
                        ProblemCompileRequestDto problemCompileRequestDto,
                        List<TestCase> testCases,
                        ProblemTemplate problemTemplate);

        public RunProblemResponseDto run(Problem problem,
                        ProblemCompileRequestDto problemCompileRequestDto,
                        List<TestCase> testCases,
                        ProblemTemplate problemTemplate);

        public long getNumberAcceptedSubmission(Problem problem);

        public boolean checkIsCurrentUserSolvedProblem(Problem problem);

        public Long countByIsAcceptedAndProblem(boolean isAccepted, Problem problem);

        public long countByUserAndIsAcceptedAndProblemIn(Users user, boolean isAccepted, List<Problem> problems);

        public ProblemSubmissionDto getSubmissionDtoById(Long submissionId);

        public ProblemSubmission getProblemSubmissionById(Long submissionId);

        public List<SubmissionListResponseDto> getListSubmission(
                        Problem problem,
                        Users user);

        public List<SuccessSubmissionListResponseDto> getSuccessSubmissionList(
                        List<Long> excludes,
                        Problem problem,
                        Users user);

        public SubmissionResponseDto getSubmissionDetail(ProblemSubmission problemSubmission, int noTestCase,
                        Users currentUser);
}
