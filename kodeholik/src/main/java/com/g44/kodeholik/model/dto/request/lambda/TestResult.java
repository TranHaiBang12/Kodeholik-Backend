package com.g44.kodeholik.model.dto.request.lambda;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.g44.kodeholik.util.serializer.RawJsonDeserializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestResult {
    private int id;

    private List<InputVariable> inputs;

    @JsonRawValue
    @JsonDeserialize(using = RawJsonDeserializer.class)
    private Object expectedOutput;

    private String status;

    private Object actualOutput;

}
