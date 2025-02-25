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
    Page<Course> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Course> findByTitleContainingIgnoreCaseAndTopicsIn(String title, List<Topic> topics, Pageable pageable);

    Page<Course> findByTopicsIn(List<Topic> topics, Pageable pageable);


//    @Query("SELECT c FROM Course c LEFT JOIN c.topics t WHERE " +
//            "(:title IS NULL OR LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
//            "AND (:topics IS NULL OR t.name IN :topics) " +
//            "GROUP BY c.id")
//    Page<Course> searchCourses(@Param("title") String title,
//                               @Param("topics") List<String> topics,
//                               Pageable pageable);
}

