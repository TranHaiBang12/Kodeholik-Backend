package com.g44.kodeholik.service.auth.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.exception.ForbiddenException;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.exception.UnauthorizedException;
import com.g44.kodeholik.model.dto.request.user.LoginRequestDto;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.token.TokenType;
import com.g44.kodeholik.repository.user.UserRepository;
import com.g44.kodeholik.service.auth.AuthService;
import com.g44.kodeholik.service.email.EmailService;
import com.g44.kodeholik.service.redis.RedisService;
import com.g44.kodeholik.service.token.TokenService;
import com.g44.kodeholik.util.encoder.PasswordEncoder;
import com.g44.kodeholik.util.validation.Validation;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    private final EmailService emailService;

    private final UserRepository userRepository;

    private final UserDetailsService userDetailsService;

    private final RedisService redisService;

    @Value("${spring.jwt.forgot-token.expiry-time}")
    private int forgotTokenExpiryTime;

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
        if (userRepository.isUserNotAllowed(username)) {
            throw new ForbiddenException("This account is not allowed to do this action",
                    "This account is not allowed to do this action");
        }
        String accessToken = tokenService.generateAccessToken(username);
        tokenService.addTokenToCookie(accessToken, response, TokenType.ACCESS);
        String refreshToken = tokenService.generateRefreshToken(username);
        tokenService.addTokenToCookie(refreshToken, response, TokenType.REFRESH);
    }

    @Override
    public Users checkUsernameExists(String username) {
        return userRepository.existsByUsernameOrEmail(username);
    }

    @Override
    public void resetPasswordInit(String username) {
        Users user = checkUsernameExists(username);
        if (user != null) {
            if (userRepository.isUserNotAllowed(username)) {
                throw new ForbiddenException("This account is not allowed to do this action",
                        "This account is not allowed to do this action");
            }
            String token = tokenService.generateForgotPasswordToken(username);
            redisService.saveToken(username, token, forgotTokenExpiryTime, TokenType.FORGOT);
            log.info(token);
            emailService.sendEmailResetPassword(user.getEmail(), "[KODEHOLIK] Reset Password", user.getUsername(),
                    token);
        } else {
            throw new BadRequestException("Username or email not existed", "Username or email not existed");
        }
    }

    @Override
    public boolean checkValidForgotPasswordToken(String token) {
        String username = tokenService.extractUsername(token);
        if (userRepository.isUserNotAllowed(username)) {
            throw new ForbiddenException("This account is not allowed to do this action",
                    "This account is not allowed to do this action");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails != null) {
            String savedToken = redisService.getToken(username, TokenType.FORGOT);
            if (savedToken != null) {
                if (tokenService.validateToken(token, userDetails) &&
                        token.equals(savedToken.trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void resetPasswordFinish(String token, String password) {
        if (checkValidForgotPasswordToken(token)) {
            String username = tokenService.extractUsername(token);
            Users user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new NotFoundException("User not found", "User not found"));
            password = password.trim().replaceAll("\"", "");
            if (Validation.isValidPassword(password)) {
                user.setPassword(PasswordEncoder.encodePassword(password));
            } else {
                throw new BadRequestException(
                        "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one number, and one special character",
                        "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one number, and one special character");
            }
            userRepository.save(user);
            redisService.deleteToken(username, TokenType.FORGOT);
        } else {
            throw new UnauthorizedException("Invalid or expired token", "Invalid or expired token");
        }
    }

}
