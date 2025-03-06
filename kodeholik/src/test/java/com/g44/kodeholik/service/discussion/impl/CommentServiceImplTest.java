package com.g44.kodeholik.service.discussion.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import com.g44.kodeholik.config.MessageProperties;
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
import com.g44.kodeholik.util.mapper.response.discussion.CommentResponseMapper;
import com.g44.kodeholik.service.problem.ProblemService;
import com.g44.kodeholik.service.problem.ProblemSolutionService;
import com.g44.kodeholik.service.user.UserService;

class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl commentService;

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

    @Mock
    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCommentsByProblemLink() {
        String link = "test-link";
        int page = 0;
        String sortBy = "createdAt";
        Boolean ascending = true;

        Problem problem = new Problem();
        Comment comment = new Comment();
        List<Comment> commentList = Collections.singletonList(comment);
        Page<Comment> commentPage = new PageImpl<>(commentList);
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setAvatar("avatar-url");
        commentResponseDto.setCreatedBy(userResponseDto);

        when(problemService.getActivePublicProblemByLink(link)).thenReturn(problem);
        when(commentRepository.findByCommentReplyAndProblemsContains(null, problem,
                PageRequest.of(page, 5, Sort.by(sortBy).ascending()))).thenReturn(commentPage);
        when(commentResponseMapper.mapFrom(comment)).thenReturn(commentResponseDto);
        when(s3Service.getPresignedUrl("avatar-url")).thenReturn("presigned-avatar-url");

        Page<CommentResponseDto> result = commentService.getCommentsByProblemLink(link, page, sortBy, ascending);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("presigned-avatar-url", result.getContent().get(0).getCreatedBy().getAvatar());
    }

    @Test
    void testIsUserVoteComment() {
        Long commentId = 1L;
        Users currentUser = new Users();
        currentUser.setEmail("test@example.com");
        Comment comment = new Comment();
        Users userVote = new Users();
        userVote.setEmail("test@example.com");
        Set<Users> usersVote = new HashSet<>();
        usersVote.add(userVote);
        comment.setUserVote(usersVote);

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        boolean result = commentService.isUserVoteComment(commentId);

        assertTrue(result);
    }

    @Test
    void testGetCommentById() {
        Long commentId = 1L;
        Comment comment = new Comment();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        Comment result = commentService.getCommentById(commentId);

        assertNotNull(result);
        assertEquals(comment, result);
    }

    @Test
    void testSaveComment() {
        Comment comment = new Comment();

        commentService.saveComment(comment);

        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void testAddCommentProblem() {
        AddCommentRequestDto addCommentRequestDto = new AddCommentRequestDto();
        addCommentRequestDto.setComment("Test comment");
        addCommentRequestDto.setLocation(CommentLocation.PROBLEM);
        addCommentRequestDto.setLocationId(1L);

        Users currentUser = new Users();
        Problem problem = new Problem();

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(problemService.getProblemById(1L)).thenReturn(problem);

        commentService.addComment(addCommentRequestDto);

        verify(commentRepository, times(2)).save(any(Comment.class));
    }

    @Test
    void testAddCommentProblemWithReply() {
        AddCommentRequestDto addCommentRequestDto = new AddCommentRequestDto();
        addCommentRequestDto.setComment("Test comment");
        addCommentRequestDto.setLocation(CommentLocation.PROBLEM);
        addCommentRequestDto.setLocationId(1L);
        addCommentRequestDto.setCommentReply(2L);

        Comment comment = new Comment();
        comment.setId(2L);

        Users currentUser = new Users();
        Problem problem = new Problem();

        when(commentRepository.findById(2L)).thenReturn(Optional.of(comment));
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(problemService.getProblemById(1L)).thenReturn(problem);

        commentService.addComment(addCommentRequestDto);

        verify(commentRepository, times(2)).save(any(Comment.class));
    }

    @Test
    void testAddCommentProblemWithReplyNotFound() {
        AddCommentRequestDto addCommentRequestDto = new AddCommentRequestDto();
        addCommentRequestDto.setComment("Test comment");
        addCommentRequestDto.setLocation(CommentLocation.PROBLEM);
        addCommentRequestDto.setLocationId(1L);
        addCommentRequestDto.setCommentReply(2L);

        Comment comment = new Comment();
        comment.setId(2L);

        Users currentUser = new Users();
        Problem problem = new Problem();

        when(commentRepository.findById(2L)).thenReturn(Optional.empty());
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(problemService.getProblemById(1L)).thenReturn(problem);

        NotFoundException notFoundException = assertThrows(
                NotFoundException.class,
                () -> commentService.addComment(addCommentRequestDto));
        assertEquals("Comment not found", notFoundException.getMessage());
        assertEquals("Comment not found", notFoundException.getDetails());
    }

    @Test
    void testAddCommentProblemSolution() {
        AddCommentRequestDto addCommentRequestDto = new AddCommentRequestDto();
        addCommentRequestDto.setComment("Test comment");
        addCommentRequestDto.setLocation(CommentLocation.SOLUTION);
        addCommentRequestDto.setLocationId(1L);

        Users currentUser = new Users();
        ProblemSolution problemSolution = new ProblemSolution();

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(problemSolutionService.findSolutionById(1L)).thenReturn(problemSolution);

        commentService.addComment(addCommentRequestDto);

        verify(commentRepository, times(2)).save(any(Comment.class));
        verify(problemSolutionService, times(1)).save(any(ProblemSolution.class));
    }

    @Test
    void testAddCommentProblemSolutionWithReply() {
        AddCommentRequestDto addCommentRequestDto = new AddCommentRequestDto();
        addCommentRequestDto.setComment("Test comment");
        addCommentRequestDto.setLocation(CommentLocation.SOLUTION);
        addCommentRequestDto.setLocationId(1L);
        addCommentRequestDto.setCommentReply(2L);

        Comment comment = new Comment();
        comment.setId(2L);

        Users currentUser = new Users();
        ProblemSolution problemSolution = new ProblemSolution();

        when(commentRepository.findById(2L)).thenReturn(Optional.of(comment));
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(problemSolutionService.findSolutionById(1L)).thenReturn(problemSolution);

        commentService.addComment(addCommentRequestDto);

        verify(commentRepository, times(2)).save(any(Comment.class));
        verify(problemSolutionService, times(1)).save(any(ProblemSolution.class));
    }

    @Test
    void testAddCommentProblemSolutionWithReplyNotFound() {
        AddCommentRequestDto addCommentRequestDto = new AddCommentRequestDto();
        addCommentRequestDto.setComment("Test comment");
        addCommentRequestDto.setLocation(CommentLocation.SOLUTION);
        addCommentRequestDto.setLocationId(1L);
        addCommentRequestDto.setCommentReply(2L);

        Comment comment = new Comment();
        comment.setId(2L);

        Users currentUser = new Users();
        ProblemSolution problemSolution = new ProblemSolution();

        when(commentRepository.findById(2L)).thenReturn(Optional.empty());
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(problemSolutionService.findSolutionById(1L)).thenReturn(problemSolution);

        NotFoundException notFoundException = assertThrows(
                NotFoundException.class,
                () -> commentService.addComment(addCommentRequestDto));
        assertEquals("Comment not found", notFoundException.getMessage());
        assertEquals("Comment not found", notFoundException.getDetails());
    }

    @Test
    void testEditComment() {
        Long commentId = 1L;
        String newComment = "\"Updated comment\"";
        Comment comment = new Comment();
        comment.setCreatedAt(Timestamp.from(Instant.now()));
        Users currentUser = new Users();
        currentUser.setId(1L);
        comment.setCreatedBy(currentUser);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(userService.getCurrentUser()).thenReturn(currentUser);

        commentService.editComment(commentId, newComment);

        assertEquals(newComment.substring(1, newComment.length() - 1), comment.getComment());
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void testEditCommentWithEmptyComment() {
        Long commentId = 1L;
        String newComment = "";
        Comment comment = new Comment();
        comment.setCreatedAt(Timestamp.from(Instant.now()));
        Users currentUser = new Users();
        currentUser.setId(1L);
        comment.setCreatedBy(currentUser);

        String message = "Comment must be 0-5000 character long";

        when(messageProperties.getMessage("MSG17")).thenReturn(message);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(userService.getCurrentUser()).thenReturn(currentUser);

        BadRequestException badRequestException = assertThrows(
                BadRequestException.class,
                () -> commentService.editComment(commentId, newComment));
        assertEquals(message, badRequestException.getMessage());
        assertEquals(message, badRequestException.getDetails());
    }

    @Test
    void testEditCommentExpired() {
        Long commentId = 1L;
        String newComment = "sada";
        Comment comment = new Comment();
        comment.setCreatedAt(new Timestamp(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 8));
        Users currentUser = new Users();
        currentUser.setId(1L);
        comment.setCreatedBy(currentUser);
        String message = "Comment can only be edited in 7 days";

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(userService.getCurrentUser()).thenReturn(currentUser);
        assertThrows(
                BadRequestException.class,
                () -> commentService.editComment(commentId, newComment));

        BadRequestException badRequestException = assertThrows(
                BadRequestException.class,
                () -> commentService.editComment(commentId, newComment));
        assertEquals(message, badRequestException.getMessage());
        assertEquals(message, badRequestException.getDetails());
    }

    @Test
    void testEditCommentNotCurrentUserCreated() {
        Long commentId = 1L;
        String newComment = "sa";
        Comment comment = new Comment();
        comment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        Users currentUser = new Users();
        currentUser.setId(1L);

        Users user = new Users();
        user.setId(2L);
        comment.setCreatedBy(user);

        String message = "You are not the owner of this comment";

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(userService.getCurrentUser()).thenReturn(currentUser);
        UnauthorizedException unauthorizedException = assertThrows(
                UnauthorizedException.class,
                () -> commentService.editComment(commentId, newComment));
        assertEquals(message, unauthorizedException.getMessage());
        assertEquals(message, unauthorizedException.getDetails());
    }

    @Test
    void testUpvoteComment() {
        Long commentId = 1L;
        Comment comment = new Comment();
        Users currentUser = new Users();
        currentUser.setEmail("test@example.com");
        comment.setUserVote(new HashSet<>());

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(userService.getCurrentUser()).thenReturn(currentUser);

        commentService.upvoteComment(commentId);

        assertEquals(1, comment.getNoUpvote());
        assertTrue(comment.getUserVote().contains(currentUser));
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void testUnupvoteComment() {
        Long commentId = 1L;
        Comment comment = new Comment();
        Users currentUser = new Users();
        currentUser.setEmail("test@example.com");
        Set<Users> usersVote = new HashSet<>();
        usersVote.add(currentUser);
        comment.setUserVote(usersVote);
        comment.setNoUpvote(1);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(userService.getCurrentUser()).thenReturn(currentUser);

        commentService.unupvoteComment(commentId);

        assertEquals(0, comment.getNoUpvote());
        assertFalse(comment.getUserVote().contains(currentUser));
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void testGetAllCommentReplyByComment() {
        Long commentId = 1L;
        Comment comment = new Comment();
        Comment replyComment = new Comment();
        List<Comment> comments = Collections.singletonList(replyComment);
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setAvatar("avatar-url");
        commentResponseDto.setCreatedBy(userResponseDto);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.findByCommentReply(comment)).thenReturn(comments);
        when(commentResponseMapper.mapFrom(replyComment)).thenReturn(commentResponseDto);
        when(s3Service.getPresignedUrl("avatar-url")).thenReturn("presigned-avatar-url");

        List<CommentResponseDto> result = commentService.getAllCommentReplyByComment(commentId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("presigned-avatar-url", result.get(0).getCreatedBy().getAvatar());
    }

    @Test
    void testCountCommentReply() {
        Long commentId = 1L;
        Comment comment = new Comment();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.countByCommentReply(comment)).thenReturn(5);

        int result = commentService.countCommentReply(commentId);

        assertEquals(5, result);
    }
}