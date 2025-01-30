package com.g44.kodeholik.model.dto.response.problem;

import java.util.List;

import com.g44.kodeholik.model.dto.request.lambda.TestCase;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProblemCompileResponseDto {
    public String template;
    public List<TestCase> testCases;
}
