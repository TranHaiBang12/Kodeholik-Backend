package com.g44.kodeholik.repository.course;

import com.g44.kodeholik.model.entity.course.CourseComment;
import com.g44.kodeholik.model.entity.course.CourseCommentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseCommentRepository extends JpaRepository<CourseComment, CourseCommentId> {
    List<CourseComment> findByCourseId(Long courseId);
}
