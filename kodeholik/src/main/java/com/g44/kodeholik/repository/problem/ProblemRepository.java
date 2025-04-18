package com.g44.kodeholik.repository.problem;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.problem.Difficulty;
import com.g44.kodeholik.model.enums.problem.ProblemStatus;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

        public Optional<Problem> findByLink(String link);

        public List<Problem> findByDifficulty(Difficulty difficulty);

        public List<Problem> findByStatusAndIsActive(ProblemStatus status, boolean isActive);

        public Optional<Problem> findByIdAndStatusAndIsActive(Long id, ProblemStatus status, boolean isActive);

        public Optional<Problem> findByLinkAndStatusAndIsActive(String link, ProblemStatus status, boolean isActive);

        @Query("SELECT COUNT(p) > 0 FROM Problem p WHERE p.title = :title")
        public boolean isTitleExisted(@Param("title") String title);

        public Page<Problem> findByUsersFavouriteContains(Users user, Pageable pageable);

        @Query("SELECT p FROM Problem p WHERE (cast(:title as text) IS NULL OR (LOWER(p.title) LIKE '%' || LOWER(CAST(:title AS text)) || '%')) AND (COALESCE(:difficulty, p.difficulty) = p.difficulty) AND (COALESCE(:status, p.status) = p.status) AND (:isActive IS NULL OR p.isActive = :isActive)")
        public Page<Problem> findByTitleContainsAndDifficultyAndStatusAndIsActive(String title, Difficulty difficulty,
                        ProblemStatus status, Boolean isActive, Pageable pageable);

        public List<Problem> findAllByOrderByNoSubmissionDescAcceptanceRateDesc();

        public List<Problem> findByCreatedAtBetweenOrderByNoSubmissionDescAcceptanceRateDesc(
                        Timestamp start, Timestamp end);

        public List<Problem> findByCreatedAtBetweenAndDifficultyOrderByNoSubmissionDescAcceptanceRateDesc(
                        Timestamp start, Timestamp end, Difficulty difficulty);
}
