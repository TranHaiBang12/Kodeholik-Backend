package com.g44.kodeholik.repository.course;

import com.g44.kodeholik.model.entity.course.CourseComment;
import com.g44.kodeholik.model.entity.course.CourseCommentId;
import com.g44.kodeholik.model.entity.discussion.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseCommentRepository extends JpaRepository<CourseComment, CourseCommentId> {
    Page<CourseComment> findByCourseId(Long courseId, Pageable pageable);

    @Query("SELECT cc.comment FROM CourseComment cc WHERE cc.course.id = :courseId")
    List<Comment> findCommentsByCourseId(@Param("courseId") Long courseId);

}
