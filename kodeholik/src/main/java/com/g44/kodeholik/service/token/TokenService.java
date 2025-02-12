package com.g44.kodeholik.service.token;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;

import com.g44.kodeholik.model.enums.token.TokenType;

import jakarta.servlet.http.HttpServletResponse;

public interface TokenService {

    public void saveRefreshToken(String refreshToken, String username);

    public boolean checkRefreshToken(String refreshToken, String username);

    public String generateAccessToken(String username);

    public String generateRefreshToken(String username, Date expiryDate);

    public String extractUsername(String token);

    public boolean validateToken(String token, UserDetails userDetails);

    public void addTokenToCookie(String token, HttpServletResponse response, TokenType tokenType);

    public boolean rotateToken(String refreshToken, HttpServletResponse response);

    public String generateForgotPasswordToken(String username);

    public void deleteCookieFromResponse(HttpServletResponse response, TokenType tokenType);
}
