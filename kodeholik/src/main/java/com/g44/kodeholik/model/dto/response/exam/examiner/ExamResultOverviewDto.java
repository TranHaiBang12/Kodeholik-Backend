package com.g44.kodeholik.model.dto.response.exam.examiner;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExamResultOverviewDto {
    private String avgGrade;

    private String submittedPercent;

    private String excellentGradePercent;

    private String goodGradePercent;

    private String badGradePercent;

    private List<Object[]> gradeDistribution;

    private List<Object[]> avgProblemPoints;
}
