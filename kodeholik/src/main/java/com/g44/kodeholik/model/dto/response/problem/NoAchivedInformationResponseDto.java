package com.g44.kodeholik.model.dto.response.problem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NoAchivedInformationResponseDto {
    private String name;

    private long noAchived;

    private long noTotal;
}
