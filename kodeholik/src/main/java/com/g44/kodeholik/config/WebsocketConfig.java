package com.g44.kodeholik.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import com.g44.kodeholik.interceptor.JwtExamHandShakeInterceptor;
import com.g44.kodeholik.interceptor.JwtNotiHandShakeInterceptor;
import com.g44.kodeholik.interceptor.WebsocketInterceptor;
import com.g44.kodeholik.service.token.TokenService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    private final TokenService tokenService;

    private final WebsocketSessionManager websocketSessionManager;

    private final WebsocketInterceptor websocketInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // API WebSocket cho client kết nối

                .addInterceptors(new JwtExamHandShakeInterceptor(tokenService,
                        websocketSessionManager))
                .setAllowedOrigins("*")

                .withSockJS();
        registry.addEndpoint("/ws/notification") // API WebSocket cho client kết nối

                .addInterceptors(new JwtNotiHandShakeInterceptor(tokenService))
                .setAllowedOrigins("*")

                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic/exam", "/error", "/notification"); // Kênh gửi message đến client
        registry.setApplicationDestinationPrefixes("/app"); // Prefix cho request từ client
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(websocketInterceptor); // Thêm interceptor vào hệ thống message broker
    }

}
