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
public class TestResult {
    private int id;

    private List<InputVariable> inputs;

    private Object expectedOutput;

    private String status;

    private Object actualOutput;

}
