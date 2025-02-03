package com.g44.kodeholik.model.dto.request.problem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemCompileRequestDto {
    private String code;
    private String languageName;
}
