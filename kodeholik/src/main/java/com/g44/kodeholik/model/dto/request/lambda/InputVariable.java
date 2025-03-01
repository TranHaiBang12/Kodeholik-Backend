package com.g44.kodeholik.model.dto.request.lambda;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InputVariable {
    private String name;
    private String type;
    private Object value;

}
