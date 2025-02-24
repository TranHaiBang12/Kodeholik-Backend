package com.g44.kodeholik.model.dto.response.course;

import com.g44.kodeholik.model.enums.course.CourseStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    private Double rate;

    private Integer numberOfParticipant;

    private List<ChapterResponseDto> chapters;
//    private List<TopicDto> topics;
}
