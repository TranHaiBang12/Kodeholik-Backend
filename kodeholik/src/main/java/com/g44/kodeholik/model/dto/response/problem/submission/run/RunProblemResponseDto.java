package com.g44.kodeholik.model.dto.response.problem.submission.run;

import java.util.List;

import com.g44.kodeholik.model.dto.request.lambda.TestResult;
import com.g44.kodeholik.model.enums.problem.SubmissionStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RunProblemResponseDto {
    private boolean isAccepted;
    private String message;
    private SubmissionStatus status;
    private List<TestResult> results;
}
