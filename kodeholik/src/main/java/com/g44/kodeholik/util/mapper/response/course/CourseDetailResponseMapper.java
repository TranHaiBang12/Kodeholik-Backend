package com.g44.kodeholik.util.mapper.response.course;

import com.g44.kodeholik.model.dto.response.course.CourseDetailResponseDto;
import com.g44.kodeholik.model.dto.response.course.CourseResponseDto;
import com.g44.kodeholik.model.entity.course.Course;
import com.g44.kodeholik.util.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CourseDetailResponseMapper implements Mapper<Course, CourseDetailResponseDto> {

    private final ModelMapper modelMapper;

    @Override
    public Course mapTo(CourseDetailResponseDto b) {
        return modelMapper.map(b, Course.class);
    }

    @Override
    public CourseDetailResponseDto mapFrom(Course a) {
        return modelMapper.map(a, CourseDetailResponseDto.class);
    }
}
