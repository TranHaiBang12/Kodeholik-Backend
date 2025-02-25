package com.g44.kodeholik.repository.course;

import com.g44.kodeholik.model.entity.course.CourseRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRatingRepository extends JpaRepository<CourseRating, Long> {
    Optional<CourseRating> findByCourseIdAndUserId(Long courseId, Long userId);
}
