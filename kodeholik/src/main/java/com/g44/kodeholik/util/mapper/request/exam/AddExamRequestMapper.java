package com.g44.kodeholik.util.mapper.request.exam;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.request.exam.AddExamRequestDto;
import com.g44.kodeholik.model.entity.exam.Exam;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AddExamRequestMapper implements Mapper<Exam, AddExamRequestDto> {

    private final ModelMapper mapper;

    @Override
    public Exam mapTo(AddExamRequestDto b) {
        return mapper.map(b, Exam.class);
    }

    @Override
    public AddExamRequestDto mapFrom(Exam a) {
        return mapper.map(a, AddExamRequestDto.class);

    }

}
