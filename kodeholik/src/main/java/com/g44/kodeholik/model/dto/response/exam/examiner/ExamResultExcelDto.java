package com.g44.kodeholik.model.dto.response.exam.examiner;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExamResultExcelDto {
    private String username;

    private String fullname;

    private double grade;

    private List<ProblemPoint> problemPoints;

    private boolean isSubmitted;
}
