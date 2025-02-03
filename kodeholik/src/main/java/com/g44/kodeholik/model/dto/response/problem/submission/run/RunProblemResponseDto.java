package com.g44.kodeholik.model.dto.response.problem.submission.run;

import java.util.List;

import com.g44.kodeholik.model.dto.request.lambda.TestResult;

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
    private List<TestResult> results;
}
