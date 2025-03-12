package com.g44.kodeholik.service.course.impl;

import com.g44.kodeholik.model.dto.request.discussion.AddCommentRequestDto;
import com.g44.kodeholik.model.dto.response.discussion.CommentResponseDto;
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
    public void createComment(AddCommentRequestDto requestDto) {
        Users currentUser = userService.getCurrentUser();

        Comment comment = Comment.builder()
                .comment(requestDto.getComment())
                .createdBy(currentUser)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();

        if (requestDto.getCommentReply() != null) {
            Comment parentComment = commentRepository.findById(requestDto.getCommentReply())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));
            comment.setCommentReply(parentComment);
        }

        comment = commentRepository.save(comment);

        if (requestDto.getCourseId() != null) {
            Course course = courseRepository.findById(requestDto.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            CourseComment courseComment = new CourseComment(
                    new CourseCommentId(course.getId(), comment.getId()),
                    course,
                    comment
            );

            courseCommentRepository.save(courseComment);
        }
    }

    @Override
    public List<CommentResponseDto> getDiscussionByCourseId(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new RuntimeException("Course not found");
        }

        List<Comment> comments = courseCommentRepository.findCommentsByCourseId(courseId);

        return comments.stream()
                .map(comment -> {
                    CommentResponseDto dto = new CommentResponseDto(comment);
                    dto.setReplyId(comment.getCommentReply() != null ? comment.getCommentReply().getId() : null);
                    dto.setNoReply(commentRepository.countByCommentReply(comment)); // ✅ Gọi repository để đếm số reply
                    return dto;
                })
                .collect(Collectors.toList());
    }

}

