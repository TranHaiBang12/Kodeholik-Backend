package com.g44.kodeholik.service.discussion.impl;

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

import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.response.discussion.CommentResponseDto;
import com.g44.kodeholik.model.entity.discussion.Comment;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSolution;
import com.g44.kodeholik.repository.discussion.CommentRepository;
import com.g44.kodeholik.service.discussion.CommentService;
import com.g44.kodeholik.service.problem.ProblemService;
import com.g44.kodeholik.service.problem.ProblemSolutionService;
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

    @Override
    public Page<CommentResponseDto> getCommentsByProblemLink(String link, int page, String sortBy, Boolean ascending) {
        Problem problem = problemService.getActivePublicProblemByLink(link);
        Set<Comment> comments = problem.getComments();
        Page<Comment> commentPage;
        if (sortBy != null && ascending != null) {
            Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            commentPage = getCommentsPage(comments, page, 5, sort);
        } else {
            commentPage = getCommentsPage(comments, page, 5, null);
        }
        return commentPage.map(commentResponseMapper::mapFrom);
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
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public Page<CommentResponseDto> getCommentsByProblemSolutionId(Long solutionId, int page, String sortBy,
            Boolean ascending) {
        ProblemSolution problemSolution = problemSolutionService.findSolutionById(solutionId);
        Set<Comment> comments = problemSolution.getComments();
        Page<Comment> commentPage;
        if (sortBy != null && ascending != null) {
            Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            commentPage = getCommentsPage(comments, page, 5, sort);
        } else {
            commentPage = getCommentsPage(comments, page, 5, null);
        }
        return commentPage.map(commentResponseMapper::mapFrom);
    }

}
