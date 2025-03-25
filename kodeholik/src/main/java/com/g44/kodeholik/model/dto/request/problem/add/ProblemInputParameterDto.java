package com.g44.kodeholik.model.dto.request.problem.add;

import java.util.List;

import com.g44.kodeholik.model.enums.problem.InputType;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProblemInputParameterDto {
    private TemplateCode templateCode;

    @NotBlank(message = "MSG02")
    private String functionSignature;

    private InputType returnType;

    private String otherReturnType;

    private Integer noDimension;

    private String language;

    private List<InputParameterDto> parameters;

}
