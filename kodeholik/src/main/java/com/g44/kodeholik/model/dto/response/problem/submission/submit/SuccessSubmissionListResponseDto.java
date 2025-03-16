package com.g44.kodeholik.model.dto.response.problem.submission.submit;

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
public class SuccessSubmissionListResponseDto {
    private Long id;

    private String code;

    @JsonSerialize(using = TimestampSerializer.class)
    private Long createdAt;

    private String languageName;
}
