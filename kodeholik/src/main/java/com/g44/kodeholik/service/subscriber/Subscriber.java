package com.g44.kodeholik.service.subscriber;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class Subscriber implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;

    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] arg1) {

        try {
            String messageBody = new String(message.getBody());
            Map<String, Object> map = objectMapper.readValue(messageBody, new TypeReference<Map<String, Object>>() {
            });
            if (map.get("error") != null) {
                log.info(map.get("error") + " " + map.get("username"));
                messagingTemplate.convertAndSend("/error/" + map.get("username"), map.get("error"));
            } else if (map.get("notification") != null) {
                log.info(map.get("notification") + " " + map.get("username"));
                messagingTemplate.convertAndSend("/notification/" + map.get("username"), map.get("notification"));
            } else {
                log.info(objectMapper.writeValueAsString(map));
                messagingTemplate.convertAndSend("/topic/exam/" + map.get("code"),
                        objectMapper.writeValueAsString(map));
            }
        } catch (IOException e) {
            log.error("❌ Lỗi khi parse message từ Redis", e);
        }
    }

}
