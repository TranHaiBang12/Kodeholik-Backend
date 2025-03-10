package com.g44.kodeholik.model.entity.course;

import com.g44.kodeholik.model.entity.user.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_lesson_progress", schema = "schema_course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLessonProgress {

    @EmbeddedId
    private UserLessonProgressId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @MapsId("lessonId")
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
}
