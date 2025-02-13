package com.g44.kodeholik.model.dto.request.problem.add;

import com.g44.kodeholik.model.enums.problem.InputType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InputParameterDto {
    private String name;

    private InputType type;
}
