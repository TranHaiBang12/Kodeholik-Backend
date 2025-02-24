package com.g44.kodeholik.model.entity.course;

import com.g44.kodeholik.model.entity.user.Users;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "course_user", schema = "schema_course")
@IdClass(CourseUserId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseUser {
    @Id
    @Column(name = "course_id")
    private Long courseId;

    @Id
    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;

    @Column(name = "enrolled_at", nullable = false, updatable = false)
    private Timestamp enrolledAt;

    public CourseUser(Course course, Users user) {
        this.course = course;
        this.user = user;
        this.courseId = course.getId();
        this.userId = user.getId();
        this.enrolledAt = new Timestamp(System.currentTimeMillis());
    }
}


