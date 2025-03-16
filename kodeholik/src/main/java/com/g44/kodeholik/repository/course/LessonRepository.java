package com.g44.kodeholik.repository.course;

import com.g44.kodeholik.model.dto.response.course.LessonResponseDto;
import com.g44.kodeholik.model.enums.course.CourseStatus;
import com.g44.kodeholik.model.enums.course.LessonStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.course.Course;
import com.g44.kodeholik.model.entity.course.Lesson;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Page<Lesson> findByStatusIn(List<LessonStatus> statuses, Pageable pageable);
    Page<Course> findById(Long id, Pageable pageable);
    List<Lesson> findByChapterIdAndStatusIn(Long id, List<LessonStatus> statuses);
}
