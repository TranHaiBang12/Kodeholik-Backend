package com.g44.kodeholik.service.exam.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;

import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.exam.AddExamRequestDto;
import com.g44.kodeholik.model.dto.request.exam.ExamProblemRequestDto;
import com.g44.kodeholik.model.dto.request.exam.FilterExamRequestDto;
import com.g44.kodeholik.model.dto.response.exam.examiner.ExamListResponseDto;
import com.g44.kodeholik.model.dto.response.exam.examiner.ExamResponseDto;
import com.g44.kodeholik.model.dto.response.exam.student.ExamProblemDetailResponseDto;
import com.g44.kodeholik.model.dto.response.exam.student.ExamResultOverviewResponseDto;
import com.g44.kodeholik.model.entity.exam.Exam;
import com.g44.kodeholik.model.entity.exam.ExamParticipant;
import com.g44.kodeholik.model.entity.exam.ExamSubmission;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.exam.ExamStatus;
import com.g44.kodeholik.model.enums.user.UserRole;
import com.g44.kodeholik.repository.*;
import com.g44.kodeholik.repository.exam.ExamParticipantRepository;
import com.g44.kodeholik.repository.exam.ExamProblemRepository;
import com.g44.kodeholik.repository.exam.ExamRepository;
import com.g44.kodeholik.repository.exam.ExamSubmissionRepository;
import com.g44.kodeholik.service.*;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.service.email.EmailService;
import com.g44.kodeholik.service.problem.ProblemService;
import com.g44.kodeholik.service.problem.ProblemSubmissionService;
import com.g44.kodeholik.service.problem.ProblemTestCaseService;
import com.g44.kodeholik.service.publisher.Publisher;
import com.g44.kodeholik.service.redis.RedisService;
import com.g44.kodeholik.service.scheduler.ExamStartEvent;
import com.g44.kodeholik.service.setting.LanguageService;
import com.g44.kodeholik.service.token.TokenService;
import com.g44.kodeholik.service.user.NotificationService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.mapper.request.exam.AddExamRequestMapper;
import com.g44.kodeholik.util.mapper.request.exam.EditExamBasicRequestMapper;
import com.g44.kodeholik.util.mapper.response.exam.ExamListResponseMapper;
import com.g44.kodeholik.util.mapper.response.exam.ExamListStudentResponseMapper;
import com.g44.kodeholik.util.mapper.response.exam.ExamResponseMapper;
import com.g44.kodeholik.util.mapper.response.exam.NotStartedExamListMapper;

class ExamServiceImplTest {

    @InjectMocks
    private ExamServiceImpl examService;

    @Mock
    private ExamRepository examRepository;

    @Mock
    private ExamProblemRepository examProblemRepository;

    @Mock
    private ExamParticipantRepository examParticipantRepository;

    @Mock
    private AddExamRequestMapper addExamRequestMapper;

    @Mock
    private ExamResponseMapper examResponseMapper;

    @Mock
    private ExamListResponseMapper examListResponseMapper;

    @Mock
    private LanguageService languageService;

    @Mock
    private ProblemService problemService;

    @Mock
    private UserService userService;

    @Mock
    private TokenService tokenService;

    @Mock
    private ProblemTestCaseService problemTestCaseService;

    @Mock
    private ExamSubmissionRepository examSubmissionRepository;

    @Mock
    private ProblemSubmissionService problemSubmissionService;

    @Mock
    private Publisher publisher;

    @Mock
    private ExamListStudentResponseMapper examListStudentResponseMapper;

    @Mock
    private S3Service s3Service;

    @Mock
    private EmailService emailService;

    @Mock
    private RedisService redisService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private NotStartedExamListMapper notStartedExamListMapper;

