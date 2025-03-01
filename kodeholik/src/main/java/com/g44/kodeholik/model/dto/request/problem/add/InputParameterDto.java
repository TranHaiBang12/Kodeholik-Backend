package com.g44.kodeholik.model.dto.request.problem.add;

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
public class InputParameterDto {
    @NotBlank(message = "Please input a name")
    private String inputName;

    private InputType inputType;

    private String otherInputType;

    private Integer noDimension;
}
