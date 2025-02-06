package com.g44.kodeholik.service.redis.impl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.service.redis.RedisService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void saveRefreshToken(String username, String refreshToken, long expirationTime) {
        redisTemplate.opsForValue().set("refresh_token: " + username, refreshToken, expirationTime);
    }

    @Override
    public String getRefreshToken(String username) {
        return (String) redisTemplate.opsForValue().get("refresh_token: " + username);
    }

    @Override
    public void deleteRefreshToken(String username) {
        redisTemplate.delete("refresh_token: " + username);
    }

}
