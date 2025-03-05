package com.g44.kodeholik.service.exam;

import java.util.List;

import org.springframework.data.domain.Page;

import com.g44.kodeholik.model.dto.request.exam.AddExamRequestDto;
import com.g44.kodeholik.model.dto.request.exam.FilterExamRequestDto;
import com.g44.kodeholik.model.dto.request.exam.SubmitExamRequestDto;
import com.g44.kodeholik.model.dto.request.problem.ProblemCompileRequestDto;
import com.g44.kodeholik.model.dto.response.exam.examiner.ExamListResponseDto;
import com.g44.kodeholik.model.dto.response.exam.examiner.ExamResponseDto;
import com.g44.kodeholik.model.dto.response.exam.student.ExamListStudentResponseDto;
import com.g44.kodeholik.model.dto.response.exam.student.ExamProblemDetailResponseDto;
import com.g44.kodeholik.model.dto.response.exam.student.ExamResultOverviewResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.run.RunProblemResponseDto;
import com.g44.kodeholik.model.enums.exam.ExamStatus;

public interface ExamService {
        public ExamResponseDto createExam(AddExamRequestDto addExamRequestDto);

        public ExamResponseDto editExam(AddExamRequestDto addExamRequestDto, String code);

        public void deleteExam(String code);

        public Page<ExamListResponseDto> getListOfExam(FilterExamRequestDto filterExamRequestDto);

        public ExamResponseDto getExamDetailByCode(String code);

        public void enrollExam(String code);

        public List<String> getCodeFromExamReadyToStarted();

        public List<ExamProblemDetailResponseDto> getProblemDetailInExam(String code);

        public ExamResultOverviewResponseDto submitExam(List<SubmitExamRequestDto> submitExamRequestDto, String code,
                        String username);

        public Object generateTokenForExam(String code);

        public List<ExamProblemDetailResponseDto> startExam(String code);

        public RunProblemResponseDto runExam(String examCode, String link,
                        ProblemCompileRequestDto problemCompileRequestDto);

        public void endExam();

        public ExamResultOverviewResponseDto viewResult(String code);

        public Page<ExamListStudentResponseDto> getListExam(ExamStatus status, int page, Integer size);

}
