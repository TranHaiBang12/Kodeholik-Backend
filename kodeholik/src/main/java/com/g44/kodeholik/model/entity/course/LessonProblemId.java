package com.g44.kodeholik.model.entity.course;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LessonProblemId implements Serializable {

    @Column(name = "lesson_id")
    private Long lessonId;

    @Column(name = "problem_id")
    private Long problemId;
}

