package com.g44.kodeholik.model.entity.course;

import com.g44.kodeholik.model.entity.discussion.Comment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

@Entity
@FieldNameConstants
@Table(name = "course_comment", schema = "schema_course")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CourseComment {

    @EmbeddedId
    private CourseCommentId id;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @ToString.Exclude
    private Course course;

    @ManyToOne
    @MapsId("commentId")
    @JoinColumn(name = "comment_id", referencedColumnName = "id")
    @ToString.Exclude
    private Comment comment;
}
