package com.g44.kodeholik.model.dto.response.exam.student;

import java.util.List;

import com.g44.kodeholik.model.dto.request.lambda.TestCase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExamCompileInformationResponseDto {
    private String language;

    private String template;

    private List<TestCase> testCases;
}
