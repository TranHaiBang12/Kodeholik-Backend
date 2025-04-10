package com.g44.kodeholik.model.dto.request.lambda;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResult {
    @SerializedName("isAccepted")
    @JsonProperty("isAccepted")
    private boolean isAccepted;
    private List<TestResult> results;
    private String time;
    private float memoryUsage;
    private int noSuccessTestcase;
    private TestResult inputWrong;
}
