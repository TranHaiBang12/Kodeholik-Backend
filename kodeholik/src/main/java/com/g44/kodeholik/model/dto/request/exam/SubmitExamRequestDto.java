package com.g44.kodeholik.model.dto.request.exam;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SubmitExamRequestDto {
    private String problemLink;

    private String code;

    private String languageName;

    private double point;
}
