package com.g44.kodeholik.model.entity.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseCommentId implements Serializable {
    private Long courseId;
    private Long commentId;
}
