package com.g44.kodeholik.service.auth;

import com.g44.kodeholik.model.dto.request.user.LoginRequestDto;
import com.g44.kodeholik.model.entity.user.Users;

import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    public boolean verify(LoginRequestDto loginRequest);

    public void login(LoginRequestDto loginRequest, HttpServletResponse response);

    public Users checkUsernameExists(String username);

    public void resetPasswordInit(String username);

    public boolean checkValidForgotPasswordToken(String token);

    public void resetPasswordFinish(String token, String password);
}
