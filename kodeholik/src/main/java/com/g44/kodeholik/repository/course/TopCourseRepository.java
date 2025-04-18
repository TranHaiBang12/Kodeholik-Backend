package com.g44.kodeholik.repository.course;

import java.util.List;

import com.g44.kodeholik.model.enums.course.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.course.TopCourse;

public interface TopCourseRepository extends JpaRepository<TopCourse, Long> {
    List<TopCourse> findByCourseStatusOrderByDisplayOrderDesc(CourseStatus status);
}
