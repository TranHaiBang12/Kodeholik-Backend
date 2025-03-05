package com.g44.kodeholik.service.redis.impl;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.model.enums.token.TokenType;
import com.g44.kodeholik.service.redis.RedisService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<Object, Object> redisTemplate;

    private static final String REFRESH_TOKEN_PREFIX = "refresh_token_";
    private static final String FORGOT_TOKEN_PREFIX = "forgot_token_";

    private String getPrefix(String username, TokenType tokenType) {
        return tokenType == TokenType.REFRESH ? REFRESH_TOKEN_PREFIX + username : FORGOT_TOKEN_PREFIX + username;
    }

    @Async("redisTaskExecutor")
    @Override
    public void saveToken(String username, String refreshToken, long expirationTime, TokenType tokenType) {
        deleteToken(username, tokenType);
        redisTemplate.opsForValue().set(
                getPrefix(username, tokenType), refreshToken,
                expirationTime);
    }

    @Override
    public String getToken(String username, TokenType tokenType) {
        return (String) redisTemplate.opsForValue()
                .get(getPrefix(username, tokenType));
    }

    @Async("redisTaskExecutor")
    @Override
    public void deleteToken(String username, TokenType tokenType) {
        redisTemplate.delete(getPrefix(username, tokenType));
    }

    @Override
    public void saveKeyCheckExamReminder(String username, String code) {
        redisTemplate.opsForValue().set(
                getPrefixForExamReminder(username, code), code,
                Duration.ofMinutes(30));
    }

    @Override
    public String getKeyCheckExamReminder(String username, String code) {
        return (String) redisTemplate.opsForValue()
                .get(getPrefixForExamReminder(username, code));
    }

    @Override
    public boolean isUserRemindedForExam(String username, String code) {
        return redisTemplate.hasKey(getPrefixForExamReminder(username, code));
    }

    private String getPrefixForExamReminder(String username, String code) {
        return "EXAM_REMINDER_" + username + "_" + code;
    }

}
