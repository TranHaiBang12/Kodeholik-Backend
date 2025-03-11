package com.g44.kodeholik.repository.discussion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<Comment> findByCommentReplyAndProblemsContains(Comment commentReply, Problem problem,
            Pageable pageable);

    public Page<Comment> findByCommentReplyAndProblemSolutionsContains(Comment commentReply, ProblemSolution solution,
            Pageable pageable);

    public int countByCommentReply(Comment commentReply);

    public int countByProblemsContains(Problem problem);

    public int countByProblemSolutionsContains(ProblemSolution solution);

}
