package com.g44.kodeholik.service.redis.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import com.g44.kodeholik.model.enums.token.TokenType;

public class RedisServiceImplTest {

    @Mock
    private RedisTemplate<Object, Object> redisTemplate;

    @Mock
    private ValueOperations<Object, Object> valueOperations;

    @InjectMocks
    private RedisServiceImpl redisServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    public void testSaveToken() {
        String username = "user1";
        String refreshToken = "token123";
        long expirationTime = 3600L;
        TokenType tokenType = TokenType.REFRESH;

        redisServiceImpl.saveToken(username, refreshToken, expirationTime, tokenType);

        verify(redisTemplate).delete("refresh_token_user1");
        verify(valueOperations).set("refresh_token_user1", refreshToken, expirationTime);
    }

    @Test
    public void testGetToken() {
        String username = "user1";
        TokenType tokenType = TokenType.REFRESH;
        String expectedToken = "token123";

        when(valueOperations.get("refresh_token_user1")).thenReturn(expectedToken);

        String actualToken = redisServiceImpl.getToken(username, tokenType);

        verify(valueOperations).get("refresh_token_user1");
        assertEquals(expectedToken, actualToken);
    }

    @Test
    public void testDeleteToken() {
        String username = "user1";
        TokenType tokenType = TokenType.REFRESH;

        redisServiceImpl.deleteToken(username, tokenType);

        verify(redisTemplate).delete("refresh_token_user1");
    }

    @Test
    public void testSaveKeyCheckExamReminder() {
        String username = "user1";
        String code = "exam123";

        redisServiceImpl.saveKeyCheckExamReminder(username, code);

        verify(valueOperations).set("EXAM_REMINDER_user1_exam123", code, Duration.ofMinutes(30));
    }

    @Test
    public void testGetKeyCheckExamReminder() {
        String username = "user1";
        String code = "exam123";
        String expectedCode = "exam123";

        when(valueOperations.get("EXAM_REMINDER_user1_exam123")).thenReturn(expectedCode);

        String actualCode = redisServiceImpl.getKeyCheckExamReminder(username, code);

        verify(valueOperations).get("EXAM_REMINDER_user1_exam123");
        assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testIsUserRemindedForExam() {
        String username = "user1";
        String code = "exam123";

        when(redisTemplate.hasKey("EXAM_REMINDER_user1_exam123")).thenReturn(true);

        boolean isReminded = redisServiceImpl.isUserRemindedForExam(username, code);

        verify(redisTemplate).hasKey("EXAM_REMINDER_user1_exam123");
        assertTrue(isReminded);
    }
}