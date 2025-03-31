package com.g44.kodeholik.controller.discussion;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.g44.kodeholik.model.dto.request.comment.AddCommentRequestDto;
import com.g44.kodeholik.model.dto.response.discussion.CommentResponseDto;
import com.g44.kodeholik.service.discussion.CommentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.*;

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

    @PostMapping("/post")
    public ResponseEntity<CommentResponseDto> postComment(
            @RequestBody @Valid AddCommentRequestDto addCommentRequestDto) {
        return new ResponseEntity<>(commentService.addComment(addCommentRequestDto), HttpStatus.CREATED);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<CommentResponseDto> editComment(@PathVariable Long id, @RequestBody String newComment) {
        commentService.editComment(id, newComment);
        return new ResponseEntity<>(commentService.editComment(id, newComment), HttpStatus.OK);
    }

    @PutMapping("/upvote/{id}")
    public ResponseEntity<Void> upvoteComment(@PathVariable Long id) {
        commentService.upvoteComment(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/unupvote/{id}")
    public ResponseEntity<Void> unupvoteComment(@PathVariable Long id) {
        commentService.unupvoteComment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list-reply/{id}")
    public ResponseEntity<List<CommentResponseDto>> getListReplyByCommentId(@PathVariable Long id) {
        if (commentService.getAllCommentReplyByComment(id).isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(commentService.getAllCommentReplyByComment(id));
    }

}
