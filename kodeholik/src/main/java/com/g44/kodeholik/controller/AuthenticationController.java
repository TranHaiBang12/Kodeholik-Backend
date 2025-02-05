package com.g44.kodeholik.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.g44.kodeholik.model.dto.request.user.LoginRequestDto;
import com.g44.kodeholik.service.auth.AuthService;
import com.g44.kodeholik.service.token.TokenService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthService authService;

    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        authService.login(loginRequestDto, response);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/rotate-token")
    public ResponseEntity<?> rotateToken(@CookieValue(name = "refresh_token", required = false) String refreshToken,
            HttpServletResponse response) {
        if (tokenService.rotateToken(refreshToken, response)) {
            return ResponseEntity.noContent().build();

        } else {
            return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body("Refresh token is missing");

        }
    }

}
