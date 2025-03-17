package com.g44.kodeholik.service.openai.impl;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.g44.kodeholik.model.dto.request.openai.Message;
import com.g44.kodeholik.model.dto.request.openai.OpenAIRequestDto;
import com.g44.kodeholik.model.dto.response.openai.OpenAIResponseDto;
import com.g44.kodeholik.service.openai.OpenAIService;

@Service
public class OpenAIServiceImpl implements OpenAIService {

    @Value("${open-ai.url}")
    private String openAiUrl;

    @Value("${open-ai.api-key}")
    private String openAiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String generateContentEmailReminder() {
        Message message = new Message();
        message.setRole("user");
        message.setContent("Hãy tạo một email nhắc nhở hài hước, cá nhân hóa cho "
                + "Bằng" + " về việc học sau 1 ngày chưa vào học");

        OpenAIRequestDto request = new OpenAIRequestDto();
        request.setMessages(Collections.singletonList(message));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiApiKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<OpenAIRequestDto> entity = new HttpEntity<>(request, headers);
        ResponseEntity<OpenAIResponseDto> response = restTemplate.exchange(openAiUrl, HttpMethod.POST, entity,
                OpenAIResponseDto.class);

        return response.getBody().getChoices().get(0).getMessage().getContent();
    }

}
