package com.g44.kodeholik.service.discussion.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import com.g44.kodeholik.model.dto.response.discussion.CommentResponseDto;
import com.g44.kodeholik.model.entity.discussion.Comment;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.repository.discussion.CommentRepository;
import com.g44.kodeholik.service.problem.ProblemService;
import com.g44.kodeholik.util.mapper.response.discussion.CommentResponseMapper;

public class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ProblemService problemService;

    @Mock
    private CommentResponseMapper commentResponseMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCommentsByProblemId() {
        Long problemId = 1L;
        int page = 0;
        String sortBy = "createdAt";
        boolean ascending = true;

        Problem problem = new Problem();
        Set<Comment> comments = new HashSet<>();
        Comment comment = new Comment();
        comments.add(comment);
        problem.setComments(comments);

        when(problemService.getProblemById(problemId)).thenReturn(problem);
        when(commentResponseMapper.mapFrom(any(Comment.class))).thenReturn(new CommentResponseDto());

        Page<CommentResponseDto> result = commentService.getCommentsByProblemId(problemId, page, sortBy, ascending);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(problemService, times(1)).getProblemById(problemId);
    }

    @Test
    public void testGetCommentsPage() {
        Set<Comment> comments = new HashSet<>();
        Comment comment = new Comment();
        comments.add(comment);

        int page = 0;
        int size = 5;
        Sort sort = Sort.by("createdAt").ascending();

        Page<Comment> result = commentService.getCommentsPage(comments, page, size, sort);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
}