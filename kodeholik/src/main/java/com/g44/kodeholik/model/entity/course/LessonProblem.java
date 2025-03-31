package com.g44.kodeholik.model.entity.course;


import com.g44.kodeholik.model.entity.problem.Problem;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lesson_problem", schema = "schema_course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LessonProblem {

    @EmbeddedId
    private LessonProblemId id;

    @ManyToOne
    @MapsId("lessonId")
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @ManyToOne
    @MapsId("problemId")
    @JoinColumn(name = "problem_id")
    private Problem problem;

    public LessonProblem(Lesson lesson, Problem problem) {
        this.id = new LessonProblemId(lesson.getId(), problem.getId());
        this.lesson = lesson;
        this.problem = problem;
    }
}

