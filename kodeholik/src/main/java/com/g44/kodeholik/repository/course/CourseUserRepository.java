package com.g44.kodeholik.repository.course;

import com.g44.kodeholik.model.entity.course.Course;
import com.g44.kodeholik.model.entity.course.CourseUser;
import com.g44.kodeholik.model.entity.course.CourseUserId;
import com.g44.kodeholik.model.entity.user.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseUserRepository extends JpaRepository<CourseUser, CourseUserId> {
    boolean existsByCourseAndUser(Course course, Users user);
    Optional<CourseUser> findByCourseAndUser(Course course, Users user);
    Page<CourseUser> findByCourseId(Long courseId, Pageable pageable);
    @Query("SELECT cu FROM CourseUser cu WHERE cu.course.id = :courseId " +
            "AND LOWER(cu.user.username) LIKE LOWER(CONCAT('%', :username, '%'))")
    Page<CourseUser> findByCourseIdAndUserUsernameContaining(
            @Param("courseId") Long courseId,
            @Param("username") String username,
            Pageable pageable
    );
    Page<CourseUser> findByUserId(Long userId, Pageable pageable);
}
