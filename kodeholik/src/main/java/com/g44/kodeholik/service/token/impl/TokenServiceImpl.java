package com.g44.kodeholik.service.token.impl;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.model.dto.request.user.LoginRequestDto;
import com.g44.kodeholik.model.enums.token.TokenType;
import com.g44.kodeholik.service.token.TokenService;
import com.g44.kodeholik.service.user.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    @Value("${spring.jwt.secret-key}")
    private String secretKey;

    @Value("${spring.jwt.access-token.expiry-time}")
    private int accessTokenExpiryTime;

    @Value("${spring.jwt.refresh-token.expiry-time}")
    private int refreshTokenExpiryTime;

    private final UserDetailsService userDetailsService;

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
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUsername(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiryTime))
                .and()
                .signWith(getKey())
                .compact();
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
            expiryTime = refreshTokenExpiryTime;
        }
        Cookie tokenCookie = new Cookie(cookieName, token);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setPath("/");
        tokenCookie.setMaxAge(expiryTime / 1000);

        response.addCookie(tokenCookie);

    }

    @Override
    public boolean rotateToken(String refreshToken, HttpServletResponse response) {
        String username = extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (refreshToken != null && validateToken(refreshToken, userDetails)) {
            String accessToken = generateAccessToken(username);
            refreshToken = generateRefreshToken(username);
            addTokenToCookie(accessToken, response, TokenType.ACCESS);
            addTokenToCookie(refreshToken, response, TokenType.REFRESH);
            return true;
        }
        return false;
    }

}
