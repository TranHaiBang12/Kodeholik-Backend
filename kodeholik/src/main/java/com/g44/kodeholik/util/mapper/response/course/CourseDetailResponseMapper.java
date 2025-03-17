package com.g44.kodeholik.util.mapper.response.course;

import com.g44.kodeholik.model.dto.response.course.CourseDetailResponseDto;
import com.g44.kodeholik.model.dto.response.course.CourseResponseDto;
import com.g44.kodeholik.model.entity.course.Course;
import com.g44.kodeholik.model.entity.setting.Topic;
import com.g44.kodeholik.repository.course.LessonRepository;
import com.g44.kodeholik.util.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class CourseDetailResponseMapper implements Mapper<Course, CourseDetailResponseDto> {

    private final ModelMapper modelMapper;

    private final LessonRepository lessonRepository;

    @Override
    public Course mapTo(CourseDetailResponseDto b) {
        return modelMapper.map(b, Course.class);
    }

    @Override
    public CourseDetailResponseDto mapFrom(Course a) {
        return modelMapper.map(a, CourseDetailResponseDto.class);
    }

    public CourseDetailResponseDto mapFromCourseAndLesson(Course course, List<Long> lessonIds, List<Long> completedLessons) {
        int totalLessons = lessonIds.size();
        int completedCount = completedLessons.size();

        double progress = totalLessons > 0 ? (completedCount * 100.0) / totalLessons : 0.0;

        return CourseDetailResponseDto.builder()
                .id(course.getId())
                .title(course.getTitle())
                .image(course.getImage())
                .status(course.getStatus())
                .rate(course.getRate())
                .numberOfParticipant(course.getNumberOfParticipant())
                .progress(progress)
                .build();
    }
}
