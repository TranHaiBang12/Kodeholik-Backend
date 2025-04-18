package com.g44.kodeholik.service.discussion.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
import com.g44.kodeholik.controller.admin.AdminController;
import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.exception.UnauthorizedException;
import com.g44.kodeholik.model.dto.request.comment.AddCommentRequestDto;
import com.g44.kodeholik.model.dto.request.comment.CommentLocation;
import com.g44.kodeholik.model.dto.response.discussion.CommentResponseDto;
import com.g44.kodeholik.model.dto.response.user.UserResponseDto;
import com.g44.kodeholik.model.entity.discussion.Comment;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSolution;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.repository.discussion.CommentRepository;
import com.g44.kodeholik.repository.problem.ProblemRepository;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.service.discussion.CommentService;
import com.g44.kodeholik.service.problem.ProblemService;
import com.g44.kodeholik.service.problem.ProblemSolutionService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.mapper.request.exam.AddExamRequestMapper;
import com.g44.kodeholik.util.mapper.request.user.AddUserAvatarFileMapper;
import com.g44.kodeholik.util.mapper.request.user.AddUserRequestMapper;
import com.g44.kodeholik.util.mapper.response.discussion.CommentResponseMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final AdminController adminController;

    private final AddUserRequestMapper addUserRequestMapper;

    private final AddUserAvatarFileMapper addUserAvatarFileMapper;

    private final AddExamRequestMapper addExamRequestMapper;

    private final CommentRepository commentRepository;

    private final ProblemService problemService;

    private final CommentResponseMapper commentResponseMapper;

    private final ProblemSolutionService problemSolutionService;

    private final UserService userService;

    private final ProblemRepository problemRepository;

    private final MessageProperties messageProperties;

    private final S3Service s3Service;

    @Override
    public Page<CommentResponseDto> getCommentsByProblemLink(String link, int page, String sortBy, Boolean ascending) {
        Sort sort;
        Problem problem = problemService.getActivePublicProblemByLink(link);
        if (sortBy != null && ascending != null && (sortBy.equals("noUpvote") || sortBy.equals("createdAt"))) {
            sort = ascending.booleanValue() ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        } else {
            sort = Sort.by("createdAt").descending();
        }
        Pageable pageable = PageRequest.of(page, 5, sort);
        Users currentUser = userService.getCurrentUser();
        commentResponseMapper.currentUser = currentUser;
        Page<Comment> comments = commentRepository.findByCommentReplyAndProblemsContains(null, problem, pageable);
        Page<CommentResponseDto> commentResponseDtos = comments.map(commentResponseMapper::mapFrom);
        for (CommentResponseDto commentResponseDto : commentResponseDtos) {
            UserResponseDto userResponseDto = commentResponseDto.getCreatedBy();
            if (userResponseDto.getId() == currentUser.getId()) {
                commentResponseDto.setUser(true);
                if (isWithinSevenDays(Instant.ofEpochMilli(commentResponseDto.getCreatedAt()), Instant.now())) {
                    commentResponseDto.setCanEdit(true);
                } else {
                    commentResponseDto.setCanEdit(false);
                }
            } else {
                commentResponseDto.setUser(false);
            }
            commentResponseDto.setNoReply(countCommentReply(commentResponseDto.getId()));

            commentResponseDto.setCreatedBy(userResponseDto);
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

    public Page<Comment> getCommentsPage(List<Comment> comments, int page, int size, Sort sort) {
        // Chuyển Set thành List
        // Tạo Pageable
        Pageable pageable;
        if (sort != null) {
            pageable = PageRequest.of(page, size, sort);
        } else {
            pageable = PageRequest.of(page, size);
        }
        // Tính toán giới hạn trang (pagination)
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), comments.size());
        List<Comment> pagedComments = comments.subList(start, end);

        // Trả về Page<Comment> sử dụng PageImpl
        return new PageImpl<>(pagedComments, pageable, comments.size());
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
        Sort sort;
        if (sortBy != null && ascending != null && (sortBy.equals("noUpvote") || sortBy.equals("createdAt"))) {
            sort = ascending.booleanValue() ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        } else {
            sort = Sort.by("createdAt").descending();
        }
        Pageable pageable = PageRequest.of(page, 5, sort);
        Page<Comment> comments = commentRepository.findByCommentReplyAndProblemSolutionsContains(null, problemSolution,
                pageable);
        Users currentUser = userService.getCurrentUser();
        commentResponseMapper.currentUser = currentUser;
        Page<CommentResponseDto> commentResponseDtos = comments.map(commentResponseMapper::mapFrom);
        for (CommentResponseDto commentResponseDto : commentResponseDtos) {
            UserResponseDto userResponseDto = commentResponseDto.getCreatedBy();
            if (userResponseDto.getId() == currentUser.getId()) {
                commentResponseDto.setUser(true);
                if (isWithinSevenDays(Instant.ofEpochMilli(commentResponseDto.getCreatedAt()), Instant.now())) {
                    commentResponseDto.setCanEdit(true);
                } else {
                    commentResponseDto.setCanEdit(false);
                }
            } else {
                commentResponseDto.setUser(false);
            }
            commentResponseDto.setNoReply(countCommentReply(commentResponseDto.getId()));
            commentResponseDto.setCreatedBy(userResponseDto);

        }
        return commentResponseDtos;
    }

    @Override
    public CommentResponseDto addComment(AddCommentRequestDto addCommentRequestDto) {
        Comment comment = new Comment();
        Users currentUser = userService.getCurrentUser();
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
        CommentResponseDto commentResponseDto = commentResponseMapper.mapFrom(comment);
        UserResponseDto userResponseDto = commentResponseDto.getCreatedBy();
        if (userResponseDto.getId() == currentUser.getId()) {
            commentResponseDto.setUser(true);
            if (isWithinSevenDays(Instant.ofEpochMilli(commentResponseDto.getCreatedAt()), Instant.now())) {
                commentResponseDto.setCanEdit(true);
            } else {
                commentResponseDto.setCanEdit(false);
            }
        } else {
            commentResponseDto.setUser(false);
        }
        commentResponseDto.setNoReply(countCommentReply(commentResponseDto.getId()));
        commentResponseDto.setCreatedBy(userResponseDto);
        return commentResponseDto;
    }

    private void addCommentProblem(AddCommentRequestDto addCommentRequestDto, Comment comment) {
        if (addCommentRequestDto.getLocationId() != null) {
            Problem problem = problemService.getProblemById(addCommentRequestDto.getLocationId());
            Set<Problem> commentProblems = comment.getProblems() != null
                    ? new HashSet<>(comment.getProblems())
                    : new HashSet<>();
            commentProblems.add(problem);
            comment.setProblems(commentProblems);
            commentRepository.save(comment);
        }
    }

    private void addCommentProblemSolution(AddCommentRequestDto addCommentRequestDto, Comment comment) {
        if (addCommentRequestDto.getLocationId() != null) {
            ProblemSolution problemSolution = problemSolutionService
                    .findSolutionById(addCommentRequestDto.getLocationId());
            problemSolution.setNoComment(problemSolution.getNoComment() + 1);
            Set<ProblemSolution> commentSolutions = comment.getProblemSolutions() != null
                    ? new HashSet<>(comment.getProblemSolutions())
                    : new HashSet<>();
            commentSolutions.add(problemSolution);
            comment.setProblemSolutions(commentSolutions);
            problemSolutionService.save(problemSolution);
            commentRepository.save(comment);

        }
    }

    @Override
    public CommentResponseDto editComment(Long commentId, String newComment) {
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
        comment.setComment(newComment);
        comment.setUpdatedAt(Timestamp.from(Instant.now()));
        comment.setUpdatedBy(currentUser);

        commentRepository.save(comment);
        CommentResponseDto commentResponseDto = commentResponseMapper.mapFrom(comment);
        UserResponseDto userResponseDto = commentResponseDto.getCreatedBy();
        if (userResponseDto.getId() == currentUser.getId()) {
            commentResponseDto.setUser(true);
            if (isWithinSevenDays(Instant.ofEpochMilli(commentResponseDto.getCreatedAt()), Instant.now())) {
                commentResponseDto.setCanEdit(true);
            } else {
                commentResponseDto.setCanEdit(false);
            }
        } else {
            commentResponseDto.setUser(false);
        }
        commentResponseDto.setNoReply(countCommentReply(commentResponseDto.getId()));
        commentResponseDto.setCreatedBy(userResponseDto);
        return commentResponseDto;
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

        Iterator<Users> iterator = usersVote.iterator();
        while (iterator.hasNext()) {
            Users userVote = iterator.next();
            if (userVote.getEmail().equals(currentUser.getEmail())) {
                isVote = true;
                iterator.remove();
                if (comment.getNoUpvote() > 0) {
                    comment.setNoUpvote(comment.getNoUpvote() - 1);
                }
                break;
            }
        }

        if (!isVote) {
            throw new BadRequestException("You have not voted for this comment",
                    "You have not voted for this comment");
        }

        commentRepository.save(comment);
    }

    @Override
    public List<CommentResponseDto> getAllCommentReplyByComment(Long commentId) {
        Comment comment = getCommentById(commentId);
        List<Comment> comments = commentRepository.findByCommentReply(comment);

        Users currentUser = userService.getCurrentUser();
        commentResponseMapper.currentUser = currentUser;
        List<CommentResponseDto> commentResponseDtos = comments.stream()
                .map(commentResponseMapper::mapFrom)
                .collect(Collectors.toList());
        for (CommentResponseDto commentResponseDto : commentResponseDtos) {
            UserResponseDto userResponseDto = commentResponseDto.getCreatedBy();
            if (userResponseDto.getId() == currentUser.getId()) {
                commentResponseDto.setUser(true);
                if (isWithinSevenDays(Instant.ofEpochMilli(commentResponseDto.getCreatedAt()), Instant.now())) {
                    commentResponseDto.setCanEdit(true);
                } else {
                    commentResponseDto.setCanEdit(false);
                }
            } else {
                commentResponseDto.setUser(false);
            }
            commentResponseDto.setNoReply(countCommentReply(commentResponseDto.getId()));
            commentResponseDto.setCreatedBy(userResponseDto);

        }
        return commentResponseDtos;
    }

    @Override
    public int countCommentReply(Long commentId) {
        Comment comment = getCommentById(commentId);
        return commentRepository.countByCommentReply(comment);
    }

    @Override
    public Page<CommentResponseDto> getCommentsByCourseId(Long courseId, int page, String sortBy, Boolean ascending) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCommentsByCourseId'");
    }
}
