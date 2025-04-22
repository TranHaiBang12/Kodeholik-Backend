package com.g44.kodeholik.model.dto.request.problem.compileRequest;

import com.fasterxml.jackson.annotation.JsonRawValue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputTestCase {
    private String name;

    @JsonRawValue
    private Object value;
}
