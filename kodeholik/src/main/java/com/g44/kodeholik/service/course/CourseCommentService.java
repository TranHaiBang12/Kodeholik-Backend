package com.g44.kodeholik.service.course;

import com.g44.kodeholik.model.dto.request.discussion.AddCommentRequestDto;
import com.g44.kodeholik.model.dto.response.discussion.CommentResponseDto;
import com.g44.kodeholik.model.entity.discussion.Comment;

import java.util.List;

public interface CourseCommentService {
//    List<Comment> getAllCommentsByCourse(Long courseId);
    public void createComment(AddCommentRequestDto requestDto);
    List<CommentResponseDto> getDiscussionByCourseId(Long courseId);
}

