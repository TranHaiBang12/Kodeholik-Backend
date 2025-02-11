package com.g44.kodeholik.service.discussion;

import org.springframework.data.domain.Page;

import com.g44.kodeholik.model.dto.response.discussion.CommentResponseDto;

public interface CommentService {
    public Page<CommentResponseDto> getCommentsByProblemId(Long problemId, int page, String sortBy, Boolean ascending);
}
