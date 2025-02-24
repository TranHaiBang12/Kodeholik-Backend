package com.g44.kodeholik.service.course.impl;

import com.g44.kodeholik.model.entity.course.CourseComment;
import com.g44.kodeholik.model.entity.discussion.Comment;
import com.g44.kodeholik.repository.course.CourseCommentRepository;
import com.g44.kodeholik.service.course.CourseCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseCommentServiceImpl implements CourseCommentService {

    private final CourseCommentRepository courseCommentRepository;

    @Override
    public List<Comment> getAllCommentsByCourse(Long courseId) {
        List<CourseComment> courseComments = courseCommentRepository.findByCourseId(courseId);
        return courseComments.stream()
                .map(CourseComment::getComment)
                .collect(Collectors.toList());
    }
}

