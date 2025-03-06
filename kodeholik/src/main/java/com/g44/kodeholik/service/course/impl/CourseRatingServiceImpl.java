package com.g44.kodeholik.service.course.impl;

import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.course.CourseRatingRequestDto;
import com.g44.kodeholik.model.dto.response.course.CourseRatingResponseDto;
import com.g44.kodeholik.model.entity.course.Course;
import com.g44.kodeholik.model.entity.course.CourseRating;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.repository.course.CourseRatingRepository;
import com.g44.kodeholik.repository.course.CourseRepository;
import com.g44.kodeholik.repository.user.UserRepository;
import com.g44.kodeholik.service.course.CourseRatingService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.mapper.response.course.CourseRatingResponseMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseRatingServiceImpl implements CourseRatingService {
    private final CourseRatingRepository courseRatingRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CourseRatingResponseMapper courseRatingResponseMapper;

    @Override
    @Transactional
    public CourseRatingResponseDto rateCourse(CourseRatingRequestDto requestDto) {
        Users currentUser = userService.getCurrentUser();
        Course course = courseRepository.findById(requestDto.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found", "Course not found"));

        // Check if the user has already rated the course
        Optional<CourseRating> existingRating = courseRatingRepository.findByCourseIdAndUserId(course.getId(), currentUser.getId());

        CourseRating courseRating;
        if (existingRating.isPresent()) {
            // Update existing rating
            courseRating = existingRating.get();
            courseRating.setRating(requestDto.getRating());
            courseRating.setComment(requestDto.getComment());
            courseRating.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        } else {
            // Create new rating
            courseRating = CourseRating.builder()
                    .course(course)
                    .user(currentUser)
                    .rating(requestDto.getRating())
                    .comment(requestDto.getComment())
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .updatedAt(new Timestamp(System.currentTimeMillis()))
                    .build();
        }

        courseRatingRepository.save(courseRating);
        return courseRatingResponseMapper.mapFrom(courseRating);
    }
}
