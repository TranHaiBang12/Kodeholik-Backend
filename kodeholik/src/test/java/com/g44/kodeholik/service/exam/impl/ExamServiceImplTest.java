package com.g44.kodeholik.service.exam.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
import com.g44.kodeholik.exception.ForbiddenException;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.exam.AddExamRequestDto;
import com.g44.kodeholik.model.dto.request.exam.ExamProblemRequestDto;
import com.g44.kodeholik.model.dto.request.exam.FilterExamRequestDto;
import com.g44.kodeholik.model.dto.request.exam.SubmitExamRequestDto;
import com.g44.kodeholik.model.dto.request.lambda.TestCase;
import com.g44.kodeholik.model.dto.request.problem.ProblemCompileRequestDto;
import com.g44.kodeholik.model.dto.response.exam.examiner.ExamListResponseDto;
import com.g44.kodeholik.model.dto.response.exam.examiner.ExamProblemResponseDto;
import com.g44.kodeholik.model.dto.response.exam.examiner.ExamResponseDto;
import com.g44.kodeholik.model.dto.response.exam.student.ExamCompileInformationResponseDto;
import com.g44.kodeholik.model.dto.response.exam.student.ExamListStudentResponseDto;
import com.g44.kodeholik.model.dto.response.exam.student.ExamProblemDetailResponseDto;
import com.g44.kodeholik.model.dto.response.exam.student.ExamResultOverviewResponseDto;
import com.g44.kodeholik.model.dto.response.exam.student.NotStartedExamListDto;
import com.g44.kodeholik.model.dto.response.exam.student.ProblemResultOverviewResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.run.RunProblemResponseDto;
import com.g44.kodeholik.model.entity.exam.Exam;
import com.g44.kodeholik.model.entity.exam.ExamParticipant;
import com.g44.kodeholik.model.entity.exam.ExamParticipantId;
import com.g44.kodeholik.model.entity.exam.ExamProblem;
import com.g44.kodeholik.model.entity.exam.ExamProblemId;
import com.g44.kodeholik.model.entity.exam.ExamSubmission;
import com.g44.kodeholik.model.entity.exam.ExamSubmissionId;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSubmission;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.exam.ExamStatus;
import com.g44.kodeholik.model.enums.user.NotificationType;
import com.g44.kodeholik.model.enums.user.UserRole;
import com.g44.kodeholik.repository.exam.ExamParticipantRepository;
import com.g44.kodeholik.repository.exam.ExamProblemRepository;
import com.g44.kodeholik.repository.exam.ExamRepository;
import com.g44.kodeholik.repository.exam.ExamSubmissionRepository;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.service.email.EmailService;
import com.g44.kodeholik.service.exam.ExamService;
import com.g44.kodeholik.service.problem.ProblemService;
import com.g44.kodeholik.service.problem.ProblemSubmissionService;
import com.g44.kodeholik.service.problem.ProblemTestCaseService;
import com.g44.kodeholik.service.publisher.Publisher;
import com.g44.kodeholik.service.redis.RedisService;
import com.g44.kodeholik.service.scheduler.ExamSchedulerService;
import com.g44.kodeholik.service.scheduler.ExamStartEvent;
import com.g44.kodeholik.service.setting.LanguageService;
import com.g44.kodeholik.service.token.TokenService;
import com.g44.kodeholik.service.user.NotificationService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.mapper.request.exam.AddExamRequestMapper;
import com.g44.kodeholik.util.mapper.response.exam.ExamListResponseMapper;
import com.g44.kodeholik.util.mapper.response.exam.ExamListStudentResponseMapper;
import com.g44.kodeholik.util.mapper.response.exam.ExamResponseMapper;
import com.g44.kodeholik.util.mapper.response.exam.NotStartedExamListMapper;
import com.g44.kodeholik.util.uuid.UUIDGenerator;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateExam() {
        AddExamRequestDto addExamRequestDto = new AddExamRequestDto();
        addExamRequestDto.setStartTime(Timestamp.from(Instant.now().plusSeconds(3600)));
        addExamRequestDto.setEndTime(Timestamp.from(Instant.now().plusSeconds(7200)));
        addExamRequestDto.setLanguageSupports(Arrays.asList("Java", "Python"));
        addExamRequestDto.setProblemRequests(new ArrayList<>());

        Exam exam = new Exam();
        when(addExamRequestMapper.mapTo(addExamRequestDto)).thenReturn(exam);
        when(languageService.getLanguagesByNameList(any())).thenReturn(new HashSet<>());
        when(userService.getCurrentUser()).thenReturn(new Users());
        when(examRepository.save(any())).thenReturn(exam);
        when(examRepository.findAll()).thenReturn(new ArrayList<>());
        when(examResponseMapper.mapFrom(any())).thenReturn(new ExamResponseDto());

        ExamResponseDto response = examService.createExam(addExamRequestDto);

        assertNotNull(response);
        verify(examRepository, times(1)).save(any());
        verify(eventPublisher, times(1)).publishEvent(any());
    }

    @Test
    void testEditExam() {
        AddExamRequestDto addExamRequestDto = new AddExamRequestDto();
        addExamRequestDto.setStartTime(Timestamp.from(Instant.now().plusSeconds(3600)));
        addExamRequestDto.setEndTime(Timestamp.from(Instant.now().plusSeconds(7200)));
        addExamRequestDto.setLanguageSupports(Arrays.asList("Java", "Python"));
        addExamRequestDto.setProblemRequests(new ArrayList<>());

        Exam savedExam = new Exam();
        savedExam.setStatus(ExamStatus.NOT_STARTED);

        when(examRepository.findByCode(anyString())).thenReturn(Optional.of(savedExam));
        when(addExamRequestMapper.mapTo(addExamRequestDto)).thenReturn(savedExam);
        when(languageService.getLanguagesByNameList(any())).thenReturn(new HashSet<>());
        when(userService.getCurrentUser()).thenReturn(new Users());
        when(examRepository.save(any())).thenReturn(savedExam);
        when(examResponseMapper.mapFrom(any())).thenReturn(new ExamResponseDto());

        ExamResponseDto response = examService.editExam(addExamRequestDto, "code");

        assertNotNull(response);
        verify(examRepository, times(1)).save(any());
        verify(eventPublisher, times(1)).publishEvent(any());
    }

    @Test
    void testDeleteExam() {
        Exam savedExam = new Exam();
        savedExam.setStatus(ExamStatus.NOT_STARTED);

        when(examRepository.findByCode(anyString())).thenReturn(Optional.of(savedExam));

        examService.deleteExam("code");

        verify(examRepository, times(1)).delete(any());
    }

    @Test
    void testGetListOfExam() {
        FilterExamRequestDto filterExamRequestDto = new FilterExamRequestDto();
        filterExamRequestDto.setPage(0);
        filterExamRequestDto.setSize(5);
        filterExamRequestDto.setSortBy("createdAt");
        filterExamRequestDto.setAscending(true);

        Page<Exam> examPage = new PageImpl<>(new ArrayList<>());

        when(examRepository.searchExam(anyString(), any(), any(), any(), any())).thenReturn(examPage);
        when(examListResponseMapper.mapFrom(any())).thenReturn(new ExamListResponseDto());

        Page<ExamListResponseDto> response = examService.getListOfExam(filterExamRequestDto);

        assertNotNull(response);
        verify(examRepository, times(1)).searchExam(anyString(), any(), any(), any(), any());
    }

    @Test
    void testGetExamDetailByCode() {
        Exam exam = new Exam();
        when(examRepository.findByCode(anyString())).thenReturn(Optional.of(exam));
        when(examResponseMapper.mapFrom(any())).thenReturn(new ExamResponseDto());
        when(examProblemRepository.findByExam(any())).thenReturn(new ArrayList<>());

        ExamResponseDto response = examService.getExamDetailByCode("code");

        assertNotNull(response);
        verify(examRepository, times(1)).findByCode(anyString());
    }

    @Test
    void testEnrollExam() {
        Exam exam = new Exam();
        exam.setStatus(ExamStatus.NOT_STARTED);
        exam.setStartTime(Timestamp.from(Instant.now().plusSeconds(3600)));

        Users user = new Users();
        when(examRepository.findByCode(anyString())).thenReturn(Optional.of(exam));
        when(userService.getCurrentUser()).thenReturn(user);
        when(examParticipantRepository.findByExamAndParticipant(any(), any())).thenReturn(Optional.empty());

        examService.enrollExam("code");

        verify(examParticipantRepository, times(1)).save(any());
        verify(examRepository, times(1)).save(any());
    }

    @Test
    void testGetCodeFromExamReadyToStarted() {
        when(examRepository.getCodeFromExamReadyToStarted(any(), any())).thenReturn(new ArrayList<>());

        List<String> response = examService.getCodeFromExamReadyToStarted();

        assertNotNull(response);
        verify(examRepository, times(1)).getCodeFromExamReadyToStarted(any(), any());
    }

    @Test
    void testGetProblemDetailInExam() {
        Exam exam = new Exam();
        exam.setStatus(ExamStatus.IN_PROGRESS);
        exam.setStartTime(Timestamp.from(Instant.now().minusSeconds(3600)));

        when(examRepository.findByCode(anyString())).thenReturn(Optional.of(exam));
        when(examProblemRepository.findByExam(any())).thenReturn(new ArrayList<>());

        List<ExamProblemDetailResponseDto> response = examService.getProblemDetailInExam("code");

        assertNotNull(response);
        verify(examRepository, times(1)).findByCode(anyString());
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
        when(examParticipantRepository.findByExamAndParticipant(any(), any()))
                .thenReturn(Optional.of(new ExamParticipant()));
        when(problemService.submitExam(any())).thenReturn(new ExamResultOverviewResponseDto());

        ExamResultOverviewResponseDto response = examService.submitExam(new ArrayList<>(), "code", "username");

        assertNotNull(response);
        verify(problemService, times(1)).submitExam(any());
    }

    @Test
    void testGenerateTokenForExam() {
        Exam exam = new Exam();
        exam.setStatus(ExamStatus.NOT_STARTED);

        Users user = new Users();
        user.setRole(UserRole.STUDENT);

        when(examRepository.findByCode(anyString())).thenReturn(Optional.of(exam));
        when(userService.getCurrentUser()).thenReturn(user);
        when(tokenService.generateAccessToken(anyString())).thenReturn("token");

        Object response = examService.generateTokenForExam("code");

        assertNotNull(response);
        verify(tokenService, times(1)).generateAccessToken(anyString());
    }

    @Test
    void testStartExam() {
        Exam exam = new Exam();
        exam.setStatus(ExamStatus.NOT_STARTED);

        when(examRepository.findByCode(anyString())).thenReturn(Optional.of(exam));
        when(examProblemRepository.findByExam(any())).thenReturn(new ArrayList<>());

        List<ExamProblemDetailResponseDto> response = examService.startExam("code");

        assertNotNull(response);
        verify(examRepository, times(1)).save(any());
    }

    @Test
    void testRunExam() {
        Exam exam = new Exam();
        when(examRepository.findByCode(anyString())).thenReturn(Optional.of(exam));
        when(examProblemRepository.findByExam(any())).thenReturn(new ArrayList<>());
        when(problemService.runExam(anyString(), any())).thenReturn(new RunProblemResponseDto());

        RunProblemResponseDto response = examService.runExam("examCode", "link", new ProblemCompileRequestDto());

        assertNotNull(response);
        verify(problemService, times(1)).runExam(anyString(), any());
    }

    @Test
    void testEndExam() {
        when(examRepository.getExamAlreadyEnded(any())).thenReturn(new ArrayList<>());

        examService.endExam();

        verify(examRepository, times(1)).getExamAlreadyEnded(any());
    }

    @Test
    void testViewResult() {
        Exam exam = new Exam();
        exam.setStatus(ExamStatus.END);

        Users user = new Users();

        when(examRepository.findByCode(anyString())).thenReturn(Optional.of(exam));
        when(userService.getCurrentUser()).thenReturn(user);
        when(examParticipantRepository.findByExamAndParticipant(any(), any()))
                .thenReturn(Optional.of(new ExamParticipant()));
        when(examSubmissionRepository.findByExamParticipant(any())).thenReturn(new ArrayList<>());

        ExamResultOverviewResponseDto response = examService.viewResult("code");

        assertNotNull(response);
        verify(examRepository, times(1)).findByCode(anyString());
    }

    @Test
    void testGetListExam() {
        Users user = new Users();
        when(userService.getCurrentUser()).thenReturn(user);
        when(examParticipantRepository.findByStatus(any(), any(), any())).thenReturn(Page.empty());
        when(examListStudentResponseMapper.mapFrom(any())).thenReturn(new ExamListStudentResponseDto());

        Page<ExamListStudentResponseDto> response = examService.getListExam(ExamStatus.NOT_STARTED, 0, 5);

        assertNotNull(response);
        verify(examParticipantRepository, times(1)).findByStatus(any(), any(), any());
    }

    @Test
    void testGetAllParticipantsInExam() {
        Exam exam = new Exam();
        when(examRepository.findByCode(anyString())).thenReturn(Optional.of(exam));
        when(examParticipantRepository.findByExam(any())).thenReturn(new ArrayList<>());

        List<Map<String, String>> response = examService.getAllParticipantsInExam("code");

        assertNotNull(response);
        verify(examRepository, times(1)).findByCode(anyString());
    }

    @Test
    void testViewResultOfASpecificParticipant() {
        Exam exam = new Exam();
        exam.setStatus(ExamStatus.END);

        Users user = new Users();

        when(examRepository.findByCode(anyString())).thenReturn(Optional.of(exam));
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(examParticipantRepository.findByExamAndParticipant(any(), any()))
                .thenReturn(Optional.of(new ExamParticipant()));
        when(examSubmissionRepository.findByExamParticipant(any())).thenReturn(new ArrayList<>());

        ExamResultOverviewResponseDto response = examService.viewResultOfASpecificParticpant("code", 1L);

        assertNotNull(response);
        verify(examRepository, times(1)).findByCode(anyString());
    }

    @Test
    void testSendNotiToUserExamAboutToStart() {
        when(examRepository.getExamAboutToStart(any(), any())).thenReturn(new ArrayList<>());

        examService.sendNotiToUserExamAboutToStart();

        verify(examRepository, times(1)).getExamAboutToStart(any(), any());
    }

    @Test
    void testGetAllPendingExam() {
        when(examRepository.findByStatus(any())).thenReturn(new ArrayList<>());

        List<Exam> response = examService.getAllPendingExam();

        assertNotNull(response);
        verify(examRepository, times(1)).findByStatus(any());
    }

    @Test
    void testGetAllPendingExamNotOverlapTime() {
        Users user = new Users();
        when(userService.getCurrentUser()).thenReturn(user);
        when(examRepository.findByStatus(any())).thenReturn(new ArrayList<>());
        when(notStartedExamListMapper.mapFrom(any())).thenReturn(new NotStartedExamListDto());

        List<NotStartedExamListDto> response = examService.getAllPendingExamNotOverlapTime();

        assertNotNull(response);
        verify(examRepository, times(1)).findByStatus(any());
    }
}