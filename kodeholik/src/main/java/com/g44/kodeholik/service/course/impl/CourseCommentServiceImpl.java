package com.g44.kodeholik.service.course.impl;

import com.g44.kodeholik.model.dto.request.discussion.AddCommentRequestDto;
import com.g44.kodeholik.model.entity.course.Course;
import com.g44.kodeholik.model.entity.course.CourseComment;
import com.g44.kodeholik.model.entity.course.CourseCommentId;
import com.g44.kodeholik.model.entity.discussion.Comment;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.repository.course.CourseCommentRepository;
import com.g44.kodeholik.repository.course.CourseRepository;
import com.g44.kodeholik.repository.discussion.CommentRepository;
import com.g44.kodeholik.repository.user.UserRepository;
import com.g44.kodeholik.service.course.CourseCommentService;
import com.g44.kodeholik.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseCommentServiceImpl implements CourseCommentService {

    private final CourseCommentRepository courseCommentRepository;
    private final CommentRepository commentRepository;
    private final CourseRepository courseRepository;
    private final UserService userService;
    @Override
    public List<Comment> getAllCommentsByCourse(Long courseId) {
        List<CourseComment> courseComments = courseCommentRepository.findByCourseId(courseId);
        return courseComments.stream()
                .map(CourseComment::getComment)
                .collect(Collectors.toList());
    }

    @Override
    public Comment addCommentToCourse(Long courseId, AddCommentRequestDto request) {
        Users currentUser = userService.getCurrentUser();
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        Comment comment = new Comment();
        comment.setComment(request.getComment());
        comment.setCreatedBy(currentUser);
        comment.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        comment = commentRepository.save(comment);

        CourseComment courseComment = new CourseComment();
        courseComment.setId(new CourseCommentId(courseId, comment.getId()));
        courseComment.setCourse(course);
        courseComment.setComment(comment);
        courseCommentRepository.save(courseComment);

        return comment;
    }
}

