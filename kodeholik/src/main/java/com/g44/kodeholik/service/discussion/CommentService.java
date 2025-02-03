package com.g44.kodeholik.service.discussion;

import java.util.List;

import com.g44.kodeholik.model.dto.response.discussion.CommentResponseDto;

public interface CommentService {
    public List<CommentResponseDto> getCommentsByProblemId(Long problemId, int page, String sortBy, boolean ascending);
}
