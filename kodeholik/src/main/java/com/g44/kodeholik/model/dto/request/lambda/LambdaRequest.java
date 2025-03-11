package com.g44.kodeholik.model.dto.request.lambda;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonRawValue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LambdaRequest {
    private String language;
    private String code;
    private String functionSignature;
    private String returnType;
    private List<TestCase> testCases;
}
