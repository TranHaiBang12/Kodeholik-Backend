package com.g44.kodeholik.repository.course;

import com.g44.kodeholik.model.entity.course.Course;
import com.g44.kodeholik.model.entity.course.CourseUser;
import com.g44.kodeholik.model.entity.course.CourseUserId;
import com.g44.kodeholik.model.entity.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseUserRepository extends JpaRepository<CourseUser, CourseUserId> {
    boolean existsByCourseAndUser(Course course, Users user);
    Optional<CourseUser> findByCourseAndUser(Course course, Users user);
}
