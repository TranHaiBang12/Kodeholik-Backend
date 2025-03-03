package com.g44.kodeholik.model.dto.response.exam;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExamProblemResponseDto {
    private Long id;

    private String problemTitle;

    private String problemLink;

    private double problemPoint;
}
