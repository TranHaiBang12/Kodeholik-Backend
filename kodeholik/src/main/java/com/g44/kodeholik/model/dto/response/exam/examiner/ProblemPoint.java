package com.g44.kodeholik.model.dto.response.exam.examiner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProblemPoint {
    private String title;
    private double point;
}
