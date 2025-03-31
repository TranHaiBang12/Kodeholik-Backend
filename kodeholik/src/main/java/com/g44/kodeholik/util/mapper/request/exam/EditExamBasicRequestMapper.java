package com.g44.kodeholik.util.mapper.request.exam;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.model.dto.request.exam.EditExamBasicRequestDto;
import com.g44.kodeholik.model.entity.exam.Exam;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EditExamBasicRequestMapper implements Mapper<Exam, EditExamBasicRequestDto> {

    private final ModelMapper mapper; // Use the model mapper to map between DTO and Entity classes.

    @Override
    public Exam mapTo(EditExamBasicRequestDto b) {
        return mapper.map(b, Exam.class);
    }

    @Override
    public EditExamBasicRequestDto mapFrom(Exam a) {
        return mapper.map(a, EditExamBasicRequestDto.class);

    }

}
