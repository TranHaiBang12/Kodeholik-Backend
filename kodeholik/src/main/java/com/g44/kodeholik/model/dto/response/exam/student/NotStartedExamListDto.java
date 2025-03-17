package com.g44.kodeholik.model.dto.response.exam.student;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.g44.kodeholik.util.serializer.TimestampSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NotStartedExamListDto {
    private String code;

    private String title;

    @JsonSerialize(using = TimestampSerializer.class)
    private Long startTime;

    @JsonSerialize(using = TimestampSerializer.class)
    private Long endTime;

    private boolean canEnroll;

}
