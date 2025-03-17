package com.g44.kodeholik.service.discussion;

import java.util.List;

import org.springframework.data.domain.Page;

import com.g44.kodeholik.model.dto.request.comment.AddCommentRequestDto;
import com.g44.kodeholik.model.dto.response.discussion.CommentResponseDto;
import com.g44.kodeholik.model.entity.discussion.Comment;

public interface CommentService {
    public Page<CommentResponseDto> getCommentsByProblemLink(String link, int page, String sortBy, Boolean ascending);

    public Page<CommentResponseDto> getCommentsByProblemSolutionId(Long solutionId, int page, String sortBy,
            Boolean ascending);

    public boolean isUserVoteComment(Long commentId);

    public Comment getCommentById(Long commentId);

    public void saveComment(Comment comment);

    public CommentResponseDto addComment(AddCommentRequestDto addCommentRequestDto);

    public CommentResponseDto editComment(Long commentId, String newComment);

    public void upvoteComment(Long commentId);

    public void unupvoteComment(Long commentId);

    public List<CommentResponseDto> getAllCommentReplyByComment(Long commentId);

    public int countCommentReply(Long commentId);

    public Page<CommentResponseDto> getCommentsByCourseId(Long courseId, int page, String sortBy, Boolean ascending);
}
