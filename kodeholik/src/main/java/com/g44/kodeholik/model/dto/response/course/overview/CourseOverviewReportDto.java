package com.g44.kodeholik.model.dto.response.course.overview;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseOverviewReportDto {
    private long totalCourseCount;

    private long totalEnrollments;

    private double avgRating;

    private List<CourseInfoOverviewDto> topPopularCourses;

    private List<CourseInfoOverviewDto> topFlopCourses;
}
