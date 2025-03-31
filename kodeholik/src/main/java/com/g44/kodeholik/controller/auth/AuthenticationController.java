package com.g44.kodeholik.controller.auth;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.g44.kodeholik.model.dto.request.user.ChangePasswordRequestDto;
import com.g44.kodeholik.model.dto.request.user.LoginRequestDto;
import com.g44.kodeholik.service.auth.AuthService;
import com.g44.kodeholik.service.token.TokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import jakarta.validation.Valid;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthService authService;

    private final TokenService tokenService;

    @Value("${spring.security.oauth2.user-google-url}")
    private String userGoogleUrl;

    @Value("${spring.security.oauth2.user-github-url}")
    private String userGithubUrl;

    @Value("${spring.security.oauth2.emp-google-url}")
    private String empGoogleUrl;

    @Value("${spring.security.oauth2.emp-github-url}")
    private String empGithubUrl;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto loginRequestDto, HttpServletResponse response) {
        authService.loginNormal(loginRequestDto, response);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login-admin")
    public ResponseEntity<?> loginAdmin(@RequestBody @Valid LoginRequestDto loginRequestDto,
            HttpServletResponse response) {
        authService.loginAdmin(loginRequestDto, response);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequestDto changePasswordRequestDto,
            HttpServletResponse response) {
        authService.changePassword(changePasswordRequestDto, response);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/login/oauth2/google")
    public void loginOauth2(
            OAuth2AuthenticationToken oAuth2User,
            HttpServletResponse response,
            HttpServletRequest request,
            @RequestParam int port) throws IOException {
        // authService.loginWithGoogle(oAuth2User, response, request);
        if (port == 5174) {
            response.sendRedirect(userGoogleUrl);
        } else {
            response.sendRedirect(empGoogleUrl);
        }
    }

    @PostMapping("/rotate-token")
    public ResponseEntity<?> rotateToken(@CookieValue(name = "refresh_token", required = false) String refreshToken,
            HttpServletResponse response) {
        if (tokenService.rotateToken(refreshToken, response)) {
            return ResponseEntity.noContent().build();

        } else {
            return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body("Refresh token is invalid");

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

    @GetMapping("/get-token-noti")
    public ResponseEntity<String> getTokenNotification() {
        return ResponseEntity.ok(authService.generateTokenForNotification());
    }

}
