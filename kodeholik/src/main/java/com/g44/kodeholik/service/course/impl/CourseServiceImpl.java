package com.g44.kodeholik.service.course.impl;

import java.sql.Timestamp;
import java.time.Instant;

import com.g44.kodeholik.model.entity.course.CourseUser;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.course.CourseStatus;
import com.g44.kodeholik.repository.course.CourseUserRepository;
import com.g44.kodeholik.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.course.CourseRequestDto;
import com.g44.kodeholik.model.dto.response.course.CourseResponseDto;
import com.g44.kodeholik.model.entity.course.Course;
import com.g44.kodeholik.repository.course.CourseRepository;
import com.g44.kodeholik.service.course.CourseService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.mapper.request.course.CourseRequestMapper;
import com.g44.kodeholik.util.mapper.response.course.CourseResponseMapper;

import lombok.RequiredArgsConstructor;
@Log4j2
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    private final CourseRequestMapper courseRequestMapper;

    private final CourseResponseMapper courseResponseMapper;

    private final UserService userService;

    private final UserRepository userRepository;

    private final CourseUserRepository courseUserRepository;

    @Override
    public Page<CourseResponseDto> getAllCourse(Pageable pageable) {
        Page<Course> coursePage = courseRepository
                .findByStatus(CourseStatus.ACTIVATED, pageable);
        return coursePage.map(courseResponseMapper::mapFrom);
    }

    @Override
    public CourseResponseDto getCourseById(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found", "Course not found"));
        return courseResponseMapper.mapFrom(course);
    }

    @Override
    public void addCourse(CourseRequestDto courseRequestDto) {
        Course course = courseRequestMapper.mapTo(courseRequestDto);
        course.setCreatedAt(Timestamp.from(Instant.now()));
        course.setCreatedBy(userService.getCurrentUser());
        courseRepository.save(course);
    }

    @Override
    public void editCourse(Long courseId, CourseRequestDto courseRequestDto) {
        Course savedCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found", "Course not found"));
        Course course = courseRequestMapper.mapTo(courseRequestDto);
        course.setId(courseId);
        course.setUpdatedAt(Timestamp.from(Instant.now()));
        course.setUpdatedBy(userService.getCurrentUser());
        course.setCreatedAt(savedCourse.getCreatedAt());
        course.setCreatedBy(savedCourse.getCreatedBy());
        courseRepository.save(course);
    }

    @Override
    public void deleteCourse(Long courseId) {
        courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found", "Course not found"));
        courseRepository.deleteById(courseId);
    }

    @Override
    public Page<CourseResponseDto> searchCoursesByTitle(String keyword, Pageable pageable) {
        Page<Course> coursePage = courseRepository.findByTitleContainingIgnoreCase(keyword, pageable);
        return coursePage.map(courseResponseMapper::mapFrom);
    }

    @Transactional
    @Override
    public void enrollUserInCourse( Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        Users user = userService.getCurrentUser();

        boolean alreadyEnrolled = courseUserRepository.existsByCourseAndUser(course, user);
        if (alreadyEnrolled) {
            throw new IllegalStateException("User is already enrolled in this course.");
        }
        log.info(course);
        log.info(user);
        CourseUser courseUser = new CourseUser(course, user);
        courseUserRepository.save(courseUser);

        course.setNumberOfParticipant(course.getNumberOfParticipant() + 1);
        courseRepository.save(course);
    }

    @Transactional
    @Override
    public void unenrollUserFromCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        Users user = userService.getCurrentUser();

        CourseUser courseUser = courseUserRepository.findByCourseAndUser(course, user)
                .orElseThrow(() -> new IllegalStateException("User is not enrolled in this course."));

        courseUserRepository.delete(courseUser);
        course.setNumberOfParticipant(Math.max(course.getNumberOfParticipant() - 1, 0));
        courseRepository.save(course);
    }
}
