package com.g44.kodeholik.model.dto.request.openai;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OpenAIRequestDto {
    private String model = "gpt-4o-mini";
    private List<Message> messages;
}
