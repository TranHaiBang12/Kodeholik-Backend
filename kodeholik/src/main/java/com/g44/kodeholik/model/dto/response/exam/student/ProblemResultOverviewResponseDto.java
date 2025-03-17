package com.g44.kodeholik.model.dto.response.exam.student;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.g44.kodeholik.model.dto.response.problem.submission.SubmissionResponseDto;

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

    private int noTestCasePassed;

    private String percentPassed;

    private SubmissionResponseDto submissionResponseDto;

    private double point;

    @JsonIgnore
    private Long submissionId;
}
