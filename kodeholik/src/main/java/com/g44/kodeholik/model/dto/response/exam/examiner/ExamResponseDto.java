package com.g44.kodeholik.model.dto.response.exam.examiner;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.g44.kodeholik.model.dto.response.user.UserResponseDto;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.exam.ExamStatus;
import com.g44.kodeholik.util.serializer.TimestampSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExamResponseDto {

    private Long id;

    private String code;

    private String title;

    private String description;

    private int noParticipant;

    private int noSubmission;

    @JsonSerialize(using = TimestampSerializer.class)
    private Long startTime;

    @JsonSerialize(using = TimestampSerializer.class)
    private Long endTime;

    private ExamStatus status;

    private List<String> languageSupports;

    private List<ExamProblemResponseDto> problems;

    @JsonSerialize(using = TimestampSerializer.class)
    private Long createdAt;

    private UserResponseDto createdBy;

    @JsonSerialize(using = TimestampSerializer.class)
    private Long updatedAt;

    private UserResponseDto updatedBy;
}
