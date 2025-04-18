package com.g44.kodeholik.service.exam.impl;

import java.sql.Timestamp;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.g44.kodeholik.controller.admin.AdminController;
import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.exception.ExamNotReadyToStartException;
import com.g44.kodeholik.exception.ForbiddenException;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.exam.AddExamRequestDto;
import com.g44.kodeholik.model.dto.request.exam.EditExamBasicRequestDto;
import com.g44.kodeholik.model.dto.request.exam.EditExamProblemRequestDto;
import com.g44.kodeholik.model.dto.request.exam.ExamProblemRequestDto;
import com.g44.kodeholik.model.dto.request.exam.FilterExamRequestDto;
import com.g44.kodeholik.model.dto.request.exam.SubmitExamRequestDto;
import com.g44.kodeholik.model.dto.request.lambda.TestCase;
import com.g44.kodeholik.model.dto.request.problem.ProblemCompileRequestDto;
import com.g44.kodeholik.model.dto.response.exam.examiner.ExamListResponseDto;
import com.g44.kodeholik.model.dto.response.exam.examiner.ExamProblemResponseDto;
import com.g44.kodeholik.model.dto.response.exam.examiner.ExamResponseDto;
import com.g44.kodeholik.model.dto.response.exam.examiner.ExamResultExcelDto;
import com.g44.kodeholik.model.dto.response.exam.examiner.ExamResultOverviewDto;
import com.g44.kodeholik.model.dto.response.exam.examiner.ProblemPoint;
import com.g44.kodeholik.model.dto.response.exam.student.ExamCompileInformationResponseDto;
import com.g44.kodeholik.model.dto.response.exam.student.ExamDetailResponseDto;
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
import com.g44.kodeholik.service.excel.ExcelService;
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
import com.g44.kodeholik.util.mapper.request.user.AddUserAvatarFileMapper;
import com.g44.kodeholik.util.mapper.request.user.AddUserRequestMapper;
import com.g44.kodeholik.util.mapper.response.exam.ExamListResponseMapper;
import com.g44.kodeholik.util.mapper.response.exam.ExamListStudentResponseMapper;
import com.g44.kodeholik.util.mapper.response.exam.ExamResponseMapper;
import com.g44.kodeholik.util.mapper.response.exam.NotStartedExamListMapper;
import com.g44.kodeholik.util.uuid.UUIDGenerator;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;

    private final ExamProblemRepository examProblemRepository;

    private final ExamParticipantRepository examParticipantRepository;

    private final AddExamRequestMapper addExamRequestMapper;

    private final ExamResponseMapper examResponseMapper;

    private final ExamListResponseMapper examListResponseMapper;

    private final LanguageService languageService;

    private final ProblemService problemService;

    private final UserService userService;

    private final TokenService tokenService;

    private final ProblemTestCaseService problemTestCaseService;

    private final ExamSubmissionRepository examSubmissionRepository;

    private final ProblemSubmissionService problemSubmissionService;

    private final Publisher publisher;

    private final ExamListStudentResponseMapper examListStudentResponseMapper;

    private final S3Service s3Service;

    private final EmailService emailService;

    private final RedisService redisService;

    private final NotificationService notificationService;

    private final ApplicationEventPublisher eventPublisher;

    private final NotStartedExamListMapper notStartedExamListMapper;

    private final EditExamBasicRequestMapper editExamBasicRequestMapper;

    private final ExcelService excelService;

    @Override
    public ExamResponseDto createExam(AddExamRequestDto addExamRequestDto) {
        if (addExamRequestDto.getLanguageSupports() == null || addExamRequestDto.getLanguageSupports().isEmpty()) {
            throw new BadRequestException("At least one language must be supported", "ERR_NO_LANGUAGES");
        }
        if (addExamRequestDto.getProblemRequests() == null || addExamRequestDto.getProblemRequests().isEmpty()) {
            throw new BadRequestException("At least one problem is required", "ERR_NO_PROBLEMS");
        }
        if (addExamRequestDto.getStartTime().before(Timestamp.from(Instant.now()))) {
            throw new BadRequestException("Start date cannot be in the past", "ERR_START_PAST");
        }
        if (addExamRequestDto.getStartTime().after(addExamRequestDto.getEndTime())) {
            throw new BadRequestException("Start date cannot be after end date", "ERR_START_AFTER_END");
        }

        Exam exam = addExamRequestMapper.mapTo(addExamRequestDto);
        if (examRepository.existsByTitleIgnoreCase(addExamRequestDto.getTitle())) {
            throw new BadRequestException("An exam with title '" + addExamRequestDto.getTitle() + "' already exists", "ERR_DUPLICATE_TITLE");
        }

        Set<Language> languages = languageService.getLanguagesByNameList(addExamRequestDto.getLanguageSupports());
        List<ExamProblemRequestDto> problemExams = addExamRequestDto.getProblemRequests();

        String code;
        do {
            code = UUIDGenerator.generateUUID();
        } while (examRepository.existsByCode(code));

        exam.setCode(code);
        exam.setLanguageSupport(languages);
        exam.setCreatedAt(Timestamp.from(Instant.now()));
        exam.setCreatedBy(userService.getCurrentUser());
        exam.setNoParticipant(0);
        exam.setStatus(ExamStatus.NOT_STARTED);

        List<ExamProblem> examProblems = new ArrayList<>();
        for (ExamProblemRequestDto problemExam : problemExams) {
            Problem problem = problemService.getProblemByExamProblemRequest(problemExam);
            if (!checkLanguageSupportEquals(languages, problem.getLanguageSupport())) {
                throw new BadRequestException("Language support of exam does not match problem: " + problem.getId(),
                        "ERR_LANGUAGE_MISMATCH");
            }
            ExamProblem examProblem = new ExamProblem();
            examProblem.setExam(exam);
            examProblem.setProblem(problem);
            examProblem.setPoint(problemExam.getProblemPoint());
            examProblems.add(examProblem);
        }

        exam = examRepository.save(exam);
        for (ExamProblem examProblem : examProblems) {
            examProblem.setId(new ExamProblemId(exam.getId(), examProblem.getProblem().getId()));
        }
        examProblemRepository.saveAll(examProblems);

        ExamResponseDto examResponseDto = getExamDetailByCode(code);
        eventPublisher.publishEvent(new ExamStartEvent(this, code, exam.getStartTime().toInstant()));
        return examResponseDto;
    }



    private boolean checkLanguageSupportEquals(Set<Language> examSupport, Set<Language> problemSupport) {
        return problemSupport.containsAll(examSupport);
    }

    private Exam getExamByCode(String code) {
        return examRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Exam not found", "Exam not found"));
    }

    @Override
    public ExamResponseDto editExam(AddExamRequestDto addExamRequestDto, String code) {
        addExamRequestDto.setStartTime(new Timestamp(addExamRequestDto.getStartTime().getTime()));
        addExamRequestDto.setEndTime(new Timestamp(addExamRequestDto.getEndTime().getTime()));
        Exam savedExam = getExamByCode(code);
        if (savedExam.getStatus() != ExamStatus.NOT_STARTED) {
            throw new BadRequestException("This exam is already started or ended",
                    "This exam is already started or ended");
        }
        if (addExamRequestDto.getStartTime().before(Timestamp.from(Instant.now()))) {
            throw new BadRequestException("Start date cannot be in the past", "Start date cannot be in the past");
        }
        if (addExamRequestDto.getStartTime().after(addExamRequestDto.getEndTime())) {
            throw new BadRequestException("Start date cannot be after end date", "Start date cannot be after end date");
        }

        Exam exam = addExamRequestMapper.mapTo(addExamRequestDto);
        exam.setId(savedExam.getId());
        exam.setCode(savedExam.getCode());
        exam.setCreatedAt(savedExam.getCreatedAt());
        exam.setCreatedBy(savedExam.getCreatedBy());
        exam.setUpdatedAt(Timestamp.from(Instant.now()));
        exam.setUpdatedBy(userService.getCurrentUser());
        exam.setStatus(ExamStatus.NOT_STARTED);
        exam.setNoParticipant(savedExam.getNoParticipant());

        Set<Language> languages = languageService.getLanguagesByNameList(addExamRequestDto.getLanguageSupports());
        List<ExamProblemRequestDto> problemExams = addExamRequestDto.getProblemRequests();

        exam.setLanguageSupport(languages);
        for (ExamProblemRequestDto problemExam : problemExams) {
            Problem problem = problemService.getProblemByExamProblemRequest(problemExam);
            if (!checkLanguageSupportEquals(languages, problem.getLanguageSupport())) {
                throw new BadRequestException("Language support of your exam does not match with your problem",
                        "Language support of your exam does not match with your problem");
            }
        }
        exam = examRepository.save(exam);
        examProblemRepository.deleteByExam(exam);
        List<ExamProblem> examProblems = new ArrayList();

        for (ExamProblemRequestDto problemExam : problemExams) {
            Problem problem = problemService.getProblemByExamProblemRequest(problemExam);
            ExamProblemId examProblemId = new ExamProblemId(exam.getId(), problem.getId());
            ExamProblem examProblem = new ExamProblem(examProblemId, exam, problem, problemExam.getProblemPoint());
            examProblems.add(examProblem);
        }
        examProblemRepository.saveAll(examProblems);

        eventPublisher.publishEvent(new ExamStartEvent(this, code, exam.getStartTime().toInstant()));
        ExamResponseDto examResponseDto = getExamDetailByCode(code);
        return examResponseDto;
    }

    @Override
    public void deleteExam(String code) {
        Exam savedExam = getExamByCode(code);
        if (savedExam.getStatus() != ExamStatus.NOT_STARTED) {
            throw new BadRequestException("This exam is already started or ended",
                    "This exam is already started or ended");
        }
        List<ExamParticipant> participants = examParticipantRepository.findByExam(savedExam);
        if (!participants.isEmpty()) {
            throw new BadRequestException("Can't delete this exam because it already has participant",
                    "Can't delete this exam because it already has participant");
        }
        examProblemRepository.deleteByExam(savedExam);
        examRepository.delete(savedExam);
    }

    @Override
    public Page<ExamListResponseDto> getListOfExam(FilterExamRequestDto filterExamRequestDto) {
        if ((filterExamRequestDto.getStart() != null && filterExamRequestDto.getEnd() == null)
                || (filterExamRequestDto.getStart() == null && filterExamRequestDto.getEnd() != null)) {
            throw new BadRequestException("Start and end date must be provided together",
                    "Start and end date must be provided together");
        }

        if (filterExamRequestDto.getTitle() != null)
            filterExamRequestDto.setTitle(filterExamRequestDto.getTitle().trim());

        if ((filterExamRequestDto.getStart() != null && filterExamRequestDto.getEnd() != null)
                && filterExamRequestDto.getStart().after(filterExamRequestDto.getEnd())) {
            throw new BadRequestException("Start date cannot be after end date", "Start date cannot be after end date");
        }
        Pageable pageable;
        String sortBy = filterExamRequestDto.getSortBy();
        if (sortBy != null
                && (sortBy.equals("createdAt") || sortBy.equals("startTime") || sortBy.equals("endTime")
                        || sortBy.equals("noParticipant"))) {
            Sort sort = filterExamRequestDto.getAscending().booleanValue() ? Sort.by(sortBy.toString()).ascending()
                    : Sort.by(sortBy.toString()).descending();
            pageable = PageRequest.of(filterExamRequestDto.getPage(),
                    filterExamRequestDto.getSize() == null ? 5 : filterExamRequestDto.getSize().intValue(), sort);
        } else {
            pageable = PageRequest.of(filterExamRequestDto.getPage(),
                    filterExamRequestDto.getSize() == null ? 5 : filterExamRequestDto.getSize().intValue());
        }
        Timestamp startTimestamp = filterExamRequestDto.getStart() != null ? filterExamRequestDto.getStart()
                : Timestamp.valueOf("1970-01-01 00:00:00");
        Timestamp endTimestamp = filterExamRequestDto.getEnd() != null ? filterExamRequestDto.getEnd()
                : Timestamp.valueOf("2100-01-01 00:00:00");
        Page<Exam> examList = examRepository
                .searchExam(
                        filterExamRequestDto.getTitle().toLowerCase(),
                        filterExamRequestDto.getStatus(),
                        startTimestamp,
                        endTimestamp,
                        pageable);
        return examList.map(examListResponseMapper::mapFrom);
    }

    @Override
    public ExamResponseDto getExamDetailByCode(String code) {
        Exam exam = getExamByCode(code);
        ExamResponseDto examResponseDto = examResponseMapper.mapFrom(exam);
        List<ExamProblemResponseDto> examProblems = new ArrayList<>();
        int totalSubmission = 0;
        for (ExamProblem examProblem : examProblemRepository.findByExam(exam)) {
            ExamProblemResponseDto examProblemResponseDto = new ExamProblemResponseDto();
            examProblemResponseDto.setId(examProblem.getProblem().getId());
            examProblemResponseDto.setProblemLink(examProblem.getProblem().getLink());
            examProblemResponseDto.setProblemTitle(examProblem.getProblem().getTitle());
            examProblemResponseDto.setProblemPoint(examProblem.getPoint());
            examProblemResponseDto
                    .setNoSubmission(examSubmissionRepository.countNoSubmissionProblem(exam, examProblem.getProblem()));
            totalSubmission += examProblemResponseDto.getNoSubmission();
            examProblems.add(examProblemResponseDto);
        }
        examResponseDto.setNoSubmission(totalSubmission);
        examResponseDto.setLanguageSupports(languageService.getLanguageNamesByList(exam.getLanguageSupport()));
        examResponseDto.setProblems(examProblems);
        return examResponseDto;
    }

    public boolean checkDuplicateTimeExam(Exam exam, Users user) {
        return !examParticipantRepository
                .checkDuplicateTimeExam(exam.getCode(), exam.getStartTime(), exam.getEndTime(), user).isEmpty();
    }

    @Override
    public void enrollExam(String code) {
        Users currentUser = userService.getCurrentUser();
        ExamParticipant examParticipant = new ExamParticipant();
        Exam exam = getExamByCode(code);
        Timestamp now = new Timestamp(System.currentTimeMillis());

        if (checkDuplicateTimeExam(exam, currentUser)) {
            throw new BadRequestException("You have enrolled for another exam that overlaps with this one",
                    "You have enrolled for another exam that overlaps with this one");
        }

        if (exam.getStartTime().before(now)) {
            throw new BadRequestException("This exam has already started",
                    "This exam has already started");
        }

        if (exam.getStatus() != ExamStatus.NOT_STARTED) {
            throw new BadRequestException("This exam is already started or ended",
                    "This exam is already started or ended");
        }

        if (examParticipantRepository.findByExamAndParticipant(exam, currentUser).isPresent()) {
            throw new BadRequestException("You have already enrolled this exam",
                    "You have already enrolled this exam");
        }

        ExamParticipantId examParticipantId = new ExamParticipantId(exam.getId(), currentUser.getId());
        examParticipant.setId(examParticipantId);
        examParticipant.setExam(exam);
        examParticipant.setParticipant(currentUser);
        examParticipant.setGrade(0);
        examParticipantRepository.save(examParticipant);

        exam.setNoParticipant(exam.getNoParticipant() + 1);
        examRepository.save(exam);

    }

    @Override
    public List<String> getCodeFromExamReadyToStarted() {
        Timestamp now = Timestamp.from(Instant.now());
        Timestamp maxTime = new Timestamp(now.getTime() + 10 * 1000);
        return examRepository.getCodeFromExamReadyToStarted(now, maxTime);
    }

    @Transactional
    @Override
    public ExamDetailResponseDto getProblemDetailInExam(String code) {
        ExamDetailResponseDto examDetailResponseDto = new ExamDetailResponseDto();
        List<ExamProblemDetailResponseDto> result = new ArrayList();
        Exam exam = getExamByCode(code);
        Map<String, Object> mapError = new HashMap<String, Object>();

        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp after5Minutes = new Timestamp(now.getTime() + 60 * 1000 * 5);

        if (exam.getStatus() != ExamStatus.IN_PROGRESS) {
            mapError.put("error", "The exam has not started or already ended.");
            publisher.sendError(mapError);
        }

        else if (!(exam.getStartTime().getTime() >= now.getTime() - 1000 * 10
                && exam.getStartTime().getTime() <= after5Minutes.getTime())) {
            mapError.put("error", "The test has not started yet or is already in progress.");
            publisher.sendError(mapError);
        } else {
            List<ExamProblem> examProblems = examProblemRepository.findByExam(exam);

            for (int i = 0; i < examProblems.size(); i++) {
                Problem problem = examProblems.get(i).getProblem();
                ExamProblemDetailResponseDto examProblemDetailResponseDto = new ExamProblemDetailResponseDto();
                examProblemDetailResponseDto.setProblemLink(problem.getLink());
                examProblemDetailResponseDto.setProblemTitle(problem.getTitle());
                examProblemDetailResponseDto.setProblemDescription(problem.getDescription());

                Set<Language> languageSupports = exam.getLanguageSupport();
                List<ExamCompileInformationResponseDto> examCompileInformationResponseDtos = new ArrayList();

                for (Language language : languageSupports) {
                    ExamCompileInformationResponseDto examCompileInformationResponseDto = problemTestCaseService
                            .getExamProblemCompileInformationByProblem(problem, language);
                    examCompileInformationResponseDtos.add(examCompileInformationResponseDto);
                }

                examProblemDetailResponseDto.setCompileInformation(examCompileInformationResponseDtos);
                result.add(examProblemDetailResponseDto);
            }
        }
        examDetailResponseDto.setDuration(getMinuteDifference(exam.getStartTime(), exam.getEndTime()) * 60);
        examDetailResponseDto.setProblems(result);
        log.info(examDetailResponseDto);
        return examDetailResponseDto;
    }

    @Override
    public double submitExam(List<SubmitExamRequestDto> submitExamRequestDto, String code,
            String username) {
        Exam exam = getExamByCode(code);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp after5Minutes = new Timestamp(now.getTime() + 60 * 1000 * 5);
        Map<String, Object> mapError = new HashMap<String, Object>();

        if (exam.getStartTime().getTime() > now.getTime()) {
            mapError.put("username", username);
            mapError.put("error", "Exam has not started yet");
            publisher.sendError(mapError);
            return 0;
        }

        else if (exam.getEndTime().getTime() <= after5Minutes.getTime()) {
            mapError.put("username", username);
            mapError.put("error", "Exam has already ended");
            publisher.sendError(mapError);
            return 0;
        }

        else if (exam.getStatus() != ExamStatus.IN_PROGRESS) {
            mapError.put("username", username);
            mapError.put("error", "The exam has not started or already ended.");
            publisher.sendError(mapError);
            return 0;
        }

        Users currentUser = userService.getUserByUsernameOrEmail(username);
        List<ExamParticipant> examParticipants = examParticipantRepository.findByExam(exam);
        ExamParticipant examParticipant = new ExamParticipant();
        if (!examParticipantRepository.findByExamAndParticipant(exam, currentUser).isPresent()) {
            mapError.put("username", username);
            mapError.put("error", "Exam not found");
            publisher.sendError(mapError);
            return 0;
        } else {
            examParticipant = examParticipantRepository.findByExamAndParticipant(exam, currentUser).get();
        }
        boolean isCurrentUserParticipated = false;

        if (currentUser.getRole() == UserRole.STUDENT) {
            for (ExamParticipant ep : examParticipants) {
                if (ep.getParticipant().getId().longValue() == currentUser.getId().longValue()) {
                    isCurrentUserParticipated = true;
                    break;
                }
            }
        } else {
            isCurrentUserParticipated = true;
        }

        if (!isCurrentUserParticipated) {
            mapError.put("username", username);
            mapError.put("error", "You are not enrolled in this exam");
            publisher.sendError(mapError);
            return 0;
        }

        for (int i = 0; i < submitExamRequestDto.size(); i++) {
            Problem problem = problemService.getProblemByLink(submitExamRequestDto.get(i).getProblemLink());
            ExamProblem examProblem = new ExamProblem();
            if (!examProblemRepository.findByExamAndProblem(exam, problem).isPresent()) {
                mapError.put("username", username);
                mapError.put("error", "Exam not found");
                publisher.sendError(mapError);
                return 0;
            } else {
                examProblem = examProblemRepository.findByExamAndProblem(exam, problem).get();
            }
            if (examSubmissionRepository.findByExamParticipantAndProblem(examParticipant, problem).isPresent()) {
                mapError.put("username", username);
                mapError.put("error", "You can only submit exam once");
                publisher.sendError(mapError);
                return 0;
            }
            if (examProblem != null) {
                submitExamRequestDto.get(i).setPoint(examProblem.getPoint());
            }
        }
        if (mapError.isEmpty()) {
            ExamResultOverviewResponseDto examResultResponseDto = problemService.submitExam(submitExamRequestDto,
                    currentUser);
            List<ProblemResultOverviewResponseDto> problemResultDetailResponseDtoList = examResultResponseDto
                    .getProblemResults();

            for (ProblemResultOverviewResponseDto problemResultDetailResponseDto : problemResultDetailResponseDtoList) {
                ExamSubmission examSubmission = new ExamSubmission();
                Problem problem = problemService.getProblemById(problemResultDetailResponseDto.getId());
                ExamSubmissionId examSubmissionId = new ExamSubmissionId(examParticipant.getId(), problem.getId());

                examSubmission.setId(examSubmissionId);
                examSubmission.setProblem(problem);
                examSubmission.setExamParticipant(examParticipant);
                examSubmission.setProblemSubmission(problemSubmissionService
                        .getProblemSubmissionById(problemResultDetailResponseDto.getSubmissionId()));
                examSubmission.setPoint(problemResultDetailResponseDto.getPoint());
                examSubmissionRepository.save(examSubmission);

            }
            examParticipant.setGrade(examResultResponseDto.getGrade());
            examParticipantRepository.save(examParticipant);

            return examResultResponseDto.getGrade();

        }
        return 0;
    }

    @Override
    public Object generateTokenForExam(String code) {
        Exam exam = getExamByCode(code);
        List<ExamParticipant> examParticipants = examParticipantRepository.findByExam(exam);

        boolean isCurrentUserParticipated = false;
        Users currentUser = userService.getCurrentUser();

        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp after5Minutes = new Timestamp(now.getTime() + 60 * 1000 * 5);

        if (exam.getStatus() != ExamStatus.NOT_STARTED) {
            throw new BadRequestException("The exam has already started or ended.",
                    "The exam has already started or ended.");
        }
        if (exam.getStartTime().getTime() <= now.getTime()) {
            throw new BadRequestException("The exam has already started or ended.",
                    "The exam has already started or ended.");
        }
        String formattedDate = sdf.format(exam.getStartTime());
        if (!(after5Minutes.getTime() >= exam.getStartTime().getTime())) {
            throw new ExamNotReadyToStartException("The exam is not ready to start. Time start is " + formattedDate,
                    "The exam is not ready to start. Time start is " + formattedDate,
                    formattedDate);
        }

        for (ExamParticipant ep : examParticipants) {
            if (ep.getParticipant().getId().longValue() == currentUser.getId().longValue()) {
                isCurrentUserParticipated = true;
                break;
            }
        }

        if (!isCurrentUserParticipated) {
            throw new ForbiddenException("You are not enrolled in this exam", "You are not enrolled in this exam");
        }

        Map<String, String> response = new HashMap<>();
        response.put("token", tokenService.generateAccessToken(currentUser.getUsername()));
        response.put("code", code);
        response.put("username", currentUser.getUsername());
        response.put("startTime", formattedDate);
        return response;
    }

    @Override
    public ExamDetailResponseDto startExam(String code) {
        Exam exam = getExamByCode(code);
        exam.setStatus(ExamStatus.IN_PROGRESS);
        examRepository.save(exam);

        return getProblemDetailInExam(code);
    }

    @Override
    public RunProblemResponseDto runExam(String examCode, String link,
            ProblemCompileRequestDto problemCompileRequestDto) {
        Exam exam = getExamByCode(examCode);
        List<ExamProblem> examProblem = examProblemRepository.findByExam(exam);
        boolean isExamContainedProblem = false;

        for (ExamProblem ep : examProblem) {
            if (ep.getProblem().getLink().equals(link)) {
                isExamContainedProblem = true;
                break;
            }
        }

        if (!isExamContainedProblem) {
            throw new BadRequestException("The problem is not in the exam", "The problem is not in the exam");
        }
        return problemService.runExam(link, problemCompileRequestDto);
    }

    @Override
    public void endExam() {
        Timestamp beforeNow10P = new Timestamp(System.currentTimeMillis() - 1000 * 1 * 60);
        List<Exam> exams = examRepository.getExamAlreadyEnded(beforeNow10P);
        for (Exam exam : exams) {
            exam.setStatus(ExamStatus.END);
            examRepository.save(exam);
        }
    }

    @Override
    public ExamResultOverviewResponseDto viewResult(String code) {
        Users currentUser = userService.getCurrentUser();
        Exam exam = getExamByCode(code);
        if (exam.getStatus() != ExamStatus.END) {
            throw new BadRequestException("The exam has not ended yet.", "The exam has not ended yet.");
        }
        ExamResultOverviewResponseDto examResultOverviewResponseDto = new ExamResultOverviewResponseDto();

        ExamParticipant examParticipant = examParticipantRepository.findByExamAndParticipant(exam, currentUser)
                .orElseThrow(() -> new NotFoundException("Exam not found", "Exam not found"));
        examResultOverviewResponseDto.setGrade(examParticipant.getGrade());
        List<ProblemResultOverviewResponseDto> problemResultDetailResponseDtoList = new ArrayList<>();
        List<ExamProblem> examProblems = examProblemRepository.findByExam(exam);
        examResultOverviewResponseDto.setNoProblems(examProblems.size());
        for (int i = 0; i < examProblems.size(); i++) {
            ProblemResultOverviewResponseDto problemResultOverviewResponseDto = new ProblemResultOverviewResponseDto();
            Problem problem = examProblems.get(i).getProblem();
            Optional<ExamSubmission> examSubmissionOptional = examSubmissionRepository
                    .findByExamParticipantAndProblem(examParticipant, problem);

            problemResultOverviewResponseDto.setId(problem.getId());
            problemResultOverviewResponseDto.setLink(problem.getLink());
            problemResultOverviewResponseDto.setTitle(problem.getTitle());
            if (examSubmissionOptional.isPresent()) {
                ExamSubmission examSubmission = examSubmissionOptional.get();
                problemResultOverviewResponseDto.setPoint(examSubmission.getPoint());
                ProblemSubmission problemSubmission = examSubmission.getProblemSubmission();
                List<TestCase> testCases = problemTestCaseService.getTestCaseByProblemAndLanguage(problem,
                        problemSubmission.getLanguage());
                problemResultOverviewResponseDto.setNoTestCasePassed(problemSubmission.getNoTestCasePassed());
                problemResultOverviewResponseDto
                        .setSubmissionResponseDto(problemService.getSubmissionDetail(problemSubmission.getId()));
                double percentPassed = (double) problemResultOverviewResponseDto.getNoTestCasePassed()
                        / testCases.size();
                problemResultOverviewResponseDto.setPercentPassed(formatDouble(percentPassed * 100) + "%");
            }
            problemResultDetailResponseDtoList.add(problemResultOverviewResponseDto);
        }
        examResultOverviewResponseDto.setProblemResults(problemResultDetailResponseDtoList);

        return examResultOverviewResponseDto;
    }

    public String formatDouble(double value) {
        if (value % 1 == 0) {
            return String.format("%.0f", value); // Nếu là số nguyên, hiển thị không có phần thập phân
        } else if ((value * 10) % 1 == 0) {
            return String.format("%.1f", value); // Nếu có 1 số sau dấu phẩy, giữ nguyên 1 số
        } else {
            return String.format("%.2f", value); // Nếu có nhiều hơn 1 số sau dấu phẩy, giữ 2 số
        }
    }

    @Override
    public Page<ExamListStudentResponseDto> getListExam(ExamStatus status, int page, Integer size, String title,
            Date start, Date end) {
        if ((start != null && end == null)
                || (start == null && end != null)) {
            throw new BadRequestException("Start and end date must be provided together",
                    "Start and end date must be provided together");
        }
        if ((start != null && end != null)
                && start.after(end)) {
            throw new BadRequestException("Start date cannot be after end date", "Start date cannot be after end date");
        }
        Pageable pageable = PageRequest.of(page,
                size == null ? 5 : size.intValue(), Sort.by("exam.startTime").descending());

        Timestamp startTimestamp = start != null ? new Timestamp(start.getTime())
                : Timestamp.valueOf("1970-01-01 00:00:00");
        Timestamp endTimestamp = end != null ? new Timestamp(end.getTime())
                : Timestamp.valueOf("2100-01-01 00:00:00");

        Users user = userService.getCurrentUser();
        String tileLowerCase = title != null ? title.toLowerCase() : null;
        Page<ExamParticipant> exams = examParticipantRepository.findByStatus(status, user, tileLowerCase, startTimestamp,
                endTimestamp, pageable);

        Users currentUser = userService.getCurrentUser();
        Page<ExamListStudentResponseDto> results = exams.map(examListStudentResponseMapper::mapFrom);
        for (ExamListStudentResponseDto examListStudentResponseDto : results) {
            if (examListStudentResponseDto.getStatus() == ExamStatus.NOT_STARTED) {
                examListStudentResponseDto.setResult("This exam does't have result yet");
            } else {
                Optional<ExamParticipant> examParticipantOptional = examParticipantRepository
                        .findByExamAndParticipant(
                                getExamByCode(examListStudentResponseDto.getCode()),
                                currentUser);
                if (examParticipantOptional.isPresent()) {
                    if (!examSubmissionRepository.findByExamParticipant(examParticipantOptional.get()).isEmpty()) {
                        examListStudentResponseDto.setResult(String.valueOf(examParticipantOptional.get().getGrade()));
                    }
                }
            }
        }
        return results;
    }

    @Override
    public List<Map<String, String>> getAllParticipantsInExam(String code) {
        List<Map<String, String>> results = new ArrayList();
        Exam exam = getExamByCode(code);

        List<ExamParticipant> examParticipants = examParticipantRepository.findByExam(exam);
        for (ExamParticipant ep : examParticipants) {
            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(ep.getParticipant().getId()));
            map.put("avatar", s3Service.getPresignedUrl(ep.getParticipant().getAvatar()));
            map.put("username", ep.getParticipant().getUsername());
            map.put("fullname", ep.getParticipant().getFullname());
            map.put("grade", ep.getGrade() + "");
            results.add(map);
        }
        return results;
    }

    @Override
    public ExamResultOverviewResponseDto viewResultOfASpecificParticpant(String code, Long userId) {
        Users user = userService.getUserById(userId);
        Exam exam = getExamByCode(code);
        ExamResultOverviewResponseDto examResultOverviewResponseDto = new ExamResultOverviewResponseDto();
        if (exam.getStatus() == ExamStatus.NOT_STARTED) {
            throw new BadRequestException("This exam is not started yet", "This exam is not started yet");
        }
        ExamParticipant examParticipant = examParticipantRepository.findByExamAndParticipant(exam, user)
                .orElseThrow(() -> new NotFoundException("Exam not found", "Exam not found"));
        examResultOverviewResponseDto.setGrade(examParticipant.getGrade());
        List<ProblemResultOverviewResponseDto> problemResultDetailResponseDtoList = new ArrayList<>();
        List<ExamProblem> examProblems = examProblemRepository.findByExam(exam);
        examResultOverviewResponseDto.setNoProblems(examProblems.size());
        for (int i = 0; i < examProblems.size(); i++) {
            ProblemResultOverviewResponseDto problemResultOverviewResponseDto = new ProblemResultOverviewResponseDto();
            Problem problem = examProblems.get(i).getProblem();
            Optional<ExamSubmission> examSubmissionOptional = examSubmissionRepository
                    .findByExamParticipantAndProblem(examParticipant, problem);

            problemResultOverviewResponseDto.setId(problem.getId());
            problemResultOverviewResponseDto.setLink(problem.getLink());
            problemResultOverviewResponseDto.setTitle(problem.getTitle());
            if (examSubmissionOptional.isPresent()) {
                ExamSubmission examSubmission = examSubmissionOptional.get();
                problemResultOverviewResponseDto.setPoint(examSubmission.getPoint());
                ProblemSubmission problemSubmission = examSubmission.getProblemSubmission();
                List<TestCase> testCases = problemTestCaseService.getTestCaseByProblemAndLanguage(problem,
                        problemSubmission.getLanguage());
                problemResultOverviewResponseDto.setNoTestCasePassed(problemSubmission.getNoTestCasePassed());
                problemResultOverviewResponseDto
                        .setSubmissionResponseDto(problemService.getSubmissionDetail(problemSubmission.getId()));
                double percentPassed = (double) problemResultOverviewResponseDto.getNoTestCasePassed()
                        / testCases.size();
                problemResultOverviewResponseDto.setPercentPassed(formatDouble(percentPassed * 100) + "%");
            }
            problemResultDetailResponseDtoList.add(problemResultOverviewResponseDto);
        }
        examResultOverviewResponseDto.setProblemResults(problemResultDetailResponseDtoList);

        return examResultOverviewResponseDto;
    }

    private long getMinuteDifference(Timestamp ts1, Timestamp ts2) {
        Instant instant1 = ts1.toInstant();
        Instant instant2 = ts2.toInstant();
        return Duration.between(instant1, instant2).toMinutes();
    }

    @Override
    public void sendNotiToUserExamAboutToStart() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp after30Minutes = new Timestamp(now.getTime() + 1000 * 30 * 60);
        Timestamp after5Minutes = new Timestamp(now.getTime() + 1000 * 5 * 60);

        List<Exam> examList = examRepository.getExamAboutToStart(now, after30Minutes);
        for (int i = 0; i < examList.size(); i++) {
            List<ExamParticipant> participants = examParticipantRepository.findByExam(examList.get(i));
            for (int j = 0; j < participants.size(); j++) {
                Users user = participants.get(j).getParticipant();
                String formattedDate = sdf.format(examList.get(i).getStartTime());
                if (!redisService.isUserRemindedForExam(user.getUsername(), examList.get(i).getCode(), 30)) {
                    redisService.saveKeyCheckExamReminder(user.getUsername(), examList.get(i).getCode(), 30);
                    notificationService.saveNotification(user, "There is a exam that will start on " + formattedDate,
                            "", NotificationType.SYSTEM);
                    emailService.sendEmailNotifyExam30Minutes(user.getEmail(), "[KODEHOLIK] Exam Reminder",
                            user.getUsername(),
                            formattedDate, examList.get(i).getCode(),
                            getMinuteDifference(examList.get(i).getStartTime(), examList.get(i).getEndTime()));
                }
            }
        }

        List<Exam> examList5Minutes = examRepository.getExamAboutToStart(now, after5Minutes);
        for (int i = 0; i < examList5Minutes.size(); i++) {
            List<ExamParticipant> participants = examParticipantRepository.findByExam(examList5Minutes.get(i));
            for (int j = 0; j < participants.size(); j++) {
                Users user = participants.get(j).getParticipant();
                String formattedDate = sdf.format(examList.get(i).getStartTime());
                if (!redisService.isUserRemindedForExam(user.getUsername(), examList5Minutes.get(i).getCode(), 5)) {
                    redisService.saveKeyCheckExamReminder(user.getUsername(), examList5Minutes.get(i).getCode(), 5);
                    notificationService.saveNotification(user, "There is a exam that will start on " + formattedDate,
                            "", NotificationType.SYSTEM);
                    emailService.sendEmailNotifyExam5Minutes(user.getEmail(), "[KODEHOLIK] Exam Reminder",
                            user.getUsername(),
                            formattedDate, examList5Minutes.get(i).getCode());
                }
            }
        }
    }

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm");

    @Override
    public List<Exam> getAllPendingExam() {
        return examRepository.findByStatus(ExamStatus.NOT_STARTED);
    }

    @Override
    public List<NotStartedExamListDto> getAllPendingExamNotOverlapTime() {
        Users currentUsers = userService.getCurrentUser();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        List<Exam> exams = examRepository.findByStatus(ExamStatus.NOT_STARTED);
        List<NotStartedExamListDto> result = exams.stream()
                .map(notStartedExamListMapper::mapFrom)
                .collect(Collectors.toList());
        for (int i = 0; i < exams.size(); i++) {
            if (now.after(exams.get(i).getStartTime())
                    || examParticipantRepository.findByExamAndParticipant(exams.get(i), currentUsers).isPresent()) {
                result.remove(i);
                exams.remove(i);
                i--;
            } else {
                if (checkDuplicateTimeExam(exams.get(i), currentUsers)) {
                    result.get(i).setCanEnroll(false);
                } else {
                    result.get(i).setCanEnroll(true);
                }
            }

        }
        return result;
    }

    @Override
    public void unenrollExam(String code) {
        Exam exam = getExamByCode(code);
        Users user = userService.getCurrentUser();

        if (exam.getStatus() != ExamStatus.NOT_STARTED) {
            throw new BadRequestException("Cannot unenroll an exam that has started",
                    "Cannot unenroll an exam that has started");
        }

        ExamParticipant examParticipant = examParticipantRepository.findByExamAndParticipant(exam, user)
                .orElseThrow(() -> new BadRequestException("You haven't enrolled in this exam",
                        "You haven't enrolled in this exam"));
        examParticipantRepository.delete(examParticipant);
        exam.setNoParticipant(exam.getNoParticipant() - 1 > 0 ? exam.getNoParticipant() - 1 : 0);
    }

    @Override
    public ExamResponseDto editExamBasic(EditExamBasicRequestDto editExamBasicRequestDto, String code) {
        editExamBasicRequestDto
                .setStartTime(new Timestamp(editExamBasicRequestDto.getStartTime().getTime() - 1000 * 60 * 60 * 7));
        editExamBasicRequestDto
                .setEndTime(new Timestamp(editExamBasicRequestDto.getEndTime().getTime() - 1000 * 60 * 60 * 7));
        Exam savedExam = getExamByCode(code);
        if (savedExam.getStatus() != ExamStatus.NOT_STARTED) {
            throw new BadRequestException("This exam is already started or ended",
                    "This exam is already started or ended");
        }
        if (editExamBasicRequestDto.getStartTime().before(Timestamp.from(Instant.now()))) {
            throw new BadRequestException("Start date cannot be in the past", "Start date cannot be in the past");
        }
        if (editExamBasicRequestDto.getStartTime().after(editExamBasicRequestDto.getEndTime())) {
            throw new BadRequestException("Start date cannot be after end date", "Start date cannot be after end date");
        }

        Exam exam = editExamBasicRequestMapper.mapTo(editExamBasicRequestDto);
        exam.setId(savedExam.getId());
        exam.setCode(savedExam.getCode());
        exam.setCreatedAt(savedExam.getCreatedAt());
        exam.setCreatedBy(savedExam.getCreatedBy());
        exam.setUpdatedAt(Timestamp.from(Instant.now()));
        exam.setUpdatedBy(userService.getCurrentUser());
        exam.setStatus(ExamStatus.NOT_STARTED);
        exam.setNoParticipant(savedExam.getNoParticipant());
        exam.setLanguageSupport(savedExam.getLanguageSupport());

        exam = examRepository.save(exam);

        eventPublisher.publishEvent(new ExamStartEvent(this, code, exam.getStartTime().toInstant()));
        ExamResponseDto examResponseDto = getExamDetailByCode(code);
        return examResponseDto;
    }

    @Override
    public ExamResponseDto editExamProblem(EditExamProblemRequestDto editExamProblemRequestDto, String code) {

        Exam exam = getExamByCode(code);

        Set<Language> languages = languageService
                .getLanguagesByNameList(editExamProblemRequestDto.getLanguageSupports());
        List<ExamProblemRequestDto> problemExams = editExamProblemRequestDto.getProblemRequests();

        exam.setLanguageSupport(languages);
        exam = examRepository.save(exam);

        examProblemRepository.deleteByExam(exam);
        for (ExamProblemRequestDto problemExam : problemExams) {
            Problem problem = problemService.getProblemByExamProblemRequest(problemExam);
            ExamProblemId examProblemId = new ExamProblemId(exam.getId(), problem.getId());
            ExamProblem examProblem = new ExamProblem(examProblemId, exam, problem, problemExam.getProblemPoint());
            if (!checkLanguageSupportEquals(languages, problem.getLanguageSupport())) {
                throw new BadRequestException("Language support of your exam does not match with your problem",
                        "Language support of your exam does not match with your problem");
            }
            examProblemRepository.save(examProblem);
        }

        eventPublisher.publishEvent(new ExamStartEvent(this, code, exam.getStartTime().toInstant()));
        ExamResponseDto examResponseDto = getExamDetailByCode(code);
        return examResponseDto;
    }

    @Override
    public byte[] generateExamResultFile(String code) {
        Exam exam = getExamByCode(code);
        if (exam.getStatus() == ExamStatus.NOT_STARTED) {
            throw new BadRequestException("This exam has not started yet", "This exam has not started yet");
        }
        List<ExamParticipant> examParticipants = examParticipantRepository.findByExam(exam);
        List<ExamResultExcelDto> results = new ArrayList<>();
        List<ExamProblem> examProblems = examProblemRepository.findByExam(exam);
        for (ExamParticipant examParticipant : examParticipants) {
            ExamResultExcelDto examResultExcelDto = new ExamResultExcelDto();
            examResultExcelDto.setUsername(examParticipant.getParticipant().getUsername());
            examResultExcelDto.setFullname(examParticipant.getParticipant().getFullname());
            examResultExcelDto.setGrade(examParticipant.getGrade());
            List<ExamSubmission> examSubmissions = examSubmissionRepository.findByExamParticipant(examParticipant);
            List<ProblemPoint> problemPoints = new ArrayList<>();
            if (examSubmissions.isEmpty()) {
                examResultExcelDto.setSubmitted(false);
                for (ExamProblem examProblem : examProblems) {
                    ProblemPoint problemPoint = new ProblemPoint();
                    problemPoint.setTitle(examProblem.getProblem().getTitle());
                    problemPoint.setPoint(0);
                    problemPoints.add(problemPoint);
                }
            } else {
                examResultExcelDto.setSubmitted(true);
                for (ExamSubmission examSubmission : examSubmissions) {
                    ProblemPoint problemPoint = new ProblemPoint();
                    problemPoint.setTitle(examSubmission.getProblem().getTitle());
                    problemPoint.setPoint(examSubmission.getPoint());
                    problemPoints.add(problemPoint);
                }
            }
            examResultExcelDto.setProblemPoints(problemPoints);
            results.add(examResultExcelDto);
        }

        return excelService.generateExamResultFile(results);
    }

    @Override
    public ExamResultOverviewDto getResultOverviewInformation(String code) {
        ExamResultOverviewDto result = new ExamResultOverviewDto();
        Exam exam = getExamByCode(code);
        List<ExamParticipant> examParticipants = examParticipantRepository.findByExam(exam);
        int size = examParticipants.size();
        int excellentGrade = 0, goodGrade = 0, badGrade = 0;

        double grade = examSubmissionRepository.getAvgGrade(exam);
        result.setAvgGrade(formatDouble(grade));

        int numberSubmitted = examSubmissionRepository.getNumberSubmitted(exam).size();
        log.info(numberSubmitted);
        result.setSubmittedPercent(formatDouble(numberSubmitted * 100 / size) + "%");

        List<Object[]> gradeDistribution = examParticipantRepository.getGradeDistribution(exam);
        List<Object[]> gradeDistributionFormatted = new ArrayList<>();
        for (int i = 0; i < gradeDistribution.size(); i++) {
            if (((Number) gradeDistribution.get(i)[0]).doubleValue() < 6) {
                badGrade += ((Number) gradeDistribution.get(i)[1]).intValue();
            } else if (((Number) gradeDistribution.get(i)[0]).doubleValue() >= 6
                    && ((Number) gradeDistribution.get(i)[0]).doubleValue() < 8) {
                goodGrade += ((Number) gradeDistribution.get(i)[1]).intValue();
            } else {
                excellentGrade += ((Number) gradeDistribution.get(i)[1]).intValue();
            }
            Object[] gradeMapFormatted = new Object[2];
            gradeMapFormatted[0] = gradeDistribution.get(i)[0];
            gradeMapFormatted[1] = formatDouble(((Number) gradeDistribution.get(i)[1]).doubleValue() * 100 / size)
                    + "%";
            gradeDistributionFormatted.add(gradeMapFormatted);
        }

        result.setGradeDistribution(gradeDistributionFormatted);
        result.setExcellentGradePercent(formatDouble(excellentGrade * 100 / size) + "%");
        result.setGoodGradePercent(formatDouble(goodGrade * 100 / size) + "%");
        result.setBadGradePercent(formatDouble(badGrade * 100 / size) + "%");

        List<Object[]> avgProblemPoint = examSubmissionRepository.getAvgProblemPoint(exam);
        List<Object[]> avgProblemPointFormatted = new ArrayList<>();

        for (int i = 0; i < avgProblemPoint.size(); i++) {
            Object[] problemPointFormatted = new Object[3];
            problemPointFormatted[0] = avgProblemPoint.get(i)[0];
            problemPointFormatted[1] = formatDouble((double) avgProblemPoint.get(i)[1]);
            problemPointFormatted[2] = formatDouble((double) avgProblemPoint.get(i)[2]);
            avgProblemPointFormatted.add(problemPointFormatted);
        }
        result.setAvgProblemPoints(avgProblemPointFormatted);
        return result;
    }

}
