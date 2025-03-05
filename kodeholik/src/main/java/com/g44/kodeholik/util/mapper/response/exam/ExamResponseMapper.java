package com.g44.kodeholik.util.mapper.response.exam;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.exam.examiner.ExamResponseDto;
import com.g44.kodeholik.model.entity.exam.Exam;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExamResponseMapper implements Mapper<Exam, ExamResponseDto> {

    private final ModelMapper mapper;

    @Override
    public Exam mapTo(ExamResponseDto b) {
        return mapper.map(b, Exam.class);
    }

    @Override
    public ExamResponseDto mapFrom(Exam a) {
        return mapper.map(a, ExamResponseDto.class);

    }
}
