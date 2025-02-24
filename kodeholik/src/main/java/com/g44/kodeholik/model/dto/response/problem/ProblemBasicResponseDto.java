package com.g44.kodeholik.model.dto.response.problem;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.g44.kodeholik.model.dto.response.user.EmployeeResponseDto;
import com.g44.kodeholik.model.enums.problem.Difficulty;
import com.g44.kodeholik.model.enums.problem.ProblemStatus;
import com.g44.kodeholik.util.serializer.TimestampSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProblemBasicResponseDto {

    private Long id;

    private String title;

    private Difficulty difficulty;

    private String description;

    private ProblemStatus status;

    private List<String> topics;

    private List<String> skills;

    private Boolean isActive;

    @JsonSerialize(using = TimestampSerializer.class)
    private Long createdAt;

    private EmployeeResponseDto createdBy;

    @JsonSerialize(using = TimestampSerializer.class)
    private Long updatedAt;

    private EmployeeResponseDto updatedBy;
}
