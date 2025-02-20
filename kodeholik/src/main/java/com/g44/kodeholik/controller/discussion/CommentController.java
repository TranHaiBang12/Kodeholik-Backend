package com.g44.kodeholik.controller.discussion;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/problem/{link}")
    public ResponseEntity<Page<CommentResponseDto>> getCommentProblem(
            @PathVariable String link,
            @RequestParam int page,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) Boolean ascending) {
        Page<CommentResponseDto> commentPage = commentService.getCommentsByProblemLink(link, page, sortBy, ascending);
        if (commentPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(commentPage);
    }

    @GetMapping("/problem-solution/{id}")
    public ResponseEntity<Page<CommentResponseDto>> getCommentProblemSolution(
            @PathVariable Long id,
            @RequestParam int page,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) Boolean ascending) {
        Page<CommentResponseDto> commentPage = commentService.getCommentsByProblemSolutionId(id, page, sortBy,
                ascending);
        if (commentPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(commentPage);
    }
}
