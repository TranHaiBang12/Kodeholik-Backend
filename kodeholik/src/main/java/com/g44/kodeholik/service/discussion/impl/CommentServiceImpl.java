package com.g44.kodeholik.service.discussion.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

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

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final ProblemService problemService;

    private final CommentResponseMapper commentResponseMapper;

    @Override
    public List<CommentResponseDto> getCommentsByProblemId(Long problemId, int page, String sortBy, boolean ascending) {
        Problem problem = problemService.getProblemById(problemId);
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, 5, sort);
        Page<Comment> comments = commentRepository.findByProblemListContaining(problem, pageable);
        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
        commentResponseDtos = comments.stream()
                .map(commentResponseMapper::mapFrom)
                .collect(Collectors.toList());
        return commentResponseDtos;
    }

}
