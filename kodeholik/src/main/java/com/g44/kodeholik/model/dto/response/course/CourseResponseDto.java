package com.g44.kodeholik.model.dto.response.course;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.g44.kodeholik.model.dto.response.user.UserResponseDto;
import com.g44.kodeholik.model.enums.course.CourseStatus;

import com.g44.kodeholik.util.serializer.TimestampSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CourseResponseDto {
    private Long id;

    private String title;

    private String image;

    private CourseStatus status;

    private Double rate;

    private Integer numberOfParticipant;

    @JsonSerialize(using = TimestampSerializer.class)
    private Long createdAt;

    private List<String> topics;

    private Double progress;

    private UserResponseDto createdBy;

    private Integer noVote;

    private Integer noChapter;

    private Integer noLesson;
}
