package com.g44.kodeholik.repository.course;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.course.Chapter;
import com.g44.kodeholik.model.entity.course.Course;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {

}
