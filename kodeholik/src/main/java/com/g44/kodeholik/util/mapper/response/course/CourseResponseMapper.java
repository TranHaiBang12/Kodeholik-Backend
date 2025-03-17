package com.g44.kodeholik.util.mapper.response.course;

import com.g44.kodeholik.model.dto.response.course.ChapterResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemResponseDto;
import com.g44.kodeholik.model.entity.course.Chapter;
import com.g44.kodeholik.model.entity.course.Lesson;
import com.g44.kodeholik.model.entity.setting.Topic;
import com.g44.kodeholik.repository.course.LessonRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.course.CourseResponseDto;
import com.g44.kodeholik.model.entity.course.Course;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CourseResponseMapper implements Mapper<Course, CourseResponseDto> {

    private final ModelMapper modelMapper;
    private final LessonRepository lessonRepository;

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
        return CourseResponseDto.builder()
                .id(course.getId())
                .title(course.getTitle())
                .image(course.getImage())
                .status(course.getStatus())
                .rate(course.getRate())
                .createdAt(course.getCreatedAt() != null ? course.getCreatedAt().getTime() : null)
                .numberOfParticipant(course.getNumberOfParticipant())
                .topics(course.getTopics().stream().map(Topic::getName).collect(Collectors.toList()))
                .build();
//        return modelMapper.map(course, CourseResponseDto.class);
    }

    public CourseResponseDto mapFromCourseAndLesson(Course course, List<Long> completedLessons) {
        List<Lesson> lessons = lessonRepository.findByChapter_Course_Id(course.getId());
        int totalLessons = lessons.size();
        int completedCount = (int) lessons.stream()
                .filter(lesson -> completedLessons.contains(lesson.getId()))
                .count();

        double progress = totalLessons > 0 ? (completedCount * 100.0) / totalLessons : 0.0;

        return CourseResponseDto.builder()
                .id(course.getId())
                .title(course.getTitle())
                .image(course.getImage())
                .status(course.getStatus())
                .rate(course.getRate())
                .createdAt(course.getCreatedAt() != null ? course.getCreatedAt().getTime() : null)
                .numberOfParticipant(course.getNumberOfParticipant())
                .topics(course.getTopics().stream().map(Topic::getName).collect(Collectors.toList()))
                .progress(progress)
                .build();
    }

}