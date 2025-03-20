package com.g44.kodeholik.service.course.impl;

import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.discussion.AddCommentRequestDto;
import com.g44.kodeholik.model.dto.response.discussion.CommentResponseDto;
import com.g44.kodeholik.model.dto.response.user.UserResponseDto;
import com.g44.kodeholik.model.entity.course.Course;
import com.g44.kodeholik.model.entity.course.CourseComment;
import com.g44.kodeholik.model.entity.course.CourseCommentId;
import com.g44.kodeholik.model.entity.discussion.Comment;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.repository.course.CourseCommentRepository;
import com.g44.kodeholik.repository.course.CourseRepository;
import com.g44.kodeholik.repository.discussion.CommentRepository;
import com.g44.kodeholik.repository.user.UserRepository;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.service.course.CourseCommentService;
import com.g44.kodeholik.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseCommentServiceImpl implements CourseCommentService {

    private final CourseCommentRepository courseCommentRepository;
    private final CommentRepository commentRepository;
    private final CourseRepository courseRepository;
    private final UserService userService;
    private final S3Service s3Service;


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
    public Page<CommentResponseDto> getDiscussionByCourseId(Long courseId, int page, int size, String sortBy, String sortDirection) {
        if (!courseRepository.existsById(courseId)) {
            throw new RuntimeException("Course not found");
        }

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Sort sort;
        if ("noUpvote".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(direction, "comment.noUpvote");
        } else if ("createdAt".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(direction, "comment.createdAt");
        } else {
            sort = Sort.by(Sort.Direction.DESC, "comment.noUpvote");
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CourseComment> courseComments = courseCommentRepository.findByCourseIdAndCommentCommentReplyIsNull(courseId, pageable);
        Users currentUser = userService.getCurrentUser();

        return courseComments.map(courseComment -> {
            Comment comment = courseComment.getComment();
            CommentResponseDto dto = new CommentResponseDto(comment);
            dto.setReplyId(null);
            dto.setNoReply(commentRepository.countByCommentReply(comment));

            UserResponseDto userDto = new UserResponseDto(comment.getCreatedBy());
            if (userDto.getAvatar() != null && userDto.getAvatar().startsWith("kodeholik")) {
                userDto.setAvatar(s3Service.getPresignedUrl(userDto.getAvatar()));
            }
            dto.setCreatedBy(userDto);
            if (userDto.getId().equals(currentUser.getId())) {
                dto.setUser(true);
                if (isWithinSevenDays(Instant.ofEpochMilli(dto.getCreatedAt()), Instant.now())) {
                    dto.setCanEdit(true);
                }
            }

            dto.setVoted(comment.getUserVote().contains(currentUser));

            return dto;
        });
    }

    public boolean isWithinSevenDays(Instant t1, Instant t2) {
        return ChronoUnit.DAYS.between(t1, t2) <= 7;
    }

    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found", "Comment not found"));
    }

    @Override
    public List<CommentResponseDto> getAllCommentReplyByComment(Long commentId) {
        // Lấy comment gốc (root comment)
        Comment comment = getCommentById(commentId);
        List<Comment> comments = commentRepository.findByCommentReply(comment);


        Users currentUser = userService.getCurrentUser();


        return comments.stream()
                .map(c -> {
                    // Sử dụng constructor của CommentResponseDto
                    CommentResponseDto dto = new CommentResponseDto(c);

                    // Set replyId (ID của root comment)
                    dto.setReplyId(c.getCommentReply().getId());

                    // Đếm số replies của reply (nếu có replies con)
                    dto.setNoReply(commentRepository.countByCommentReply(c));

                    // Set thông tin user và quyền chỉnh sửa
                    UserResponseDto userDto = new UserResponseDto(c.getCreatedBy());
                    dto.setCreatedBy(userDto);
                    if (userDto.getId().equals(currentUser.getId())) {
                        dto.setUser(true);
                        if (isWithinSevenDays(Instant.ofEpochMilli(dto.getCreatedAt()), Instant.now())) {
                            dto.setCanEdit(true);
                        }
                    } else {
                        dto.setUser(false);
                    }

                    dto.setVoted(c.getUserVote().contains(currentUser));

                    return dto;
                })
                .collect(Collectors.toList());
    }

}

