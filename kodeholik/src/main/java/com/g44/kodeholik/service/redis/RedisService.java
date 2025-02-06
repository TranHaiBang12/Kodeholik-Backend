package com.g44.kodeholik.service.redis;

public interface RedisService {
    public void saveRefreshToken(String username, String refreshToken, long expirationTime);

    public String getRefreshToken(String username);

    public void deleteRefreshToken(String username);
}
