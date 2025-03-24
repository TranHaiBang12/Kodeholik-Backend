package com.g44.kodeholik.model.dto.request.lambda;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.g44.kodeholik.util.serializer.RawJsonDeserializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InputVariable {
    private String name;
    private String type;
    @JsonRawValue
    @JsonDeserialize(using = RawJsonDeserializer.class)
    private Object value;
    private Integer noDimension;
    @JsonProperty("isSample")
    private boolean isSample;

    public InputVariable(String name, String type, Object value, Integer noDimension) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.noDimension = noDimension;
    }

    @JsonProperty("isSample")
    public boolean isSample() {
        return isSample;
    }

}
