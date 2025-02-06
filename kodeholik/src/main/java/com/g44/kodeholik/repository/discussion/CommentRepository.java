package com.g44.kodeholik.repository.discussion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.discussion.Comment;
import com.g44.kodeholik.model.entity.problem.Problem;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Page<Comment> findByProblemListContaining(Problem problem, Pageable
    // pageable);
}
