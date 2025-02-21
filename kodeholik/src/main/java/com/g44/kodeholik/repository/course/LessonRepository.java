package com.g44.kodeholik.repository.course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.course.Course;
import com.g44.kodeholik.model.entity.course.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
}
