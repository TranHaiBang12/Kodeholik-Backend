package com.g44.kodeholik.service.exam;

import java.sql.Timestamp;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.g44.kodeholik.model.dto.request.exam.AddExamRequestDto;
import com.g44.kodeholik.model.dto.request.exam.EditExamBasicRequestDto;
import com.g44.kodeholik.model.dto.request.exam.EditExamProblemRequestDto;
import com.g44.kodeholik.model.dto.request.exam.FilterExamRequestDto;
import com.g44.kodeholik.model.dto.request.exam.SubmitExamRequestDto;
import com.g44.kodeholik.model.dto.request.problem.ProblemCompileRequestDto;
import com.g44.kodeholik.model.dto.response.exam.examiner.ExamListResponseDto;
import com.g44.kodeholik.model.dto.response.exam.examiner.ExamResponseDto;
import com.g44.kodeholik.model.dto.response.exam.examiner.ExamResultOverviewDto;
import com.g44.kodeholik.model.dto.response.exam.student.ExamDetailResponseDto;
import com.g44.kodeholik.model.dto.response.exam.student.ExamListStudentResponseDto;
import com.g44.kodeholik.model.dto.response.exam.student.ExamProblemDetailResponseDto;
import com.g44.kodeholik.model.dto.response.exam.student.ExamResultOverviewResponseDto;
import com.g44.kodeholik.model.dto.response.exam.student.NotStartedExamListDto;
import com.g44.kodeholik.model.dto.response.problem.submission.run.RunProblemResponseDto;
import com.g44.kodeholik.model.entity.exam.Exam;
import com.g44.kodeholik.model.enums.exam.ExamStatus;

public interface ExamService {
        public ExamResponseDto createExam(AddExamRequestDto addExamRequestDto);

        public ExamResponseDto editExam(AddExamRequestDto addExamRequestDto, String code);

        public ExamResponseDto editExamBasic(EditExamBasicRequestDto editExamBasicRequestDto, String code);

        public ExamResponseDto editExamProblem(EditExamProblemRequestDto editExamProblemRequestDto, String code);

        public void deleteExam(String code);

        public Page<ExamListResponseDto> getListOfExam(FilterExamRequestDto filterExamRequestDto);

        public ExamResponseDto getExamDetailByCode(String code);

        public void enrollExam(String code);

        public void unenrollExam(String code);

        public List<String> getCodeFromExamReadyToStarted();

        public ExamDetailResponseDto getProblemDetailInExam(String code);

        public double submitExam(List<SubmitExamRequestDto> submitExamRequestDto, String code,
                        String username);

        public Object generateTokenForExam(String code);

        public ExamDetailResponseDto startExam(String code);

        public RunProblemResponseDto runExam(String examCode, String link,
                        ProblemCompileRequestDto problemCompileRequestDto);

        public void endExam();

        public ExamResultOverviewResponseDto viewResult(String code);

        public Page<ExamListStudentResponseDto> getListExam(ExamStatus status, int page, Integer size, String title,
                        Date start, Date end);

        public List<Map<String, String>> getAllParticipantsInExam(String code);

        public ExamResultOverviewResponseDto viewResultOfASpecificParticpant(String code, Long userId);

        public byte[] generateExamResultFile(String code);

        public void sendNotiToUserExamAboutToStart();

        public List<Exam> getAllPendingExam();

        public List<NotStartedExamListDto> getAllPendingExamNotOverlapTime();

        public ExamResultOverviewDto getResultOverviewInformation(String code);
}
