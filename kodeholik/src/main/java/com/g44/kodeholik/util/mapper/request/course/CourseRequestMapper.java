package com.g44.kodeholik.util.mapper.request.course;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.request.course.CourseRequestDto;
import com.g44.kodeholik.model.entity.course.Course;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CourseRequestMapper implements Mapper<Course, CourseRequestDto> {

    private final ModelMapper modelMapper;

    @Override
    public Course mapTo(CourseRequestDto b) {
        return modelMapper.map(b, Course.class);
    }

    @Override
    public CourseRequestDto mapFrom(Course a) {
        return modelMapper.map(a, CourseRequestDto.class);
    }

}
