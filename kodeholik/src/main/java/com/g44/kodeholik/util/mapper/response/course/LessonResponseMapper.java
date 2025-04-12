package com.g44.kodeholik.util.mapper.response.course;

import com.g44.kodeholik.model.dto.response.course.CourseResponseDto;
import com.g44.kodeholik.model.dto.response.course.LessonProblemResponseDto;
import com.g44.kodeholik.model.entity.course.Course;
import com.g44.kodeholik.model.entity.course.LessonProblem;
import com.g44.kodeholik.model.entity.setting.Topic;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.course.LessonResponseDto;
import com.g44.kodeholik.model.entity.course.Lesson;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LessonResponseMapper implements Mapper<Lesson, LessonResponseDto> {

    private final ModelMapper modelMapper;

    @PostConstruct
    public void configureMapper() {
        modelMapper.createTypeMap(Lesson.class, LessonResponseDto.class)
                .addMappings(mapper -> mapper.map(src -> src.getChapter().getId(), LessonResponseDto::setChapterId))
                .addMappings(mapper -> mapper.map(src -> src.getChapter().getCourse().getId(), LessonResponseDto::setCourseId));
    }

    @Override
    public Lesson mapTo(LessonResponseDto b) {
        return modelMapper.map(b, Lesson.class);
    }

    @Override
    public LessonResponseDto mapFrom(Lesson a) {
        return mapFrom(a, Collections.emptyList()); // Mặc định nếu không có completedLessons
    }

    public LessonResponseDto mapFrom(Lesson lesson, List<Long> completedLessons) {
        LessonResponseDto dto = modelMapper.map(lesson, LessonResponseDto.class);
        dto.setCompleted(completedLessons.contains(lesson.getId()));
        return dto;
    }
}

