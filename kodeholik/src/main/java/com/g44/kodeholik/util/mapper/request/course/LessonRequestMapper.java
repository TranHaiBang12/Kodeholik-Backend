package com.g44.kodeholik.util.mapper.request.course;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.request.course.LessonRequestDto;
import com.g44.kodeholik.model.entity.course.Lesson;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LessonRequestMapper implements Mapper<Lesson, LessonRequestDto> {

    private final ModelMapper modelMapper;

    @Override
    public Lesson mapTo(LessonRequestDto b) {
        return modelMapper.map(b, Lesson.class);
    }

    @Override
    public LessonRequestDto mapFrom(Lesson a) {
        return modelMapper.map(a, LessonRequestDto.class);

    }

    public void updateFromDto(LessonRequestDto dto, Lesson lesson) {
        modelMapper.map(dto, lesson);
    }

}
