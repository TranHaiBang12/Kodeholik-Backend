package com.g44.kodeholik.model.dto.response.course.overview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CourseInfoOverviewDto {
    private long id;

    private String title;

    private long totalEnrollments;

    private double avgRating;
}
