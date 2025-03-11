package com.g44.kodeholik.util.mapper.response.course;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.course.ChapterResponseDto;
import com.g44.kodeholik.model.entity.course.Chapter;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChapterResponseMapper implements Mapper<Chapter, ChapterResponseDto> {

    private final ModelMapper mapper;

    @Override
    public Chapter mapTo(ChapterResponseDto b) {
        return mapper.map(b, Chapter.class);
    }

    @Override
    public ChapterResponseDto mapFrom(Chapter a) {
        return mapper.map(a, ChapterResponseDto.class);
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
