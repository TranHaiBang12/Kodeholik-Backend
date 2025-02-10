package com.g44.kodeholik.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.g44.kodeholik.model.dto.response.discussion.CommentResponseDto;
import com.g44.kodeholik.service.discussion.CommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/problem/{id}")
    public ResponseEntity<Page<CommentResponseDto>> getProblems(
            @PathVariable Long id,
            @RequestParam int page,
            @RequestParam String sortBy,
            @RequestParam boolean ascending) {
        log.info(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        return ResponseEntity.ok(commentService.getCommentsByProblemId(id, page, sortBy, ascending));
    }
}
