package com.g44.kodeholik.repository.course;

import com.g44.kodeholik.model.dto.response.course.ChapterResponseDto;
import com.g44.kodeholik.model.entity.course.Lesson;
import com.g44.kodeholik.model.enums.course.ChapterStatus;
import com.g44.kodeholik.model.enums.course.LessonStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.g44.kodeholik.model.entity.course.Chapter;
import com.g44.kodeholik.model.entity.course.Course;

import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    List<Chapter> findByCourseIdAndStatusIn(Long courseId, List<ChapterStatus> statuses);

    Page<Chapter> findByStatusIn(List<ChapterStatus> statuses, Pageable pageable);

    List<Chapter> findByCourseId(Long courseId);

    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, Long id);
}
