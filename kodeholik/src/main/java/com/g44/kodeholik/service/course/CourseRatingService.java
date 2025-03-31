package com.g44.kodeholik.service.course;

import java.util.List;
import java.util.Optional;

import com.g44.kodeholik.model.dto.request.course.CourseRatingRequestDto;
import com.g44.kodeholik.model.dto.response.course.CourseRatingResponseDto;

public interface CourseRatingService {
    public CourseRatingResponseDto rateCourse(CourseRatingRequestDto requestDto);

    public List<CourseRatingResponseDto> getCourseRating(Long courseId);

    Optional<CourseRatingResponseDto> getUserCourseRating(Long courseId);
}