    @Mock
    private EditExamBasicRequestMapper editExamBasicRequestMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateExamSuccess() {

        Language language = new Language();
        language.setName("Java");

        Problem problem = new Problem();
        problem.setLanguageSupport(Set.of(language));

        ExamProblemRequestDto examProblemRequestDto = new ExamProblemRequestDto();

        List<ExamProblemRequestDto> examProblemRequestDtos = List.of(examProblemRequestDto);

        AddExamRequestDto addExamRequestDto = new AddExamRequestDto();
        addExamRequestDto.setStartTime(Timestamp.from(Instant.now().plusSeconds(3600 + 1000 * 60 * 60 * 7)));
        addExamRequestDto.setEndTime(Timestamp.from(Instant.now().plusSeconds(7200 + 1000 * 60 * 60 * 7)));
        addExamRequestDto.setLanguageSupports(Collections.singletonList("Java"));
        addExamRequestDto.setProblemRequests(examProblemRequestDtos);

        Exam exam = new Exam();
        exam.setStartTime(Timestamp.from(Instant.now().plusSeconds(3600 + 1000 * 60 * 60 * 7)));
        exam.setEndTime(Timestamp.from(Instant.now().plusSeconds(7200 + 1000 * 60 * 60 * 7)));
        exam.setTitle("test");
        exam.setDescription("test");

        when(addExamRequestMapper.mapTo(any(AddExamRequestDto.class))).thenReturn(exam);
        when(languageService.getLanguagesByNameList(anyList())).thenReturn(new HashSet<>());
        when(userService.getCurrentUser()).thenReturn(new Users());
        when(problemService.getProblemByExamProblemRequest(any())).thenReturn(problem);
        when(examRepository.save(any(Exam.class))).thenReturn(exam);
        when(examRepository.findAll()).thenReturn(Collections.emptyList());
        when(examResponseMapper.mapFrom(any(Exam.class))).thenReturn(new ExamResponseDto());
        when(examRepository.findByCode(any())).thenReturn(Optional.of(exam));

        ExamResponseDto response = examService.createExam(addExamRequestDto);

        assertNotNull(response);
        verify(examRepository, times(1)).save(any(Exam.class));
        verify(eventPublisher, times(1)).publishEvent(any(ExamStartEvent.class));
    }

