package com.g44.kodeholik.util.mapper.response.course;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.course.LessonResponseDto;
import com.g44.kodeholik.model.entity.course.Lesson;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LessonResponseMapper implements Mapper<Lesson, LessonResponseDto> {

    private final ModelMapper modelMapper;

    @Override
    public Lesson mapTo(LessonResponseDto b) {
        return modelMapper.map(b, Lesson.class);
    }

    @Override
    public LessonResponseDto mapFrom(Lesson a) {
        return modelMapper.map(a, LessonResponseDto.class);

    }

}
