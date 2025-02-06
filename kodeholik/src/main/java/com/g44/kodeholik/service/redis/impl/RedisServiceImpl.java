package com.g44.kodeholik.service.redis.impl;

import org.springframework.data.redis.core.RedisTemplate;
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

    @Override
    public void saveToken(String username, String refreshToken, long expirationTime, TokenType tokenType) {
        log.info(username);
        redisTemplate.opsForValue().set(
                tokenType == TokenType.REFRESH ? "refresh_token_" + username : "forgot_token_" + username, refreshToken,
                expirationTime);
    }

    @Override
    public String getToken(String username, TokenType tokenType) {
        return (String) redisTemplate.opsForValue()
                .get(tokenType == TokenType.REFRESH ? "refresh_token_" + username : "forgot_token_" + username);
    }

    @Override
    public void deleteToken(String username, TokenType tokenType) {
        redisTemplate.delete(tokenType == TokenType.REFRESH ? "refresh_token_" + username : "forgot_token_" + username);
    }

}
