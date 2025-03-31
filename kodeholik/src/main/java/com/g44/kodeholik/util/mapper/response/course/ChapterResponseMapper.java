package com.g44.kodeholik.util.mapper.response.course;

import com.g44.kodeholik.model.dto.response.course.LessonResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.course.ChapterResponseDto;
import com.g44.kodeholik.model.entity.course.Chapter;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChapterResponseMapper implements Mapper<Chapter, ChapterResponseDto> {

    private final ModelMapper mapper;
    private final LessonResponseMapper lessonResponseMapper;

    @Override
    public Chapter mapTo(ChapterResponseDto b) {
        return mapper.map(b, Chapter.class);
    }

    @Override
    public ChapterResponseDto mapFrom(Chapter chapter) {
        return mapFrom(chapter, Collections.emptyList()); // Mặc định nếu không có completedLessons
    }

    public ChapterResponseDto mapFrom(Chapter chapter, List<Long> completedLessons) {
        List<LessonResponseDto> lessonDtos = chapter.getLessons() != null
                ? chapter.getLessons().stream()
                        .map(lesson -> lessonResponseMapper.mapFrom(lesson, completedLessons))
                        .toList()
                : Collections.emptyList();

        return ChapterResponseDto.builder()
                .id(chapter.getId())
                .courseId(chapter.getCourse() != null ? chapter.getCourse().getId() : null)
                .title(chapter.getTitle())
                .description(chapter.getDescription())
                .displayOrder(chapter.getDisplayOrder())
                .status(chapter.getStatus())
                .lessons(lessonDtos)
                .build();
    }

    public ChapterResponseDto mapDetailFrom(Chapter chapter) {
        return ChapterResponseDto.builder()
                .id(chapter.getId())
                .title(chapter.getTitle())
                .courseId(chapter.getCourse().getId())
                .description(chapter.getDescription())
                .status(chapter.getStatus())
                .build();
    }
}
