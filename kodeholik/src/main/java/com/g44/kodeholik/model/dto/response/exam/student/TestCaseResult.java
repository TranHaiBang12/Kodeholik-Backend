package com.g44.kodeholik.model.dto.response.exam.student;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.g44.kodeholik.model.dto.request.lambda.InputVariable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TestCaseResult {
    private List<InputVariable> input;
    @JsonRawValue
    private Object expectedOutput;

    private boolean isPassed;
}
