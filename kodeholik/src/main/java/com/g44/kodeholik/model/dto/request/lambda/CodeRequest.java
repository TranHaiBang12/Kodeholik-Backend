package com.g44.kodeholik.model.dto.request.lambda;

import java.util.List;
import java.util.Map;

import com.g44.kodeholik.model.enums.problem.InputType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CodeRequest {
    private String language;
    private String code;
    private String functionSignature;
    private String returnType;
    private List<TestCase> testCases;
}
