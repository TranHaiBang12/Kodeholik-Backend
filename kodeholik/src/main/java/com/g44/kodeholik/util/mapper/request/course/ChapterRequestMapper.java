package com.g44.kodeholik.util.mapper.request.course;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.request.course.ChapterRequestDto;
import com.g44.kodeholik.model.entity.course.Chapter;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChapterRequestMapper implements Mapper<Chapter, ChapterRequestDto> {

    private final ModelMapper modelMapper;

    @Override
    public Chapter mapTo(ChapterRequestDto b) {
        return modelMapper.map(b, Chapter.class);
    }

    @Override
    public ChapterRequestDto mapFrom(Chapter a) {
        return modelMapper.map(a, ChapterRequestDto.class);

    }

}
