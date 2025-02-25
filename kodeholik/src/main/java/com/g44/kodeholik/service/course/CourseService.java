package com.g44.kodeholik.service.course;

import com.g44.kodeholik.model.dto.request.course.search.CourseSortField;
import com.g44.kodeholik.model.dto.request.course.search.SearchCourseRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.g44.kodeholik.model.dto.request.course.CourseRequestDto;
import com.g44.kodeholik.model.dto.response.course.CourseResponseDto;

public interface CourseService {
    public Page<CourseResponseDto> getAllCourse(Pageable pageable);

    public CourseResponseDto getCourseById(Long courseId);

    public void addCourse(CourseRequestDto courseRequestDto);

    public void editCourse(Long courseId, CourseRequestDto courseRequestDto);

    public void deleteCourse(Long courseId);

    Page<CourseResponseDto> searchCoursesByTitle(String keyword, Pageable pageable);

    Page<CourseResponseDto> searchCourses(SearchCourseRequestDto request, Integer page, Integer size,
            CourseSortField sortBy, Boolean ascending);

    public void enrollUserInCourse(Long courseId);

    public void unenrollUserFromCourse(Long courseId);
}
