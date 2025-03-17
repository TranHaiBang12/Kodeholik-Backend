package com.g44.kodeholik.util.mapper.response.course;

import com.g44.kodeholik.model.dto.response.course.ChapterResponseDto;
import com.g44.kodeholik.model.dto.response.course.CourseDetailResponseDto;
import com.g44.kodeholik.model.dto.response.course.CourseResponseDto;
import com.g44.kodeholik.model.dto.response.setting.TopicResponseDto;
import com.g44.kodeholik.model.entity.course.Course;
import com.g44.kodeholik.model.entity.setting.Topic;
import com.g44.kodeholik.repository.course.LessonRepository;
import com.g44.kodeholik.util.mapper.Mapper;
import com.g44.kodeholik.util.mapper.response.tag.TagResponseMapperTopic;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class CourseDetailResponseMapper implements Mapper<Course, CourseDetailResponseDto> {

    private final ModelMapper modelMapper;

    private final TagResponseMapperTopic topicResponseMapper;
    private final ChapterResponseMapper chapterResponseMapper;

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

        // Ánh xạ topics
        List<TopicResponseDto> topicDtos = course.getTopics() != null
                ? course.getTopics().stream()
                .map(topicResponseMapper::mapFromTopic)
                .collect(Collectors.toList())
                : Collections.emptyList();

        // Ánh xạ chapters với completedLessons
        List<ChapterResponseDto> chapterDtos = course.getChapters() != null
                ? course.getChapters().stream()
                .map(chapter -> chapterResponseMapper.mapFrom(chapter, completedLessons))
                .collect(Collectors.toList())
                : Collections.emptyList();

        return CourseDetailResponseDto.builder()
                .id(course.getId())
                .description(course.getDescription())
                .title(course.getTitle())
                .image(course.getImage())
                .status(course.getStatus())
                .rate(course.getRate())
                .updatedAt(course.getUpdatedAt().getTime())
                .numberOfParticipant(course.getNumberOfParticipant())
                .progress(progress)
                .topics(topicDtos)
                .chapters(chapterDtos)
                .build();
    }
}
