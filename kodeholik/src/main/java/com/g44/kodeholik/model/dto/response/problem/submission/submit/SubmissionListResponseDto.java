package com.g44.kodeholik.model.dto.response.problem.submission.submit;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.g44.kodeholik.model.enums.problem.SubmissionStatus;
import com.g44.kodeholik.util.serializer.TimestampSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SubmissionListResponseDto {
    private SubmissionStatus status;

    private String languageName;

    private double executionTime;

    private double memoryUsage;

    @JsonSerialize(using = TimestampSerializer.class)
    private Long createdAt;
}
