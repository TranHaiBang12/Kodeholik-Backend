package com.g44.kodeholik.service.discussion.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.config.MessageProperties;
import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.exception.UnauthorizedException;
import com.g44.kodeholik.model.dto.request.comment.AddCommentRequestDto;
import com.g44.kodeholik.model.dto.request.comment.CommentLocation;
import com.g44.kodeholik.model.dto.response.discussion.CommentResponseDto;
import com.g44.kodeholik.model.entity.discussion.Comment;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSolution;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.repository.discussion.CommentRepository;
import com.g44.kodeholik.repository.problem.ProblemRepository;
import com.g44.kodeholik.service.discussion.CommentService;
import com.g44.kodeholik.service.problem.ProblemService;
import com.g44.kodeholik.service.problem.ProblemSolutionService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.mapper.response.discussion.CommentResponseMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final ProblemService problemService;

    private final CommentResponseMapper commentResponseMapper;

    private final ProblemSolutionService problemSolutionService;

    private final UserService userService;

    private final ProblemRepository problemRepository;

    private final MessageProperties messageProperties;

    @Override
    public Page<CommentResponseDto> getCommentsByProblemLink(String link, int page, String sortBy, Boolean ascending) {
        Problem problem = problemService.getActivePublicProblemByLink(link);
        Set<Comment> comments = problem.getComments();
        Page<Comment> commentPage;
        if (sortBy != null && ascending != null && (sortBy.equals("noUpvote") || sortBy.equals("createdAt"))) {
            Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            commentPage = getCommentsPage(comments, page, 5, sort);
        } else {
            Sort sort = ascending ? Sort.by("createdAt").ascending() : Sort.by("createdAt").descending();
            commentPage = getCommentsPage(comments, page, 5, sort);
        }
        Page<CommentResponseDto> commentResponseDtos = commentPage.map(commentResponseMapper::mapFrom);
        for (CommentResponseDto commentResponseDto : commentResponseDtos) {
            commentResponseDto.setVoted(isUserVoteComment(commentResponseDto.getId()));
            commentResponseDto.setNoReply(countCommentReply(commentResponseDto.getId()));

        }
        return commentResponseDtos;
    }

    @Override
    public boolean isUserVoteComment(Long commentId) {
        Users currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            Set<Users> usersVote = getCommentById(commentId).getUserVote();
            for (Users userVote : usersVote) {
                if (userVote.getEmail().equals(currentUser.getEmail())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Page<Comment> getCommentsPage(Set<Comment> comments, int page, int size, Sort sort) {
        // Chuyển Set thành List
        List<Comment> commentList = comments.stream().collect(Collectors.toList());

        // Tạo Pageable
        Pageable pageable;
        if (sort != null) {
            pageable = PageRequest.of(page, size, sort);
        } else {
            pageable = PageRequest.of(page, size);
        }
        // Tính toán giới hạn trang (pagination)
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), commentList.size());
        List<Comment> pagedComments = commentList.subList(start, end);

        // Trả về Page<Comment> sử dụng PageImpl
        return new PageImpl<>(pagedComments, pageable, commentList.size());
    }

    @Override
    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found", "Comment not found"));
    }

    @Override
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public Page<CommentResponseDto> getCommentsByProblemSolutionId(Long solutionId, int page, String sortBy,
            Boolean ascending) {
        ProblemSolution problemSolution = problemSolutionService.findSolutionById(solutionId);
        Set<Comment> comments = problemSolution.getComments();
        Page<Comment> commentPage;
        if (sortBy != null && ascending != null && (sortBy.equals("noUpvote") || sortBy.equals("createdAt"))) {
            Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            commentPage = getCommentsPage(comments, page, 5, sort);
        } else {
            Sort sort = ascending ? Sort.by("createdAt").ascending() : Sort.by("createdAt").descending();
            commentPage = getCommentsPage(comments, page, 5, sort);
        }
        Page<CommentResponseDto> commentResponseDtos = commentPage.map(commentResponseMapper::mapFrom);
        for (CommentResponseDto commentResponseDto : commentResponseDtos) {
            commentResponseDto.setVoted(isUserVoteComment(commentResponseDto.getId()));
            commentResponseDto.setNoReply(countCommentReply(commentResponseDto.getId()));
        }
        return commentResponseDtos;
    }

    @Override
    public void addComment(AddCommentRequestDto addCommentRequestDto) {
        Comment comment = new Comment();
        comment.setComment(addCommentRequestDto.getComment());
        if (addCommentRequestDto.getCommentReply() != null)
            comment.setCommentReply(getCommentById(addCommentRequestDto.getCommentReply()));
        comment.setCreatedAt(Timestamp.from(Instant.now()));
        comment.setCreatedBy(userService.getCurrentUser());
        commentRepository.save(comment);

        if (addCommentRequestDto.getLocation() == CommentLocation.PROBLEM) {
            addCommentProblem(addCommentRequestDto, comment);
        } else if (addCommentRequestDto.getLocation() == CommentLocation.SOLUTION) {
            addCommentProblemSolution(addCommentRequestDto, comment);
        }
    }

    private void addCommentProblem(AddCommentRequestDto addCommentRequestDto, Comment comment) {
        if (addCommentRequestDto.getLocationId() != null) {
            Problem problem = problemService.getProblemById(addCommentRequestDto.getLocationId());
            problem.getComments().add(comment);
            problemRepository.save(problem);
        }
    }

    private void addCommentProblemSolution(AddCommentRequestDto addCommentRequestDto, Comment comment) {
        if (addCommentRequestDto.getLocationId() != null) {
            ProblemSolution problemSolution = problemSolutionService
                    .findSolutionById(addCommentRequestDto.getLocationId());
            problemSolution.setNoComment(problemSolution.getNoComment() + 1);
            problemSolution.getComments().add(comment);
            problemSolutionService.save(problemSolution);
        }
    }

    @Override
    public void editComment(Long commentId, String newComment) {
        Comment comment = getCommentById(commentId);
        Timestamp updatedAt;
        Users currentUser = userService.getCurrentUser();

        if (comment.getUpdatedAt() != null) {
            updatedAt = comment.getUpdatedAt();
        } else {
            updatedAt = comment.getCreatedAt();
        }
        if (newComment == null || newComment.equals("")) {
            throw new BadRequestException(messageProperties.getMessage("MSG17"), messageProperties.getMessage("MSG17"));
        }
        if (currentUser.getId().intValue() != comment.getCreatedBy().getId().intValue()) {
            throw new UnauthorizedException("You are not the owner of this comment",
                    "You are not the owner of this comment");
        }
        if (!isWithinSevenDays(updatedAt.toInstant(), Instant.now())) {
            throw new BadRequestException("Comment can only be edited in 7 days",
                    "Comment can only be edited in 7 days");
        }
        comment.setComment(newComment.substring(1, newComment.length() - 1));
        comment.setUpdatedAt(Timestamp.from(Instant.now()));
        comment.setUpdatedBy(currentUser);

        commentRepository.save(comment);
    }

    public boolean isWithinSevenDays(Instant t1, Instant t2) {
        return ChronoUnit.DAYS.between(t1, t2) <= 7;
    }

    @Override
    public void upvoteComment(Long commentId) {
        Comment comment = getCommentById(commentId);
        Set<Users> usersVote = comment.getUserVote();
        Users currentUser = userService.getCurrentUser();

        for (Users userVote : usersVote) {
            if (userVote.getEmail().equals(currentUser.getEmail())) {
                throw new BadRequestException("You have already voted this comment",
                        "You have already voted this comment");
            }
        }

        usersVote.add(currentUser);
        comment.setNoUpvote(comment.getNoUpvote() + 1);
        commentRepository.save(comment);
    }

    @Override
    public void unupvoteComment(Long commentId) {
        Comment comment = getCommentById(commentId);
        Set<Users> usersVote = comment.getUserVote();
        Users currentUser = userService.getCurrentUser();
        boolean isVote = false;

        for (Users userVote : usersVote) {
            if (userVote.getEmail().equals(currentUser.getEmail())) {
                isVote = true;
                usersVote.remove(currentUser);
                if (comment.getNoUpvote() > 0)
                    comment.setNoUpvote(comment.getNoUpvote() + 1);
                commentRepository.save(comment);
            }
        }
        if (!isVote) {
            throw new BadRequestException("You have not vote this comment",
                    "You have not vote this comment");
        }

    }

    @Override
    public List<CommentResponseDto> getAllCommentReplyByComment(Long commentId) {
        Comment comment = getCommentById(commentId);
        List<Comment> comments = commentRepository.findByCommentReply(comment);
        return comments.stream()
                .map(commentResponseMapper::mapFrom)
                .collect(Collectors.toList());
    }

    @Override
    public int countCommentReply(Long commentId) {
        Comment comment = getCommentById(commentId);
        return commentRepository.countByCommentReply(comment);
    }
}
