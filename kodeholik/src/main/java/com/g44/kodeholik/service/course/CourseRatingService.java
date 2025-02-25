package com.g44.kodeholik.service.course;

import com.g44.kodeholik.model.dto.request.course.CourseRatingRequestDto;
import com.g44.kodeholik.model.dto.response.course.CourseRatingResponseDto;

public interface CourseRatingService {
    public CourseRatingResponseDto rateCourse(CourseRatingRequestDto requestDto);
}
