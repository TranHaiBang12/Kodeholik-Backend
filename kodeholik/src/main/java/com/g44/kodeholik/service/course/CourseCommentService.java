package com.g44.kodeholik.service.course;

import com.g44.kodeholik.model.dto.request.discussion.AddCommentRequestDto;
import com.g44.kodeholik.model.dto.response.discussion.CommentResponseDto;
import com.g44.kodeholik.model.entity.discussion.Comment;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CourseCommentService {
//    List<Comment> getAllCommentsByCourse(Long courseId);
    public void createComment(AddCommentRequestDto requestDto);
    Page<CommentResponseDto> getDiscussionByCourseId(Long courseId, int page, int size, String sortBy, String sortDirection);
    List<CommentResponseDto> getAllCommentReplyByComment(Long commentId);
}

