package com.g44.kodeholik.util.mapper.response.course;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.course.CourseResponseDto;
import com.g44.kodeholik.model.entity.course.Course;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CourseResponseMapper implements Mapper<Course, CourseResponseDto> {

    private final ModelMapper modelMapper;

    @Override
    public Course mapTo(CourseResponseDto b) {
        return modelMapper.map(b, Course.class);
    }

    @Override
    public CourseResponseDto mapFrom(Course a) {
        return modelMapper.map(a, CourseResponseDto.class);

    }

}
