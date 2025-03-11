package com.g44.kodeholik.repository.course;

import com.g44.kodeholik.model.dto.response.course.ChapterResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.course.Chapter;
import com.g44.kodeholik.model.entity.course.Course;

import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    List<Chapter> findByCourseId(Long id);
}
