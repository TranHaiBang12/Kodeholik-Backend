package com.g44.kodeholik.model.dto.request.problem;

import java.util.List;

import com.g44.kodeholik.model.dto.request.lambda.TestCase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemCompileRequestDto {
    private String code;
    private String languageName;
    private List<TestCase> testCases;
}
