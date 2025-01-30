package com.g44.kodeholik.model.dto.request.lambda;

import com.g44.kodeholik.model.enums.problem.InputType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InputVariable {
    private String name;
    private String type;
    private Object value;
}
