package com.g44.kodeholik.interceptor;

import java.util.Map;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.service.token.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class WebsocketInterceptor implements ChannelInterceptor {

    private final TokenService tokenService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // Lấy token từ session attributes (đã được lưu từ HandshakeInterceptor)
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        String token = sessionAttributes != null ? (String) sessionAttributes.get("token") : null;
        log.info(token);
        if (token == null) {
            throw new IllegalArgumentException("Token không hợp lệ!");
        }

        // Giải mã JWT để lấy username
        String usernameFromToken = tokenService.extractUsername(token);
        log.info(usernameFromToken + " " + accessor.getDestination());
        // Kiểm tra nếu đây là hành động SUBSCRIBE
        if (accessor.getCommand() == StompCommand.SUBSCRIBE) {
            String destination = accessor.getDestination(); // "/notification/{username}"
            if (destination != null && destination.startsWith("/notification/")) {
                String requestedUsername = destination.substring("/notification/".length());

                // Nếu username trong token KHÔNG khớp với username trong topic -> Chặn
                if (!requestedUsername.equals(usernameFromToken)) {
                    throw new IllegalArgumentException("You can not subscribe into other user's topic!");
                }
            }
        }

        return message;
    }
}
