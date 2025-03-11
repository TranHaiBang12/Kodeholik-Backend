package com.g44.kodeholik.model.dto.response.problem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProblemShortResponseDto {
    private Long id;

    private String title;

    private String link;
}
