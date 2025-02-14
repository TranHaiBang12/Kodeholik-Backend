package com.g44.kodeholik.service.discussion;

import org.springframework.data.domain.Page;

import com.g44.kodeholik.model.dto.response.discussion.CommentResponseDto;
import com.g44.kodeholik.model.entity.discussion.Comment;

public interface CommentService {
    public Page<CommentResponseDto> getCommentsByProblemId(Long problemId, int page, String sortBy, Boolean ascending);

    public Comment getCommentById(Long commentId);

    public void saveComment(Comment comment);

    public void deleteComment(Long commentId);
}
