package com.g44.kodeholik.service.exam.publisher;

import java.util.Map;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class ExamPublisher {
    private final RedisTemplate<Object, Object> redisTemplate;

    private final ChannelTopic topic;

    private final ObjectMapper objectMapper;

    public void startExam(Map<String, Object> examInfo) {
        try {
            redisTemplate.convertAndSend(topic.getTopic(), objectMapper.writeValueAsString(examInfo));
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    public void sendError(Map<String, Object> errorInfo) {
        try {
            redisTemplate.convertAndSend(topic.getTopic(), objectMapper.writeValueAsString(errorInfo));
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}
