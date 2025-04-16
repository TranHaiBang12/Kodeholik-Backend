package com.g44.kodeholik.util.mapper.response.course;

import com.g44.kodeholik.model.dto.response.course.CourseRatingResponseDto;
import com.g44.kodeholik.model.dto.response.user.UserResponseDto;
import com.g44.kodeholik.model.entity.course.CourseRating;
import com.g44.kodeholik.util.mapper.Mapper;
import com.g44.kodeholik.util.mapper.response.user.UserResponseMapper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseRatingResponseMapper implements Mapper<CourseRating, CourseRatingResponseDto> {

    private final ModelMapper modelMapper;

    private final UserResponseMapper userResponseMapper;

    @PostConstruct
    public void configureMapper() {
        modelMapper.createTypeMap(CourseRating.class, CourseRatingResponseDto.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getCourse().getId(), CourseRatingResponseDto::setCourseId);
                    mapper.map(src -> new UserResponseDto(src.getUser()), CourseRatingResponseDto::setUser);
                });
    }

    @Override
    public CourseRating mapTo(CourseRatingResponseDto b) {
        return modelMapper.map(b, CourseRating.class);
    }

    @Override
    public CourseRatingResponseDto mapFrom(CourseRating courseRating) {
        if (courseRating == null) {
            return null;
        }

        UserResponseDto createdBy = courseRating.getUser() != null ? userResponseMapper.mapFrom(courseRating.getUser()) : null;
        return CourseRatingResponseDto.builder()
                .id(courseRating.getId())
                .courseId(courseRating.getCourse().getId())
                .user(createdBy)
                .rating(courseRating.getRating())
                .comment(courseRating.getComment())
                .createdAt(courseRating.getCreatedAt() != null ? courseRating.getCreatedAt().getTime() : null)
                .updatedAt(courseRating.getUpdatedAt() != null ? courseRating.getUpdatedAt().getTime() : null)
                .build();
    }
}


