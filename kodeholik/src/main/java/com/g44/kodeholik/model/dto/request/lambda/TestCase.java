package com.g44.kodeholik.model.dto.request.lambda;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonRawValue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TestCase {
    private List<InputVariable> input;
    @JsonRawValue
    private Object expectedOutput;
}