    @Test
    void testCreateExamStartTimeInPast() {

        Language language = new Language();
        language.setName("Java");

        Problem problem = new Problem();
        problem.setLanguageSupport(Set.of(language));

        ExamProblemRequestDto examProblemRequestDto = new ExamProblemRequestDto();

        List<ExamProblemRequestDto> examProblemRequestDtos = List.of(examProblemRequestDto);

        AddExamRequestDto addExamRequestDto = new AddExamRequestDto();
        addExamRequestDto.setStartTime(Timestamp.from(Instant.now().plusSeconds(3600)));
        addExamRequestDto.setEndTime(Timestamp.from(Instant.now().plusSeconds(7200)));
        addExamRequestDto.setLanguageSupports(Collections.singletonList("Java"));
        addExamRequestDto.setProblemRequests(examProblemRequestDtos);

        Exam exam = new Exam();
        exam.setStartTime(Timestamp.from(Instant.now().plusSeconds(3600 + 1000 * 60 * 60 * 7)));
        exam.setEndTime(Timestamp.from(Instant.now().plusSeconds(7200 + 1000 * 60 * 60 * 7)));
        exam.setTitle("test");
        exam.setDescription("test");

        when(addExamRequestMapper.mapTo(any(AddExamRequestDto.class))).thenReturn(exam);
        when(languageService.getLanguagesByNameList(anyList())).thenReturn(new HashSet<>());
        when(userService.getCurrentUser()).thenReturn(new Users());
        when(problemService.getProblemByExamProblemRequest(any())).thenReturn(problem);
        when(examRepository.save(any(Exam.class))).thenReturn(exam);
        when(examRepository.findAll()).thenReturn(Collections.emptyList());
        when(examResponseMapper.mapFrom(any(Exam.class))).thenReturn(new ExamResponseDto());
        when(examRepository.findByCode(any())).thenReturn(Optional.of(exam));

        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> examService.createExam(addExamRequestDto));
        assertEquals("Start date cannot be in the past", badRequestException.getMessage());
        assertEquals("Start date cannot be in the past", badRequestException.getDetails());
    }

    @Test
    void testCreateExamEndTimeAfterStartTime() {

        Language language = new Language();
        language.setName("Java");

        Problem problem = new Problem();
        problem.setLanguageSupport(Set.of(language));

        ExamProblemRequestDto examProblemRequestDto = new ExamProblemRequestDto();

        List<ExamProblemRequestDto> examProblemRequestDtos = List.of(examProblemRequestDto);

        AddExamRequestDto addExamRequestDto = new AddExamRequestDto();
        addExamRequestDto.setStartTime(Timestamp.from(Instant.now().plusSeconds(7200 + 1000 * 60 * 60 * 7)));
        addExamRequestDto.setEndTime(Timestamp.from(Instant.now().plusSeconds(3600 + 1000 * 60 * 60 * 7)));
        addExamRequestDto.setLanguageSupports(Collections.singletonList("Java"));
        addExamRequestDto.setProblemRequests(examProblemRequestDtos);

        Exam exam = new Exam();
        exam.setStartTime(Timestamp.from(Instant.now().plusSeconds(3600 + 1000 * 60 * 60 * 7)));
        exam.setEndTime(Timestamp.from(Instant.now().plusSeconds(7200 + 1000 * 60 * 60 * 7)));
        exam.setTitle("test");
        exam.setDescription("test");

        when(addExamRequestMapper.mapTo(any(AddExamRequestDto.class))).thenReturn(exam);
        when(languageService.getLanguagesByNameList(anyList())).thenReturn(new HashSet<>());
        when(userService.getCurrentUser()).thenReturn(new Users());
        when(problemService.getProblemByExamProblemRequest(any())).thenReturn(problem);
        when(examRepository.save(any(Exam.class))).thenReturn(exam);
        when(examRepository.findAll()).thenReturn(Collections.emptyList());
        when(examResponseMapper.mapFrom(any(Exam.class))).thenReturn(new ExamResponseDto());
        when(examRepository.findByCode(any())).thenReturn(Optional.of(exam));

        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> examService.createExam(addExamRequestDto));
        assertEquals("Start date cannot be after end date", badRequestException.getMessage());
        assertEquals("Start date cannot be after end date", badRequestException.getDetails());
    }

    @Test
    void testCreateExamLanguageNotMatch() {

        Language language = new Language();
        language.setName("C");

        Problem problem = new Problem();
        problem.setLanguageSupport(new HashSet());

        ExamProblemRequestDto examProblemRequestDto = new ExamProblemRequestDto();

        List<ExamProblemRequestDto> examProblemRequestDtos = List.of(examProblemRequestDto);

        AddExamRequestDto addExamRequestDto = new AddExamRequestDto();
        addExamRequestDto.setStartTime(Timestamp.from(Instant.now().plusSeconds(3600 + 1000 * 60 * 60 * 7)));
        addExamRequestDto.setEndTime(Timestamp.from(Instant.now().plusSeconds(7200 + 1000 * 60 * 60 * 7)));
        addExamRequestDto.setLanguageSupports(Collections.singletonList("Java"));
        addExamRequestDto.setProblemRequests(examProblemRequestDtos);

        Exam exam = new Exam();
        exam.setStartTime(Timestamp.from(Instant.now().plusSeconds(3600 + 1000 * 60 * 60 * 7)));
        exam.setEndTime(Timestamp.from(Instant.now().plusSeconds(7200 + 1000 * 60 * 60 * 7)));
        exam.setTitle("test");
        exam.setDescription("test");

        when(addExamRequestMapper.mapTo(any(AddExamRequestDto.class))).thenReturn(exam);
        when(languageService.getLanguagesByNameList(anyList())).thenReturn(Set.of(language));
        when(userService.getCurrentUser()).thenReturn(new Users());
        when(problemService.getProblemByExamProblemRequest(any())).thenReturn(problem);
        when(examRepository.save(any(Exam.class))).thenReturn(exam);
        when(examRepository.findAll()).thenReturn(Collections.emptyList());
        when(examResponseMapper.mapFrom(any(Exam.class))).thenReturn(new ExamResponseDto());
        when(examRepository.findByCode(any())).thenReturn(Optional.of(exam));

        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> examService.createExam(addExamRequestDto));
        assertEquals("Language support of your exam does not match with your problem",
                badRequestException.getMessage());
        assertEquals("Language support of your exam does not match with your problem",
                badRequestException.getDetails());
    }

    @Test
    void testEditExamSuccess() {
        Language language = new Language();
        language.setName("Java");

        Problem problem = new Problem();
        problem.setLanguageSupport(Set.of(language));

        ExamProblemRequestDto examProblemRequestDto = new ExamProblemRequestDto();

        List<ExamProblemRequestDto> examProblemRequestDtos = List.of(examProblemRequestDto);

        AddExamRequestDto addExamRequestDto = new AddExamRequestDto();
        addExamRequestDto.setStartTime(Timestamp.from(Instant.now().plusSeconds(3600 + 1000 * 60 * 60 * 7)));
        addExamRequestDto.setEndTime(Timestamp.from(Instant.now().plusSeconds(7200 + 1000 * 60 * 60 * 7)));
        addExamRequestDto.setLanguageSupports(Collections.singletonList("Java"));
        addExamRequestDto.setProblemRequests(examProblemRequestDtos);

        Exam savedExam = new Exam();
        savedExam.setStartTime(Timestamp.from(Instant.now().plusSeconds(3600 + 1000 * 60 * 60 * 7)));
        savedExam.setEndTime(Timestamp.from(Instant.now().plusSeconds(7200 + 1000 * 60 * 60 * 7)));
        savedExam.setTitle("test");
        savedExam.setDescription("test");
        savedExam.setStatus(ExamStatus.NOT_STARTED);

        when(examRepository.findByCode(anyString())).thenReturn(Optional.of(savedExam));
        when(addExamRequestMapper.mapTo(any(AddExamRequestDto.class))).thenReturn(savedExam);
        when(languageService.getLanguagesByNameList(anyList())).thenReturn(new HashSet<>());
        when(userService.getCurrentUser()).thenReturn(new Users());
        when(examRepository.save(any(Exam.class))).thenReturn(savedExam);
        when(examResponseMapper.mapFrom(any(Exam.class))).thenReturn(new ExamResponseDto());
        when(problemService.getProblemByExamProblemRequest(any())).thenReturn(problem);

        ExamResponseDto response = examService.editExam(addExamRequestDto, "code");

        assertNotNull(response);
        verify(examRepository, times(1)).save(any(Exam.class));
        verify(eventPublisher, times(1)).publishEvent(any(ExamStartEvent.class));
    }

    @Test
    void testEditExamNotInRightStatus() {
        Language language = new Language();
        language.setName("Java");

        Problem problem = new Problem();
        problem.setLanguageSupport(Set.of(language));

        ExamProblemRequestDto examProblemRequestDto = new ExamProblemRequestDto();

        List<ExamProblemRequestDto> examProblemRequestDtos = List.of(examProblemRequestDto);

        AddExamRequestDto addExamRequestDto = new AddExamRequestDto();
        addExamRequestDto.setStartTime(Timestamp.from(Instant.now().plusSeconds(3600 + 1000 * 60 * 60 * 7)));
        addExamRequestDto.setEndTime(Timestamp.from(Instant.now().plusSeconds(7200 + 1000 * 60 * 60 * 7)));
        addExamRequestDto.setLanguageSupports(Collections.singletonList("Java"));
        addExamRequestDto.setProblemRequests(examProblemRequestDtos);

        Exam savedExam = new Exam();
        savedExam.setStartTime(Timestamp.from(Instant.now().plusSeconds(3600 + 1000 * 60 * 60 * 7)));
        savedExam.setEndTime(Timestamp.from(Instant.now().plusSeconds(7200 + 1000 * 60 * 60 * 7)));
        savedExam.setTitle("test");
        savedExam.setDescription("test");
        savedExam.setStatus(ExamStatus.IN_PROGRESS);

        when(examRepository.findByCode(anyString())).thenReturn(Optional.of(savedExam));
        when(addExamRequestMapper.mapTo(any(AddExamRequestDto.class))).thenReturn(savedExam);
        when(languageService.getLanguagesByNameList(anyList())).thenReturn(new HashSet<>());
        when(userService.getCurrentUser()).thenReturn(new Users());
        when(examRepository.save(any(Exam.class))).thenReturn(savedExam);
        when(examResponseMapper.mapFrom(any(Exam.class))).thenReturn(new ExamResponseDto());
        when(problemService.getProblemByExamProblemRequest(any())).thenReturn(problem);

        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> examService.editExam(addExamRequestDto, "code"));
        assertEquals("This exam is already started or ended",
                badRequestException.getMessage());
        assertEquals("This exam is already started or ended",
                badRequestException.getDetails());
    }

    @Test
    void testDeleteExamSuccess() {
        Exam savedExam = new Exam();
        savedExam.setStatus(ExamStatus.NOT_STARTED);

        when(examRepository.findByCode(anyString())).thenReturn(Optional.of(savedExam));

        examService.deleteExam("code");

        verify(examRepository, times(1)).delete(any(Exam.class));
    }

    @Test
    void testDeleteExamNotFound() {
        Exam savedExam = new Exam();
        savedExam.setStatus(ExamStatus.NOT_STARTED);

        when(examRepository.findByCode(anyString())).thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> examService.deleteExam("code"));
        assertEquals("Exam not found",
                notFoundException.getMessage());
        assertEquals("Exam not found",
                notFoundException.getDetails());
    }

    @Test
    void testGetListOfExam() {
        FilterExamRequestDto filterExamRequestDto = new FilterExamRequestDto();
        filterExamRequestDto.setPage(0);
        filterExamRequestDto.setSize(5);
        filterExamRequestDto.setTitle("test");
        filterExamRequestDto.setStatus(ExamStatus.NOT_STARTED);

        List<Exam> exams = new ArrayList();
        exams.add(new Exam());

        Page<Exam> examPage = new PageImpl<>(exams);
        when(examRepository.searchExam(anyString(), any(), any(), any(), any(Pageable.class))).thenReturn(examPage);

        Page<ExamListResponseDto> response = examService.getListOfExam(filterExamRequestDto);

        assertNotNull(response);
        verify(examRepository, times(1)).searchExam(anyString(), any(), any(), any(), any(Pageable.class));
    }

    @Test
    void testGetExamDetailByCodeSuccess() {
        Exam exam = new Exam();
        when(examRepository.findByCode(anyString())).thenReturn(Optional.of(exam));
        when(examResponseMapper.mapFrom(any(Exam.class))).thenReturn(new ExamResponseDto());

        ExamResponseDto response = examService.getExamDetailByCode("code");

        assertNotNull(response);
        verify(examRepository, times(1)).findByCode(anyString());
    }

    @Test
    void testGetExamDetailByCodeNotFound() {
        when(examRepository.findByCode(anyString())).thenReturn(Optional.empty());
        when(examResponseMapper.mapFrom(any(Exam.class))).thenReturn(new ExamResponseDto());

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> examService.getExamDetailByCode("code"));
        assertEquals("Exam not found",
                notFoundException.getMessage());
        assertEquals("Exam not found",
                notFoundException.getDetails());
    }

    @Test
    void testEnrollExamSuccess() {
        Exam exam = new Exam();
        exam.setStatus(ExamStatus.NOT_STARTED);
        exam.setStartTime(Timestamp.from(Instant.now().plusSeconds(3600)));

        Users user = new Users();
        when(examParticipantRepository
                .checkDuplicateTimeExam(any(), any(), any(), any())).thenReturn(new ArrayList());
        when(examRepository.findByCode(anyString())).thenReturn(Optional.of(exam));
        when(userService.getCurrentUser()).thenReturn(user);
        when(examParticipantRepository.findByExamAndParticipant(any(Exam.class), any(Users.class)))
                .thenReturn(Optional.empty());

        examService.enrollExam("code");

        verify(examParticipantRepository, times(1)).save(any(ExamParticipant.class));
        verify(examRepository, times(1)).save(any(Exam.class));
    }

    @Test
    void testEnrollExamOverlapTime() {
        Exam exam = new Exam();
        exam.setStatus(ExamStatus.NOT_STARTED);
        exam.setStartTime(Timestamp.from(Instant.now().plusSeconds(3600)));

        Users user = new Users();
        when(examParticipantRepository
                .checkDuplicateTimeExam(any(), any(), any(), any())).thenReturn(List.of(new ExamParticipant()));
        when(examRepository.findByCode(anyString())).thenReturn(Optional.of(exam));
        when(userService.getCurrentUser()).thenReturn(user);
        when(examParticipantRepository.findByExamAndParticipant(any(Exam.class), any(Users.class)))
                .thenReturn(Optional.empty());

        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> examService.enrollExam("code"));
        assertEquals("You have enrolled for another exam that overlaps with this one",
                badRequestException.getMessage());
        assertEquals("You have enrolled for another exam that overlaps with this one",
                badRequestException.getDetails());
    }

    @Test
    void testEnrollExamStartBefore() {
        Exam exam = new Exam();
        exam.setStatus(ExamStatus.NOT_STARTED);
        exam.setStartTime(Timestamp.from(Instant.now().minusSeconds(3600)));

        Users user = new Users();
        when(examParticipantRepository
                .checkDuplicateTimeExam(any(), any(), any(), any())).thenReturn(new ArrayList());
        when(examRepository.findByCode(anyString())).thenReturn(Optional.of(exam));
        when(userService.getCurrentUser()).thenReturn(user);
        when(examParticipantRepository.findByExamAndParticipant(any(Exam.class), any(Users.class)))
                .thenReturn(Optional.empty());

        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> examService.enrollExam("code"));
        assertEquals("This exam has already started",
                badRequestException.getMessage());
        assertEquals("This exam has already started",
                badRequestException.getDetails());
    }

    @Test
    void testEnrollExamAlreadyStarted() {
        Exam exam = new Exam();
        exam.setStatus(ExamStatus.END);
        exam.setStartTime(Timestamp.from(Instant.now().plusSeconds(3600)));

        Users user = new Users();
        when(examParticipantRepository
                .checkDuplicateTimeExam(any(), any(), any(), any())).thenReturn(new ArrayList());
        when(examRepository.findByCode(anyString())).thenReturn(Optional.of(exam));
        when(userService.getCurrentUser()).thenReturn(user);
        when(examParticipantRepository.findByExamAndParticipant(any(Exam.class), any(Users.class)))
                .thenReturn(Optional.empty());

        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> examService.enrollExam("code"));
        assertEquals("This exam is already started or ended",
                badRequestException.getMessage());
        assertEquals("This exam is already started or ended",
                badRequestException.getDetails());
    }

    @Test
    void testEnrollExamAlreadyEnrolled() {
        Exam exam = new Exam();
        exam.setStatus(ExamStatus.NOT_STARTED);
        exam.setStartTime(Timestamp.from(Instant.now().plusSeconds(3600)));

        Users user = new Users();
        when(examParticipantRepository
                .checkDuplicateTimeExam(any(), any(), any(), any())).thenReturn(new ArrayList());
        when(examRepository.findByCode(anyString())).thenReturn(Optional.of(exam));
        when(userService.getCurrentUser()).thenReturn(user);
        when(examParticipantRepository.findByExamAndParticipant(any(Exam.class), any(Users.class)))
                .thenReturn(Optional.of(new ExamParticipant()));

        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> examService.enrollExam("code"));
        assertEquals("You have already enrolled this exam",
                badRequestException.getMessage());
        assertEquals("You have already enrolled this exam",
                badRequestException.getDetails());
    }

    @Test
    void testSubmitExam() {
        Exam exam = new Exam();
        exam.setStatus(ExamStatus.IN_PROGRESS);
        exam.setStartTime(Timestamp.from(Instant.now().minusSeconds(3600)));
        exam.setEndTime(Timestamp.from(Instant.now().plusSeconds(3600)));

        Users user = new Users();
        user.setRole(UserRole.STUDENT);

        when(examRepository.findByCode(anyString())).thenReturn(Optional.of(exam));
        when(userService.getUserByUsernameOrEmail(anyString())).thenReturn(user);
        when(examParticipantRepository.findByExamAndParticipant(any(Exam.class), any(Users.class)))
                .thenReturn(Optional.of(new ExamParticipant()));
        when(problemService.submitExam(anyList())).thenReturn(new ExamResultOverviewResponseDto());

        double grade = examService.submitExam(Collections.emptyList(), "code", "username");

        assertEquals(0, grade);
        verify(examSubmissionRepository, times(0)).save(any(ExamSubmission.class));
    }

    @Test
    void testGenerateTokenForExamSuccess() {
        Exam exam = new Exam();
        exam.setCode("test");
        exam.setStatus(ExamStatus.NOT_STARTED);
        exam.setStartTime(Timestamp.from(Instant.now().plusSeconds(36)));

        Users user = new Users();
        user.setId(1L);
        user.setUsername("test");
        ExamParticipant examParticipant = new ExamParticipant();
        examParticipant.setParticipant(user);

        when(examParticipantRepository.findByExam(any())).thenReturn(List.of(examParticipant));
        when(examRepository.findByCode(anyString())).thenReturn(Optional.of(exam));
        when(userService.getCurrentUser()).thenReturn(user);
        when(tokenService.generateAccessToken(anyString())).thenReturn("token");

        Object response = examService.generateTokenForExam("code");

        assertNotNull(response);
        verify(tokenService, times(1)).generateAccessToken(anyString());
    }

    @Test
    void testGenerateTokenForExamAlreadyStarted() {
        Exam exam = new Exam();
        exam.setCode("test");
        exam.setStatus(ExamStatus.END);
        exam.setStartTime(Timestamp.from(Instant.now().plusSeconds(36)));

        Users user = new Users();
        user.setId(1L);
        user.setUsername("test");
        ExamParticipant examParticipant = new ExamParticipant();
        examParticipant.setParticipant(user);

        when(examParticipantRepository.findByExam(any())).thenReturn(List.of(examParticipant));
        when(examRepository.findByCode(anyString())).thenReturn(Optional.of(exam));
        when(userService.getCurrentUser()).thenReturn(user);
        when(tokenService.generateAccessToken(anyString())).thenReturn("token");

        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> examService.generateTokenForExam("code"));
        assertEquals("The exam has already started or ended.",
                badRequestException.getMessage());
        assertEquals("The exam has already started or ended.",
                badRequestException.getDetails());
    }

    @Test
    void testGenerateTokenForExamNotEnrolled() {
        Exam exam = new Exam();
        exam.setCode("test");
        exam.setStatus(ExamStatus.END);
        exam.setStartTime(Timestamp.from(Instant.now().plusSeconds(36)));

        Users user = new Users();
        user.setId(1L);
        user.setUsername("test");
        ExamParticipant examParticipant = new ExamParticipant();
        examParticipant.setParticipant(user);

        when(examParticipantRepository.findByExam(any())).thenReturn(new ArrayList());
        when(examRepository.findByCode(anyString())).thenReturn(Optional.of(exam));
        when(userService.getCurrentUser()).thenReturn(user);
        when(tokenService.generateAccessToken(anyString())).thenReturn("token");

        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> examService.generateTokenForExam("code"));
        assertEquals("The exam has already started or ended.",
                badRequestException.getMessage());
        assertEquals("The exam has already started or ended.",
                badRequestException.getDetails());
    }

    @Test
    void testGenerateTokenForExamNotReadyToStart() {
        Exam exam = new Exam();
        exam.setCode("test");
        exam.setStatus(ExamStatus.END);
        exam.setStartTime(Timestamp.from(Instant.now().minusSeconds(36000)));

        Users user = new Users();
        user.setId(1L);
        user.setUsername("test");
        ExamParticipant examParticipant = new ExamParticipant();
        examParticipant.setParticipant(user);

        when(examParticipantRepository.findByExam(any())).thenReturn(new ArrayList());
        when(examRepository.findByCode(anyString())).thenReturn(Optional.of(exam));
        when(userService.getCurrentUser()).thenReturn(user);
        when(tokenService.generateAccessToken(anyString())).thenReturn("token");

        assertThrows(BadRequestException.class,
                () -> examService.generateTokenForExam("code"));
    }

    @Test
    void testStartExam() {
        Exam exam = new Exam();
        exam.setStatus(ExamStatus.NOT_STARTED);
        exam.setStartTime(Timestamp.from(Instant.now().plusSeconds(36)));

        when(examRepository.findByCode(anyString())).thenReturn(Optional.of(exam));
        when(examRepository.save(any(Exam.class))).thenReturn(exam);

        List<ExamProblemDetailResponseDto> response = examService.startExam("code");

        assertNotNull(response);
        verify(examRepository, times(1)).save(any(Exam.class));
    }

    @Test
    void testEndExam() {
        List<Exam> exams = Collections.singletonList(new Exam());
        when(examRepository.getExamAlreadyEnded(any(Timestamp.class))).thenReturn(exams);

        examService.endExam();

        verify(examRepository, times(1)).save(any(Exam.class));
    }

    @Test
    void testViewResult() {
        Exam exam = new Exam();
        exam.setStatus(ExamStatus.END);

        Users user = new Users();
        when(examRepository.findByCode(anyString())).thenReturn(Optional.of(exam));
        when(userService.getCurrentUser()).thenReturn(user);
        when(examParticipantRepository.findByExamAndParticipant(any(Exam.class), any(Users.class)))
                .thenReturn(Optional.of(new ExamParticipant()));

        ExamResultOverviewResponseDto response = examService.viewResult("code");

        assertNotNull(response);
        verify(examParticipantRepository, times(1)).findByExamAndParticipant(any(Exam.class), any(Users.class));
    }
}