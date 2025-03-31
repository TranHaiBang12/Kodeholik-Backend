package com.g44.kodeholik.model.dto.response.problem.solution;

import java.sql.Timestamp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.g44.kodeholik.model.dto.response.user.ProfileResponseDto;
import com.g44.kodeholik.model.dto.response.user.UserResponseDto;
import com.g44.kodeholik.util.serializer.TimestampSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SolutionListResponseDto {
    private Long id;

    private String title;

    private int noUpvote;

    private int noComment;

    @JsonSerialize(using = TimestampSerializer.class)
    private Long createdAt;

    private UserResponseDto createdBy;

    private boolean isCurrentUserCreated;

    private boolean isCurrentUserVoted;
}
