package com.g44.kodeholik.model.dto.response.course;

import com.g44.kodeholik.model.enums.course.CourseStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CourseResponseDto {
    private Long id;

    private String title;

    private String description;

    private String image;

    private CourseStatus status;
}
