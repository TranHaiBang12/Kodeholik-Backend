package com.g44.kodeholik.model.dto.request.openai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Message {

    private String role;
    private String content;

}
