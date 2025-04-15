package com.g44.kodeholik.util.mapper.response.exam;

import com.g44.kodeholik.model.dto.response.user.UserResponseDto;
import com.g44.kodeholik.util.mapper.response.user.UserResponseMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.exam.examiner.ExamListResponseDto;
import com.g44.kodeholik.model.entity.exam.Exam;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExamListResponseMapper implements Mapper<Exam, ExamListResponseDto> {

    private final ModelMapper mapper;
    private final UserResponseMapper userResponseMapper;

    @Override
    public Exam mapTo(ExamListResponseDto b) {
        return mapper.map(b, Exam.class);
    }

    @Override
    public ExamListResponseDto mapFrom(Exam a) {
        if (a.getCreatedBy() != null) {
            userResponseMapper.mapFrom(a.getCreatedBy());
        }
        return mapper.map(a, ExamListResponseDto.class);
    }

}
