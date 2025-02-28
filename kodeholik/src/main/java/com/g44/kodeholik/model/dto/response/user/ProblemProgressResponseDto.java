package com.g44.kodeholik.model.dto.response.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.g44.kodeholik.model.enums.problem.Difficulty;
import com.g44.kodeholik.model.enums.user.ProgressType;
import com.g44.kodeholik.util.serializer.TimestampSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProblemProgressResponseDto {
    private String problemTitle;

    private String problemLink;

    private Difficulty difficulty;

    private ProgressType progressType;

    private int noSubmission;

    @JsonSerialize(using = TimestampSerializer.class)
    private Long lastSubmitted;
}
