package com.g44.kodeholik.model.dto.request.lambda;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResult {
    private boolean isAccepted;
    private List<TestResult> results;
    private String time;
    private float memoryUsage;
    private int noSuccessTestcase;
    private TestResult inputWrong;
}
