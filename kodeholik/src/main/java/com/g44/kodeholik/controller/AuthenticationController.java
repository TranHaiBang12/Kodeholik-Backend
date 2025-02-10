package com.g44.kodeholik.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.g44.kodeholik.model.dto.request.user.LoginRequestDto;
import com.g44.kodeholik.model.entity.user.UserPrincipal;
import com.g44.kodeholik.service.auth.AuthService;
import com.g44.kodeholik.service.token.TokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.security.Principal;
import java.util.Enumeration;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthService authService;

    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        authService.loginNormal(loginRequestDto, response);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/login/oauth2/google")
    public ResponseEntity<?> loginWithGoogle(
            OAuth2AuthenticationToken oAuth2User,
            HttpServletResponse response,
            HttpServletRequest request) {
        // authService.loginWithGoogle(oAuth2User, response, request);
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

    @PostMapping("/reset-password-init")
    public ResponseEntity<?> resetPasswordInit(@RequestParam String username) {
        authService.resetPasswordInit(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reset-password-check")
    public ResponseEntity<?> resetPasswordCheckToken(@RequestParam String token) {
        if (authService.checkValidForgotPasswordToken(token)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body("Invalid token");
        }
    }

    @PostMapping("/reset-password-finish")
    public ResponseEntity<?> resetPasswordFinish(@RequestParam String token, @RequestBody String newPassword) {
        authService.resetPasswordFinish(token, newPassword);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseEntity.noContent().build();
    }

}
