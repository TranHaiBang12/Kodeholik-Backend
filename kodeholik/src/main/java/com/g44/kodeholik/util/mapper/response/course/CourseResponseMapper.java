package com.g44.kodeholik.util.mapper.response.course;

import com.g44.kodeholik.model.dto.response.course.ChapterResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemResponseDto;
import com.g44.kodeholik.model.dto.response.user.UserResponseDto;
import com.g44.kodeholik.model.entity.course.Chapter;
import com.g44.kodeholik.model.entity.course.Lesson;
import com.g44.kodeholik.model.entity.setting.Topic;
import com.g44.kodeholik.repository.course.CourseRatingRepository;
import com.g44.kodeholik.repository.course.LessonRepository;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.util.mapper.request.exam.AddExamRequestMapper;
import com.g44.kodeholik.util.mapper.response.user.UserResponseMapper;

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

    private final S3Service s3Service;

    private final LessonRepository lessonRepository;

    private final CourseRatingRepository courseRatingRepository;

    private final UserResponseMapper userResponseMapper;

    // @PostConstruct
    // public void configureMapper() {
    // modelMapper.createTypeMap(Course.class, CourseResponseDto.class)
    // .addMappings(mapper -> mapper.map(Course::getChapters,
    // CourseResponseDto::setChapters));
    //
    // modelMapper.createTypeMap(Chapter.class, ChapterResponseDto.class)
    // .addMappings(mapper -> mapper.map(Chapter::getLessons,
    // ChapterResponseDto::setLessons));
    // }

    @Override
    public Course mapTo(CourseResponseDto b) {
        return modelMapper.map(b, Course.class);
    }

    @Override
    public CourseResponseDto mapFrom(Course course) {
        String image = course.getImage();
        if (image != null && image.startsWith("kodeholik")) {
            image = s3Service.getPresignedUrl(image);
        }
        return CourseResponseDto.builder()
                .id(course.getId())
                .title(course.getTitle())
                .image(image)
                .status(course.getStatus())
                .rate(course.getRate())
                .createdAt(course.getCreatedAt() != null ? course.getCreatedAt().getTime() : null)
                .numberOfParticipant(course.getNumberOfParticipant())
                .topics(course.getTopics().stream().map(Topic::getName).collect(Collectors.toList()))
                .build();
        // return modelMapper.map(course, CourseResponseDto.class);
    }

    public CourseResponseDto mapFromCourseAndLesson(Course course, List<Long> completedLessons) {
        List<Lesson> lessons = lessonRepository.findByChapter_Course_Id(course.getId());

        // Xử lý URL hình ảnh
        String image = course.getImage();
        if (image != null && image.startsWith("kodeholik")) {
            image = s3Service.getPresignedUrl(image);
        }

        // Tính progress
        int totalLessons = lessons.size();
        int completedCount = (int) lessons.stream()
                .filter(lesson -> completedLessons.contains(lesson.getId()))
                .count();
        double progress = totalLessons > 0 ? (completedCount * 100.0) / totalLessons : 0.0;

        UserResponseDto createdBy = course.getCreatedBy() != null ? userResponseMapper.mapFrom(course.getCreatedBy()) : null;
        
        int noChapter = course.getChapters() != null ? course.getChapters().size() : 0;

        int noLesson = totalLessons;

        int noVote = courseRatingRepository.countByCourseId(course.getId());

        return CourseResponseDto.builder()
                .id(course.getId())
                .title(course.getTitle())
                .image(image)
                .status(course.getStatus())
                .rate(course.getRate())
                .numberOfParticipant(course.getNumberOfParticipant())
                .createdAt(course.getCreatedAt() != null ? course.getCreatedAt().getTime() : null)
                .topics(course.getTopics().stream()
                        .map(Topic::getName)
                        .collect(Collectors.toList()))
                .progress(progress)
                .createdBy(createdBy)
                .noVote(noVote)
                .noChapter(noChapter)
                .noLesson(noLesson)
                .build();
    }

}