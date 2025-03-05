package com.g44.kodeholik.model.dto.response.exam.student;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProblemResultOverviewResponseDto {

    private Long id;

    private String link;

    private String title;

    private String code;

    private String languageName;

    private int noTestCasePassed;

    private String percentPassed;

    private double point;

    @JsonIgnore
    private Long submissionId;
}
