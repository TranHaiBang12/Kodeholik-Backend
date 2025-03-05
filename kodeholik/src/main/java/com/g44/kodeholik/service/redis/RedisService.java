package com.g44.kodeholik.service.redis;

import com.g44.kodeholik.model.enums.token.TokenType;

public interface RedisService {
    public void saveToken(String username, String refreshToken, long expirationTime, TokenType tokenType);

    public String getToken(String username, TokenType tokenType);

    public void deleteToken(String username, TokenType tokenType);

    public void saveKeyCheckExamReminder(String username, String code);

    public String getKeyCheckExamReminder(String username, String code);

    public boolean isUserRemindedForExam(String username, String code);
}
