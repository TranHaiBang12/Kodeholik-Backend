package com.g44.kodeholik.model.dto.response.openai;

import java.util.List;

import com.g44.kodeholik.model.dto.request.openai.Message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OpenAIResponseDto {
    private List<Choice> choices;

    @Data
    public static class Choice {
        private Message message;
    }
}
