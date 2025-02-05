package com.g44.kodeholik.service.auth.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.model.dto.request.user.LoginRequestDto;
import com.g44.kodeholik.model.enums.token.TokenType;
import com.g44.kodeholik.service.auth.AuthService;
import com.g44.kodeholik.service.token.TokenService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;
    @Override
    public boolean verify(LoginRequestDto loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));
        return authentication.isAuthenticated();
    }

    @Override
    public void login(LoginRequestDto loginRequest, HttpServletResponse response) {
        verify(loginRequest);
        String username = loginRequest.getUsername();
        String accessToken = tokenService.generateAccessToken(username);
        tokenService.addTokenToCookie(accessToken, response, TokenType.ACCESS);
        String refreshToken = tokenService.generateRefreshToken(username);
        tokenService.addTokenToCookie(refreshToken, response, TokenType.REFRESH);
    }

}
