package com.g44.kodeholik.repository.course;

import com.g44.kodeholik.model.entity.setting.Topic;
import com.g44.kodeholik.model.enums.course.CourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.course.Course;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findByStatus(CourseStatus status, Pageable pageable);

    Page<Course> findByTitleContainingIgnoreCaseAndStatus(String title, CourseStatus status, Pageable pageable);

    Page<Course> findByTopicsInAndStatus(List<Topic> topics, CourseStatus status, Pageable pageable);

    Page<Course> findByTitleContainingIgnoreCaseAndTopicsInAndStatus(String title, List<Topic> topics, CourseStatus status, Pageable pageable);

}

