package com.g44.kodeholik.repository.problem;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSubmission;
import com.g44.kodeholik.model.entity.user.Users;

public interface ProblemSubmissionRepository extends JpaRepository<ProblemSubmission, Long> {
    public long countByIsAcceptedAndProblem(boolean isAccepted, Problem problem);

    @Query("SELECT COUNT(DISTINCT p.problem) FROM ProblemSubmission p WHERE p.user = :user AND p.isAccepted = :isAccepted AND p.problem IN :problems")
    public long countByUserAndIsAcceptedAndProblemIn(Users user, boolean isAccepted, List<Problem> problems);

    public List<ProblemSubmission> findByUserAndProblemAndIsAccepted(Users user, Problem problem,
            boolean isAccepted);

    public List<ProblemSubmission> findByUserAndProblem(Users user, Problem problem);

    public Page<ProblemSubmission> findByProblem(Problem problem, Pageable pageable);

}
