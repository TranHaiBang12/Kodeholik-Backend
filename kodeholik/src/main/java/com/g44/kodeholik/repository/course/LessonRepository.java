package com.g44.kodeholik.repository.course;

import com.g44.kodeholik.model.dto.response.course.LessonResponseDto;
import com.g44.kodeholik.model.enums.course.CourseStatus;
import com.g44.kodeholik.model.enums.course.LessonStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.course.Course;
import com.g44.kodeholik.model.entity.course.Lesson;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Page<Lesson> findByStatusIn(List<LessonStatus> statuses, Pageable pageable);

    Page<Course> findById(Long id, Pageable pageable);

    List<Lesson> findByChapterIdAndStatusIn(Long id, List<LessonStatus> statuses);

    List<Lesson> findByChapter_Course_Id(Long courseId);

    List<Lesson> findByChapter_Course_IdIn(List<Long> courseIds);

    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, Long id);
}
