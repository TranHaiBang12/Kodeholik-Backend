package com.g44.kodeholik.service.course;

import com.g44.kodeholik.model.dto.request.discussion.AddCommentRequestDto;
import com.g44.kodeholik.model.entity.discussion.Comment;

import java.util.List;

public interface CourseCommentService {
    List<Comment> getAllCommentsByCourse(Long courseId);
    Comment addCommentToCourse(Long courseId, AddCommentRequestDto request);
}

