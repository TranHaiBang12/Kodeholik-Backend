package com.g44.kodeholik.service.course.impl;

import java.sql.Timestamp;
import java.time.Instant;

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

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    private final CourseRequestMapper courseRequestMapper;

    private final CourseResponseMapper courseResponseMapper;

    private final UserService userService;

    @Override
    public Page<CourseResponseDto> getAllCourse(Pageable pageable) {
        Page<Course> coursePage = courseRepository.findAll(pageable);
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

}
