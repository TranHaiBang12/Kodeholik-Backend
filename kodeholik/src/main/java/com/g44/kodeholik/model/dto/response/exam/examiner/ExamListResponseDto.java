package com.g44.kodeholik.model.dto.response.exam.examiner;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.g44.kodeholik.model.dto.response.user.UserResponseDto;
import com.g44.kodeholik.model.enums.exam.ExamStatus;
import com.g44.kodeholik.util.serializer.TimestampSerializer;

import jakarta.persistence.PostLoad;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExamListResponseDto {
    private String code;

    private String title;

    private int noParticipant;

    @JsonSerialize(using = TimestampSerializer.class)
    private Long startTime;

    @JsonSerialize(using = TimestampSerializer.class)
    private Long endTime;

    private ExamStatus status;

    private UserResponseDto createdBy;

    @JsonSerialize(using = TimestampSerializer.class)
    private Long createdAt;

}
