package com.g44.kodeholik.util.mapper.response.exam;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.exam.student.NotStartedExamListDto;
import com.g44.kodeholik.model.entity.exam.Exam;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotStartedExamListMapper implements Mapper<Exam, NotStartedExamListDto> {

    private final ModelMapper mapper;

    @Override
    public Exam mapTo(NotStartedExamListDto b) {
        return mapper.map(b, Exam.class);
    }

    @Override
    public NotStartedExamListDto mapFrom(Exam a) {
        return mapper.map(a, NotStartedExamListDto.class);

    }

}
