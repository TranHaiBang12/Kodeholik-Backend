package com.g44.kodeholik.model.dto.response.problem;

import com.google.auto.value.AutoValue.Builder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TemplateCodeResponseDto {
    private String templateCode;

    private String language;

    private String functionCode;
}
