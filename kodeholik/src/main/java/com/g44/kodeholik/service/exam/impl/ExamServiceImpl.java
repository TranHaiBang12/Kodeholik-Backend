package com.g44.kodeholik.service.exam.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.exam.AddExamRequestDto;
import com.g44.kodeholik.model.dto.request.exam.ExamProblemRequestDto;
import com.g44.kodeholik.model.dto.request.exam.FilterExamRequestDto;
import com.g44.kodeholik.model.dto.request.problem.ProblemCompileRequestDto;
import com.g44.kodeholik.model.dto.response.exam.ExamListResponseDto;
import com.g44.kodeholik.model.dto.response.exam.ExamProblemResponseDto;
import com.g44.kodeholik.model.dto.response.exam.ExamResponseDto;
import com.g44.kodeholik.model.entity.exam.Exam;
import com.g44.kodeholik.model.entity.exam.ExamProblem;
import com.g44.kodeholik.model.entity.exam.ExamProblemId;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSubmission;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.model.enums.exam.ExamStatus;
import com.g44.kodeholik.repository.exam.ExamProblemRepository;
import com.g44.kodeholik.repository.exam.ExamRepository;
import com.g44.kodeholik.service.exam.ExamService;
import com.g44.kodeholik.service.problem.ProblemService;
import com.g44.kodeholik.service.setting.LanguageService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.mapper.request.exam.AddExamRequestMapper;
import com.g44.kodeholik.util.mapper.response.exam.ExamListResponseMapper;
import com.g44.kodeholik.util.mapper.response.exam.ExamResponseMapper;
import com.g44.kodeholik.util.uuid.UUIDGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;

    private final ExamProblemRepository examProblemRepository;

    private final AddExamRequestMapper addExamRequestMapper;

    private final ExamResponseMapper examResponseMapper;

    private final ExamListResponseMapper examListResponseMapper;

    private final LanguageService languageService;

    private final ProblemService problemService;

    private final UserService userService;

    @Override
    public ExamResponseDto createExam(AddExamRequestDto addExamRequestDto) {
        Exam exam = addExamRequestMapper.mapTo(addExamRequestDto);

        if (addExamRequestDto.getStartTime().before(Timestamp.from(Instant.now()))) {
            throw new BadRequestException("Start date cannot be in the past", "Start date cannot be in the past");
        }
        if (addExamRequestDto.getStartTime().after(addExamRequestDto.getEndTime())) {
            throw new BadRequestException("Start date cannot be after end date", "Start date cannot be after end date");
        }

        Set<Language> languages = languageService.getLanguagesByNameList(addExamRequestDto.getLanguageSupports());
        List<ExamProblemRequestDto> problemExams = addExamRequestDto.getProblemRequests();

        String code = UUIDGenerator.generateUUID();
        List<Exam> exams = examRepository.findAll();

        boolean isDuplicateCode = false;
        while (true) {
            code = UUIDGenerator.generateUUID();
            for (int i = 0; i < exams.size(); i++) {
                if (exams.get(i).getCode().equals(code)) {
                    isDuplicateCode = true;
                    break;
                }
            }
            if (!isDuplicateCode) {
                break;
            }
        }

        exam.setCode(code);
        exam.setLanguageSupport(languages);
        exam.setCreatedAt(Timestamp.from(Instant.now()));
        exam.setCreatedBy(userService.getCurrentUser());
        exam.setStatus(ExamStatus.NOT_STARTED);

        exam = examRepository.save(exam);

        for (ExamProblemRequestDto problemExam : problemExams) {
            Problem problem = problemService.getProblemByExamProblemRequest(problemExam);
            if (!checkLanguageSupportEquals(languages, problem.getLanguageSupport())) {
                throw new BadRequestException("Language support of your exam does not match with your problem",
                        "Language support of your exam does not match with your problem");
            }
            ExamProblemId examProblemId = new ExamProblemId(exam.getId(), problem.getId());
            ExamProblem examProblem = new ExamProblem(examProblemId, exam, problem, problemExam.getProblemPoint());
            examProblemRepository.save(examProblem);
        }

        ExamResponseDto examResponseDto = getExamDetailByCode(code);
        return examResponseDto;
    }

    private boolean checkLanguageSupportEquals(Set<Language> examSupport, Set<Language> problemSupport) {
        return examSupport.equals(problemSupport);
    }

    private Exam getExamByCode(String code) {
        return examRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Exam not found", "Exam not found"));
    }

    @Override
    public ExamResponseDto editExam(AddExamRequestDto addExamRequestDto, String code) {
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

        Set<Language> languages = languageService.getLanguagesByNameList(addExamRequestDto.getLanguageSupports());
        List<ExamProblemRequestDto> problemExams = addExamRequestDto.getProblemRequests();

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
        examRepository.delete(savedExam);
    }

    @Override
    public Page<ExamListResponseDto> getListOfExam(FilterExamRequestDto filterExamRequestDto) {
        if ((filterExamRequestDto.getStart() != null && filterExamRequestDto.getEnd() == null)
                || (filterExamRequestDto.getStart() == null && filterExamRequestDto.getEnd() != null)) {
            throw new BadRequestException("Start and end date must be provided together",
                    "Start and end date must be provided together");
        }
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
                        filterExamRequestDto.getTitle(),
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
        for (ExamProblem examProblem : examProblemRepository.findByExam(exam)) {
            ExamProblemResponseDto examProblemResponseDto = new ExamProblemResponseDto();
            examProblemResponseDto.setId(examProblem.getProblem().getId());
            examProblemResponseDto.setProblemLink(examProblem.getProblem().getLink());
            examProblemResponseDto.setProblemTitle(examProblem.getProblem().getTitle());
            examProblemResponseDto.setProblemPoint(examProblem.getPoint());
            examProblems.add(examProblemResponseDto);
        }
        examResponseDto.setLanguageSupports(languageService.getLanguageNamesByList(exam.getLanguageSupport()));
        examResponseDto.setProblems(examProblems);
        return examResponseDto;
    }

    @Override
    public void enrollExam(String code) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'enrollExam'");
    }
}
