package com.g44.kodeholik.service.auth;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import com.g44.kodeholik.model.dto.request.user.ChangePasswordRequestDto;
import com.g44.kodeholik.model.dto.request.user.LoginRequestDto;
import com.g44.kodeholik.model.entity.user.Users;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    public boolean verify(LoginRequestDto loginRequest);

    public void loginNormal(LoginRequestDto loginRequest, HttpServletResponse response);

    public void loginAdmin(LoginRequestDto loginRequest, HttpServletResponse response);

    public Users checkUsernameExists(String username);

    public void resetPasswordInit(String username);

    public boolean checkValidForgotPasswordToken(String token);

    public void resetPasswordFinish(String token, String password);

    public void logout(HttpServletResponse response);

    public void loginWithGoogle(OAuth2AuthenticationToken oAuth2User, HttpServletResponse response,
            HttpServletRequest request);

    public void changePassword(ChangePasswordRequestDto ChangePasswordRequestDto, HttpServletResponse response);

    public String generateTokenForNotification();

}
