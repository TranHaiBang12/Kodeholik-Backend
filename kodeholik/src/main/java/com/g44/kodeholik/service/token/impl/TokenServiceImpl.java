package com.g44.kodeholik.service.token.impl;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.exception.MalformedJwtException;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    @Value("${spring.jwt.secret-key}")
    private String secretKey;

    @Value("${spring.jwt.access-token.expiry-time}")
    private int accessTokenExpiryTime;

    @Value("${spring.jwt.refresh-token.expiry-time}")
    private int refreshTokenExpiryTime;

    @Value("${spring.jwt.forgot-token.expiry-time}")
    private int forgotTokenExpiryTime;

    private final UserRepository userRepository;

    private final RedisService redisService;

    @Override
    public String generateAccessToken(String username) {

        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiryTime))
                .and()
                .signWith(getKey())
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String extractUsername(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith((SecretKey) getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new MalformedJwtException("Invalid or expired token", "Invalid or expired token");
        }
    }

    @Override
    public boolean validateToken(String token) {
        final String userName = extractUsername(token);
        return (!isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public String generateRefreshToken(String username, Date expiryDate) {
        Map<String, Object> claims = new HashMap<>();
        String refreshToken = Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(expiryDate.getTime() + refreshTokenExpiryTime))
                .and()
                .signWith(getKey())
                .compact();
        saveRefreshToken(refreshToken, username);
        return refreshToken;
    }

    @Override
    public void addTokenToCookie(String token, HttpServletResponse response, TokenType tokenType) {
        String cookieName = "";
        int expiryTime = 0;
        if (tokenType == TokenType.ACCESS) {
            cookieName = "access_token";
            expiryTime = accessTokenExpiryTime;
        } else {
            cookieName = "refresh_token";
            expiryTime = (int) extractExpiration(token).getTime();
        }
        Cookie tokenCookie = new Cookie(cookieName, token);
        tokenCookie.setHttpOnly(true);

        tokenCookie.setPath("/");
        tokenCookie.setMaxAge(expiryTime / 1000);

        response.addCookie(tokenCookie);

    }

    @Override
    public boolean rotateToken(String refreshToken, HttpServletResponse response) {
        if (refreshToken != null && !refreshToken.equals("")) {
            String username = extractUsername(refreshToken);
            if (userRepository.existsByUsernameOrEmail(username).isPresent()
                    && (validateToken(refreshToken) && checkRefreshToken(refreshToken, username))) {
                String accessToken = generateAccessToken(username);
                refreshToken = generateRefreshToken(username, extractExpiration(refreshToken));
                addTokenToCookie(accessToken, response, TokenType.ACCESS);
                addTokenToCookie(refreshToken, response, TokenType.REFRESH);
                return true;
            }
        }
        return false;
    }

    @Override
    public void saveRefreshToken(String refreshToken, String username) {
        redisService.saveToken(username, refreshToken, refreshTokenExpiryTime, TokenType.REFRESH);
    }

    @Override
    public boolean checkRefreshToken(String refreshToken, String username) {
        String savedToken = redisService.getToken(username, TokenType.REFRESH);
        if (savedToken != null && refreshToken.equals(savedToken.trim())) {
            return true;
        }
        return false;
    }

    @Override
    public String generateForgotPasswordToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + forgotTokenExpiryTime))
                .and()
                .signWith(getKey())
                .compact();
    }

    @Override
    public void deleteCookieFromResponse(HttpServletResponse response, TokenType tokenType) {
        String cookieName = "";
        if (tokenType == TokenType.ACCESS) {
            cookieName = "access_token";
        } else {
            cookieName = "refresh_token";
        }
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setHttpOnly(true);
        cookie.setPath("/"); // Đảm bảo cùng path với cookie ban đầu
        cookie.setMaxAge(0); // Đặt thời gian sống về 0 để trình duyệt xóa cookie
        response.addCookie(cookie);
    }

}
