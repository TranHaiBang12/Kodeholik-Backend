package com.g44.kodeholik.service.auth;

import com.g44.kodeholik.model.dto.request.user.LoginRequestDto;

import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    public boolean verify(LoginRequestDto loginRequest);

    public void login(LoginRequestDto loginRequest, HttpServletResponse response);
}
