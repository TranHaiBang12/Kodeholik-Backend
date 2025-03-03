package com.g44.kodeholik.service.exam;

import java.util.List;

import org.springframework.data.domain.Page;

import com.g44.kodeholik.model.dto.request.exam.AddExamRequestDto;
import com.g44.kodeholik.model.dto.request.exam.FilterExamRequestDto;
import com.g44.kodeholik.model.dto.request.problem.ProblemCompileRequestDto;
import com.g44.kodeholik.model.dto.response.exam.ExamListResponseDto;
import com.g44.kodeholik.model.dto.response.exam.ExamResponseDto;

public interface ExamService {
    public ExamResponseDto createExam(AddExamRequestDto addExamRequestDto);

    public ExamResponseDto editExam(AddExamRequestDto addExamRequestDto, String code);

    public void deleteExam(String code);

    public Page<ExamListResponseDto> getListOfExam(FilterExamRequestDto filterExamRequestDto);

    public ExamResponseDto getExamDetailByCode(String code);

    public void enrollExam(String code);

}
