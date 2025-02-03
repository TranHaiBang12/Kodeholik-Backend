package com.g44.kodeholik.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.g44.kodeholik.model.dto.response.discussion.CommentResponseDto;
import com.g44.kodeholik.service.discussion.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/problem/{id}")
    public ResponseEntity<List<CommentResponseDto>> getProblems(
            @PathVariable Long id,
            @RequestParam int page,
            @RequestParam String sortBy,
            @RequestParam boolean ascending) {
        return ResponseEntity.ok(commentService.getCommentsByProblemId(id, page, sortBy, ascending));
    }
}
