package com.g44.kodeholik.repository.course;

import com.g44.kodeholik.model.enums.course.CourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.course.Course;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findByStatus(CourseStatus status, Pageable pageable);
    Page<Course> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}

