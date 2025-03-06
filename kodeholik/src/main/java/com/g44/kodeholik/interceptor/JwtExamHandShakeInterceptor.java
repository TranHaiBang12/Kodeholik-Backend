package com.g44.kodeholik.interceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.g44.kodeholik.config.WebsocketSessionManager;
import com.g44.kodeholik.service.token.TokenService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class JwtExamHandShakeInterceptor implements HandshakeInterceptor {

    private TokenService tokenService;

    private WebsocketSessionManager websocketSessionManager;

    public JwtExamHandShakeInterceptor(TokenService tokenService, WebsocketSessionManager websocketSessionManager) {
        this.tokenService = tokenService;
        this.websocketSessionManager = websocketSessionManager;
    }

    @Override
    public void afterHandshake(ServerHttpRequest arg0, ServerHttpResponse arg1, WebSocketHandler arg2,
            Exception arg3) {
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {

        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpServletRequest httpRequest = servletRequest.getServletRequest();
            String token = servletRequest.getServletRequest().getParameter("token"); // Lấy JWT từ query params
            if (token != null && validateToken(token)) {
                String username = tokenService.extractUsername(token);
                String sessionId = UUID.randomUUID().toString();
                attributes.put("username", username);
                attributes.put("token", token);

                if (!websocketSessionManager.registerSession(username, sessionId, "EXAM")) {
                    return false; // Từ chối kết nối nếu user đã đăng nhập
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean validateToken(String token) {
        return tokenService.validateToken(token);
    }

}
