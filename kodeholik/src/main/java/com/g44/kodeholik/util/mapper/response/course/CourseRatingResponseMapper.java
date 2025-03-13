package com.g44.kodeholik.util.mapper.response.course;

import com.g44.kodeholik.model.dto.response.course.CourseRatingResponseDto;
import com.g44.kodeholik.model.entity.course.CourseRating;
import com.g44.kodeholik.util.mapper.Mapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseRatingResponseMapper implements Mapper<CourseRating, CourseRatingResponseDto> {

    private final ModelMapper modelMapper;

    @PostConstruct
    public void configureMapper() {
        modelMapper.createTypeMap(CourseRating.class, CourseRatingResponseDto.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getCourse().getId(), CourseRatingResponseDto::setCourseId);
                    mapper.map(src -> src.getUser().getId(), CourseRatingResponseDto::setUserId);
                });
    }


    @Override
    public CourseRating mapTo(CourseRatingResponseDto b) {
        return modelMapper.map(b, CourseRating.class);
    }

    @Override
    public CourseRatingResponseDto mapFrom(CourseRating a) {
        return modelMapper.map(a, CourseRatingResponseDto.class);
    }
}
