package com.g44.kodeholik.service.discussion.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.model.dto.response.discussion.CommentResponseDto;
import com.g44.kodeholik.model.entity.discussion.Comment;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.setting.Skill;
import com.g44.kodeholik.repository.discussion.CommentRepository;
import com.g44.kodeholik.service.discussion.CommentService;
import com.g44.kodeholik.service.problem.ProblemService;
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

    @Override
    public Page<CommentResponseDto> getCommentsByProblemId(Long problemId, int page, String sortBy, Boolean ascending) {
        Problem problem = problemService.getProblemById(problemId);
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

}
