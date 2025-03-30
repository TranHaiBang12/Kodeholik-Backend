package com.g44.kodeholik.model.dto.response.problem.overview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProblemInfoOverviewDto {
    private String title;

    private String link;

    private long totalSubmissions;

    private double avgAcceptanceRate;

}
