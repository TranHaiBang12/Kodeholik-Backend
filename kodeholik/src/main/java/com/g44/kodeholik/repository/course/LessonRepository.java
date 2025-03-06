package com.g44.kodeholik.repository.course;

import com.g44.kodeholik.model.enums.course.CourseStatus;
import com.g44.kodeholik.model.enums.course.LessonStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.course.Course;
import com.g44.kodeholik.model.entity.course.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Page<Lesson> findByStatus(LessonStatus status, Pageable pageable);
    Page<Course> findById(Long id, Pageable pageable);

}
