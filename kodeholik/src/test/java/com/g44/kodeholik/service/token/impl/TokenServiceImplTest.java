package com.g44.kodeholik.service.token.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;

import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.token.TokenType;
import com.g44.kodeholik.repository.user.UserRepository;
import com.g44.kodeholik.service.redis.RedisService;
import com.g44.kodeholik.service.token.TokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

class TokenServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RedisService redisService;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private TokenServiceImpl tokenService;

    @Value("${spring.jwt.secret-key}")
    private String secretKey = "d2f21asdabkocxz213123ADSAKDOACZXCMZKADKASODKWOEMKQMKCMAKDMASKDAKDMAKDASM";

    @Value("${spring.jwt.access-token.expiry-time}")
    private int accessTokenExpiryTime = 600000;

    @Value("${spring.jwt.refresh-token.expiry-time}")
    private int refreshTokenExpiryTime = 1200000;

    @Value("${spring.jwt.forgot-token.expiry-time}")
    private int forgotTokenExpiryTime = 300000;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        tokenService = new TokenServiceImpl(userRepository, redisService);
        Field secretKeyField = TokenServiceImpl.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true); // Cho phép truy cập private field
        secretKeyField.set(tokenService, secretKey);

        Field accessTime = TokenServiceImpl.class.getDeclaredField("accessTokenExpiryTime");
        accessTime.setAccessible(true); // Cho phép truy cập private field
        accessTime.set(tokenService, accessTokenExpiryTime);

        Field refreshTime = TokenServiceImpl.class.getDeclaredField("refreshTokenExpiryTime");
        refreshTime.setAccessible(true); // Cho phép truy cập private field
        refreshTime.set(tokenService, refreshTokenExpiryTime);
    }

    @Test
    void testGenerateAccessToken() {
        String username = "testUser";
        String token = tokenService.generateAccessToken(username);
        assertNotNull(token);
    }

    @Test
    void testExtractUsername() {
        String username = "testUser";
        String token = tokenService.generateAccessToken(username);
        String extractedUsername = tokenService.extractUsername(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    void testValidateToken() {
        String username = "testUser";
        String token = tokenService.generateAccessToken(username);
        assertTrue(tokenService.validateToken(token));
    }

    @Test
    void testGenerateRefreshToken() {
        String username = "testUser";
        Date expiryDate = new Date(System.currentTimeMillis() + refreshTokenExpiryTime);
        String refreshToken = tokenService.generateRefreshToken(username, expiryDate);
        assertNotNull(refreshToken);
    }

    @Test
    void testAddTokenToCookie() {
        String token = "testToken";
        tokenService.addTokenToCookie(token, response, TokenType.ACCESS);
        verify(response, times(1)).addCookie(any(Cookie.class));
    }

    @Test
    void testRotateToken() {
        String username = "testUser";
        Users user = new Users();
        user.setUsername(username);

        String refreshToken = tokenService.generateRefreshToken(username, new Date());
        when(userRepository.existsByUsernameOrEmail(username)).thenReturn(Optional.of(user));
        when(redisService.getToken(username, TokenType.REFRESH)).thenReturn(refreshToken);
        assertTrue(tokenService.rotateToken(refreshToken, response));
    }

    @Test
    void testSaveRefreshToken() {
        String username = "testUser";
        String refreshToken = "testRefreshToken";
        tokenService.saveRefreshToken(refreshToken, username);
        verify(redisService, times(1)).saveToken(username, refreshToken, refreshTokenExpiryTime, TokenType.REFRESH);
    }

    @Test
    void testCheckRefreshToken() {
        String username = "testUser";
        String refreshToken = "testRefreshToken";
        when(redisService.getToken(username, TokenType.REFRESH)).thenReturn(refreshToken);
        assertTrue(tokenService.checkRefreshToken(refreshToken, username));
    }

    @Test
    void testGenerateForgotPasswordToken() {
        String username = "testUser";
        String token = tokenService.generateForgotPasswordToken(username);
        assertNotNull(token);
    }

    @Test
    void testDeleteCookieFromResponse() {
        tokenService.deleteCookieFromResponse(response, TokenType.ACCESS);
        verify(response, times(1)).addCookie(any(Cookie.class));
    }
}