package com.g44.kodeholik.model.dto.request.problem.add;

import java.util.List;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSubmission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ShareSolutionRequestDto {
    private String link;

    private Problem problem;

    private String title;

    private String textSolution;

    private List<String> skills;

    private List<Long> submissionId;

    private List<ProblemSubmission> submissions;
}
