package com.g44.kodeholik.model.dto.request.course;

import com.g44.kodeholik.model.enums.course.CourseStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CourseRequestDto {
    private String title;

    private String description;

    private String image;

    private CourseStatus status;

    private Set<Long> topicIds;
}
