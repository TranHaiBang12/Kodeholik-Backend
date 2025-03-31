package com.g44.kodeholik.model.dto.response.course;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.g44.kodeholik.model.dto.response.setting.TopicResponseDto;
import com.g44.kodeholik.model.enums.course.ChapterStatus;
import com.g44.kodeholik.model.enums.course.CourseStatus;
import com.g44.kodeholik.util.serializer.TimestampSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CourseDetailResponseDto {
    private Long id;

    private String title;

    private String description;

    private String image;

    private CourseStatus status;

    private Double rate;

    @JsonSerialize(using = TimestampSerializer.class)
    private Long updatedAt;

    private Integer numberOfParticipant;

    private List<TopicResponseDto> topics;

    private List<ChapterResponseDto> chapters;

    private Double progress;
}
