package com.g44.kodeholik.repository.problem;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSubmission;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.problem.SubmissionStatus;
import com.g44.kodeholik.model.enums.setting.Level;

public interface ProblemSubmissionRepository extends JpaRepository<ProblemSubmission, Long> {
        public long countByIsAcceptedAndProblem(boolean isAccepted, Problem problem);

        @Query("SELECT COUNT(DISTINCT p.problem) FROM ProblemSubmission p WHERE p.user = :user AND p.isAccepted = :isAccepted AND p.problem IN :problems")
        public long countByUserAndIsAcceptedAndProblemIn(
                        Users user,
                        boolean isAccepted,
                        List<Problem> problems);

        @Query("SELECT COUNT(DISTINCT p.problem) FROM ProblemSubmission p WHERE p.isAccepted = :isAccepted AND p.problem IN :problems")
        public long countByIsAcceptedAndProblemIn(
                        boolean isAccepted,
                        List<Problem> problems);

        public List<ProblemSubmission> findByUserAndProblemAndIsAccepted(
                        Users user,
                        Problem problem,
                        boolean isAccepted);

        public List<ProblemSubmission> findByUserAndProblem(Users user, Problem problem);

        @Query("SELECT p FROM ProblemSubmission p WHERE p.user = :user AND (:problem IS NULL OR p.problem = :problem) AND (COALESCE(:status, p.status) = p.status) AND (p.createdAt >= :start AND p.createdAt <= :end)")
        public Page<ProblemSubmission> findByUserAndTimeBetween(
                        @Param("user") Users user,
                        @Param("problem") Problem problem,
                        @Param("status") SubmissionStatus status,
                        @Param("start") Timestamp start,
                        @Param("end") Timestamp end,
                        Pageable pageable);

        public Page<ProblemSubmission> findByProblem(Problem problem, Pageable pageable);

        @Query("SELECT DISTINCT p.problem FROM ProblemSubmission p WHERE p.user = :user")
        public List<Problem> findByUserAndProblemDistinct(Users user);

        @Query("SELECT p.problem, p.problem.noSubmission, MAX(p.createdAt) AS createdAt  FROM ProblemSubmission p WHERE p.user = :user  GROUP BY p.problem")
        public Page<Object[]> findLastSubmittedByUserAndProblemIn(Users user,
                        Pageable pageable);

        @Query("SELECT p.problem, p.problem.noSubmission, MAX(p.createdAt) AS createdAt " +
                        "FROM ProblemSubmission p " +
                        "WHERE p.user = :user AND " +
                        "(p.problem IN " +
                        "(SELECT ps.problem FROM ProblemSubmission ps WHERE 'SUCCESS' = ps.status)) "
                        +
                        "GROUP BY p.problem")
        Page<Object[]> findLastSubmittedByUserAndProblemInAndSuccessStatus(
                        @Param("user") Users user,
                        Pageable pageable);

        @Query("SELECT p.problem, p.problem.noSubmission, MAX(p.createdAt) AS createdAt " +
                        "FROM ProblemSubmission p " +
                        "WHERE p.user = :user AND " +
                        "(p.problem NOT IN " +
                        "(SELECT ps.problem FROM ProblemSubmission ps WHERE 'SUCCESS' = ps.status)) "
                        +
                        "GROUP BY p.problem")
        Page<Object[]> findLastSubmittedByUserAndProblemInAndFailedStatus(
                        @Param("user") Users user,
                        Pageable pageable);

        @Query("SELECT sk, COUNT(DISTINCT p.title) FROM ProblemSubmission ps " +
                        "JOIN ps.problem p " +
                        "JOIN p.skills sk " +
                        "WHERE ps.status = 'SUCCESS' AND ps.user = :user AND sk.level = :level " +
                        "GROUP BY sk")
        List<Object[]> findNumberSkillUserSolved(
                        @Param("user") Users user,
                        Level level);

        @Query("SELECT t, COUNT(DISTINCT p.title) FROM ProblemSubmission ps " +
                        "JOIN ps.problem p " +
                        "JOIN p.topics t " +
                        "WHERE ps.status = 'SUCCESS' AND ps.user = :user " +
                        "GROUP BY t")
        List<Object[]> findNumberTopicUserSolved(
                        @Param("user") Users user);

        @Query("SELECT l, COUNT(DISTINCT p.title) FROM ProblemSubmission ps " +
                        "JOIN ps.problem p " +
                        "JOIN ps.language l " +
                        "WHERE ps.status = 'SUCCESS' AND ps.user = :user " +
                        "GROUP BY l")
        List<Object[]> findNumberLanguageUserSolved(
                        @Param("user") Users user);

        @Query("SELECT COUNT(*) FROM ProblemSubmission ps WHERE ps.user = :user AND ps.status = 'SUCCESS'")
        public int getNumberSuccessSubmission(Users user);

        @Query("SELECT COUNT(*) FROM ProblemSubmission ps WHERE ps.user = :user")
        public int getTotalSubmission(Users user);

        @Query("SELECT p FROM ProblemSubmission p WHERE p.createdAt >= :start AND p.createdAt <= :end")
        public List<ProblemSubmission> getSubmissionsByTimeBetween(Timestamp start, Timestamp end);

}
