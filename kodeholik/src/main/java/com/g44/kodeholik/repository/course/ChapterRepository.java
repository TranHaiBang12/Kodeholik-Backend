package com.g44.kodeholik.repository.course;

import com.g44.kodeholik.model.dto.response.course.ChapterResponseDto;
import com.g44.kodeholik.model.entity.course.Lesson;
import com.g44.kodeholik.model.enums.course.ChapterStatus;
import com.g44.kodeholik.model.enums.course.LessonStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.g44.kodeholik.model.entity.course.Chapter;
import com.g44.kodeholik.model.entity.course.Course;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    List<Chapter> findByCourseIdAndStatusIn(Long courseId, List<ChapterStatus> statuses);

    List<Chapter> findByCourseIdAndStatusIn(Long courseId, List<ChapterStatus> statuses, Sort sort);

    Page<Chapter> findByStatusIn(List<ChapterStatus> statuses, Pageable pageable);

    List<Chapter> findByCourseId(Long courseId);

    List<Chapter> findByCourseIdOrderByDisplayOrderAsc(Long courseId);

    Optional<Chapter> findByTitleIgnoreCaseAndCourseId(String title, Long courseId);

    Optional<Chapter> findByTitleIgnoreCaseAndIdNotAndCourseId(String title, Long id, Long courseId);
}
