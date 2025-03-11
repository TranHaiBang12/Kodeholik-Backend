package com.g44.kodeholik.util.mapper.response.exam;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.exam.student.ExamListStudentResponseDto;
import com.g44.kodeholik.model.entity.exam.Exam;
import com.g44.kodeholik.model.entity.exam.ExamParticipant;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExamListStudentResponseMapper implements Mapper<ExamParticipant, ExamListStudentResponseDto> {

    private final ModelMapper mapper;

    @Override
    public ExamParticipant mapTo(ExamListStudentResponseDto b) {
        return mapper.map(b, ExamParticipant.class);
    }

    @Override
    public ExamListStudentResponseDto mapFrom(ExamParticipant a) {
        ExamListStudentResponseDto examListStudentResponseDto = new ExamListStudentResponseDto();
        examListStudentResponseDto.setCode(a.getExam().getCode());
        examListStudentResponseDto.setEndTime(a.getExam().getEndTime().getTime());
        examListStudentResponseDto.setStartTime(a.getExam().getStartTime().getTime());
        examListStudentResponseDto.setStatus(a.getExam().getStatus());
        return examListStudentResponseDto;

    }

}
