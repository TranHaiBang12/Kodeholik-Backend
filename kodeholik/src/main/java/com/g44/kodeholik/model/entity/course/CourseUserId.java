package com.g44.kodeholik.model.entity.course;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseUserId implements Serializable {
    private Long courseId;
    private Long userId;
}
