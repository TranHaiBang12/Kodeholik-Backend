package com.g44.kodeholik.model.dto.response.problem;

import java.util.List;

import com.g44.kodeholik.model.enums.problem.Difficulty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProblemDescriptionResponseDto {
    private Long id;

    private String title;

    private String description;

    private Difficulty difficulty;

    private float acceptanceRate;

    private int noSubmission;

    private int noAccepted;

    private List<String> topicList;
}
