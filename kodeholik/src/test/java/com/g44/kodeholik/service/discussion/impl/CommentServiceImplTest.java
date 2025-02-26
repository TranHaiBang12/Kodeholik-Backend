package com.g44.kodeholik.service.discussion.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import com.g44.kodeholik.config.MessageProperties;
import com.g44.kodeholik.model.dto.request.comment.AddCommentRequestDto;
import com.g44.kodeholik.model.dto.request.comment.CommentLocation;
import com.g44.kodeholik.model.dto.response.discussion.CommentResponseDto;
import com.g44.kodeholik.model.entity.discussion.Comment;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.repository.discussion.CommentRepository;
import com.g44.kodeholik.repository.problem.ProblemRepository;
import com.g44.kodeholik.service.problem.ProblemService;
import com.g44.kodeholik.service.problem.ProblemSolutionService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.mapper.response.discussion.CommentResponseMapper;

class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ProblemService problemService;

    @Mock
    private CommentResponseMapper commentResponseMapper;

    @Mock
    private ProblemSolutionService problemSolutionService;

    @Mock
    private UserService userService;

    @Mock
    private ProblemRepository problemRepository;

    @Mock
    private MessageProperties messageProperties;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCommentsByProblemLink() {
        Problem problem = new Problem();
        problem.setComments(new HashSet<>());
        when(problemService.getActivePublicProblemByLink(anyString())).thenReturn(problem);

        Page<CommentResponseDto> result = commentService.getCommentsByProblemLink("link", 0, null, null);

        assertNotNull(result);
        verify(problemService).getActivePublicProblemByLink(anyString());
    }

    @Test
    void testIsUserVoteComment() {
        Users user = new Users();
        user.setEmail("test@example.com");
        when(userService.getCurrentUser()).thenReturn(user);

        Comment comment = new Comment();
        comment.setUserVote(new HashSet<>(Collections.singletonList(user)));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        boolean result = commentService.isUserVoteComment(1L);

        assertTrue(result);
        verify(userService).getCurrentUser();
    }

    @Test
    void testAddComment() {
        AddCommentRequestDto requestDto = new AddCommentRequestDto();
        requestDto.setComment("Test comment");
        requestDto.setLocation(CommentLocation.PROBLEM);
        requestDto.setLocationId(1L);

        Users user = new Users();
        when(userService.getCurrentUser()).thenReturn(user);

        Problem problem = new Problem();
        when(problemService.getProblemById(anyLong())).thenReturn(problem);

        commentService.addComment(requestDto);

        verify(commentRepository).save(any(Comment.class));
        verify(problemRepository).save(any(Problem.class));
    }

    @Test
    void testEditComment() {
        Comment comment = new Comment();
        comment.setCreatedAt(Timestamp.from(Instant.now()));
        Users user = new Users();
        user.setId(1L);
        comment.setCreatedBy(user);

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(userService.getCurrentUser()).thenReturn(user);

        commentService.editComment(1L, "New comment");

        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void testUpvoteComment() {
        Comment comment = new Comment();
        comment.setUserVote(new HashSet<>());
        Users user = new Users();
        user.setEmail("test@example.com");

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(userService.getCurrentUser()).thenReturn(user);

        commentService.upvoteComment(1L);

        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void testUnupvoteComment() {
        Users user = new Users();
        user.setEmail("test@example.com");

        Comment comment = new Comment();
        comment.setUserVote(new HashSet<>(Collections.singletonList(user)));

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(userService.getCurrentUser()).thenReturn(user);

        commentService.unupvoteComment(1L);

        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void testGetAllCommentReplyByComment() {
        Comment comment = new Comment();
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(commentRepository.findByCommentReply(any(Comment.class))).thenReturn(Collections.emptyList());

        List<CommentResponseDto> result = commentService.getAllCommentReplyByComment(1L);

        assertNotNull(result);
        verify(commentRepository).findByCommentReply(any(Comment.class));
    }

    @Test
    void testCountCommentReply() {
        Comment comment = new Comment();
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(commentRepository.countByCommentReply(any(Comment.class))).thenReturn(5);

        int result = commentService.countCommentReply(1L);

        assertEquals(5, result);
        verify(commentRepository).countByCommentReply(any(Comment.class));
    }
}