package com.g44.kodeholik.model.dto.response.course;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.g44.kodeholik.util.serializer.TimestampSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CourseRatingResponseDto {
    private Long id;
    private Long courseId;
    private Long userId;
    private Integer rating;
    private String comment;
    @JsonSerialize(using = TimestampSerializer.class)
    private Long createdAt;
    @JsonSerialize(using = TimestampSerializer.class)
    private Long updatedAt;
}
