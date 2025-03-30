package com.g44.kodeholik.model.dto.response.problem.overview;

import java.util.List;

import com.g44.kodeholik.model.dto.response.problem.NoAchivedInformationResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProblemOverviewReportDto {
    private List<NoAchivedInformationResponseDto> noAchivedInformationList;

    private long totalProblemCount;

    private long totalSubmissions;

    private double avgAcceptanceRate;

    private List<ProblemInfoOverviewDto> topPopularProblems;

    private List<ProblemInfoOverviewDto> topFlopProblems;

}
