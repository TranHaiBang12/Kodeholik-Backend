package com.g44.kodeholik.model.entity.course;

import java.sql.Timestamp;

import com.g44.kodeholik.model.entity.user.Users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "study_streak")
    private int studyStreak;

    @Column(name = "study_time")
    private Long studyTime;

    @Column(name = "last_studied_start", nullable = false, updatable = false)
    private Timestamp lastStudiedStartAt;

    @Column(name = "last_studied_end", nullable = false, updatable = false)
    private Timestamp lastStudiedEndAt;

    public CourseUser(Course course, Users user) {
        this.course = course;
        this.user = user;
        this.courseId = course.getId();
        this.userId = user.getId();
        this.enrolledAt = new Timestamp(System.currentTimeMillis());
    }
}
