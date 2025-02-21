package com.g44.kodeholik.repository.discussion;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.discussion.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Page<Comment> findByProblemListContaining(Problem problem, Pageable
    // pageable);
}
