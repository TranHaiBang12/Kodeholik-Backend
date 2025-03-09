package com.g44.kodeholik.util.mapper.response.course;

import com.g44.kodeholik.model.dto.response.course.ChapterResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemResponseDto;
import com.g44.kodeholik.model.entity.course.Chapter;
import com.g44.kodeholik.model.entity.setting.Topic;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.course.CourseResponseDto;
import com.g44.kodeholik.model.entity.course.Course;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CourseResponseMapper implements Mapper<Course, CourseResponseDto> {

    private final ModelMapper modelMapper;

//    @PostConstruct
//    public void configureMapper() {
//        modelMapper.createTypeMap(Course.class, CourseResponseDto.class)
//                .addMappings(mapper -> mapper.map(Course::getChapters, CourseResponseDto::setChapters));
//
//        modelMapper.createTypeMap(Chapter.class, ChapterResponseDto.class)
//                .addMappings(mapper -> mapper.map(Chapter::getLessons, ChapterResponseDto::setLessons));
//    }

    @Override
    public Course mapTo(CourseResponseDto b) {
        return modelMapper.map(b, Course.class);
    }

    @Override
    public CourseResponseDto mapFrom(Course course) {
//        return CourseResponseDto.builder()
//                .id(course.getId())
//                .title(course.getTitle())
//                .image(course.getImage())
//                .status(course.getStatus())
//                .rate(course.getRate())
//                .createdAt(course.getCreatedAt())
//                .numberOfParticipant(course.getNumberOfParticipant())
//                .topics(course.getTopics().stream().map(Topic::getName).collect(Collectors.toList()))
//                .build();
        return modelMapper.map(course, CourseResponseDto.class);
    }

}