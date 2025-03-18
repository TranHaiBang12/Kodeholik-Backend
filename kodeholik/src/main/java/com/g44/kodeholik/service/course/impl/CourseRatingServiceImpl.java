package com.g44.kodeholik.service.course.impl;

import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.course.CourseRatingRequestDto;
import com.g44.kodeholik.model.dto.response.course.CourseRatingResponseDto;
import com.g44.kodeholik.model.entity.course.Course;
import com.g44.kodeholik.model.entity.course.CourseRating;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.repository.course.CourseRatingRepository;
import com.g44.kodeholik.repository.course.CourseRepository;
import com.g44.kodeholik.repository.course.CourseUserRepository;
import com.g44.kodeholik.repository.user.UserRepository;
import com.g44.kodeholik.service.course.CourseRatingService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.mapper.response.course.CourseRatingResponseMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseRatingServiceImpl implements CourseRatingService {
    private final CourseRatingRepository courseRatingRepository;
    private final CourseRepository courseRepository;
    private final CourseUserRepository courseUserRepository;
    private final UserService userService;
    private final CourseRatingResponseMapper courseRatingResponseMapper;

    @Override
    @Transactional
    public CourseRatingResponseDto rateCourse(CourseRatingRequestDto requestDto) {
        Users currentUser = userService.getCurrentUser();
        Course course = courseRepository.findById(requestDto.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found", "Course not found"));

        // Kiểm tra xem người dùng đã enroll vào course chưa
        boolean alreadyEnrolled = courseUserRepository.existsByCourseAndUser(course, currentUser);
        if (alreadyEnrolled) {
            throw new BadRequestException("You must enroll in the course before rating", "User not enrolled");
        }

        // Kiểm tra xem người dùng đã rate course chưa
        if (courseRatingRepository.findByCourseIdAndUserId(course.getId(), currentUser.getId()).isPresent()) {
            throw new BadRequestException("You have already rated this course", "User can rate only once per course");
        }

        // Tạo mới CourseRating
        CourseRating courseRating = CourseRating.builder()
                .course(course)
                .user(currentUser)
                .rating(requestDto.getRating())
                .comment(requestDto.getComment())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();

        courseRatingRepository.save(courseRating);
        updateCourseRating(course); // Giả định đây là method cập nhật rating trung bình của course
        return new CourseRatingResponseDto(courseRating); // Sử dụng constructor của DTO
    }

    private void updateCourseRating(Course course) {
        List<CourseRating> ratings = courseRatingRepository.findByCourseId(course.getId());

        double avgRating = ratings.stream()
                .mapToInt(CourseRating::getRating)
                .average()
                .orElse(0.0);

        course.setRate(avgRating);
        courseRepository.save(course);
    }


    @Override
    public List<CourseRatingResponseDto> getCourseRating(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found", "Course not found"));

        List<CourseRating> courseRatings = courseRatingRepository.findByCourseId(course.getId());
        return courseRatings.stream()
                .map(courseRatingResponseMapper::mapFrom)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CourseRatingResponseDto> getUserCourseRating(Long courseId) {
        Users currentUser = userService.getCurrentUser();
        return courseRatingRepository.findByCourseIdAndUserId(courseId, currentUser.getId())
                .map(courseRatingResponseMapper::mapFrom);
    }
}
