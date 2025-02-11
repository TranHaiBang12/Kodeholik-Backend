package com.g44.kodeholik.service.redis.impl;

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

}
