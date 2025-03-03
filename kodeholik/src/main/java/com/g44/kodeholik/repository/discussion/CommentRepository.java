package com.g44.kodeholik.repository.discussion;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.discussion.Comment;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSolution;

import java.util.List;
import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Page<Comment> findByProblemListContaining(Problem problem, Pageable
    // pageable);

    public List<Comment> findByCommentReply(Comment commentReply);

    public List<Comment> findByCommentReplyAndProblemsContains(Comment commentReply, Problem problem);

    public List<Comment> findByCommentReplyAndProblemSolutionsContains(Comment commentReply, ProblemSolution solution);

    public int countByCommentReply(Comment commentReply);
}
