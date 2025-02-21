package com.g44.kodeholik.model.dto.request.problem.search;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SearchProblemRequestDto {
    private String title;

    private List<String> difficulty;

    private List<String> topics;

    private List<String> skills;
}
