package com.g44.kodeholik.repository.discussion;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.discussion.Comment;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Page<Comment> findByProblemListContaining(Problem problem, Pageable
    // pageable);

    public List<Comment> findByCommentReply(Comment commentReply);

    public int countByCommentReply(Comment commentReply);
}
