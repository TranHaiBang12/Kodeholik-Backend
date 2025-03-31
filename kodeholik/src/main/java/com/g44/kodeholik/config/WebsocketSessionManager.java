package com.g44.kodeholik.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class WebsocketSessionManager {
    private final ConcurrentHashMap<String, Map<String, String>> activeUsers = new ConcurrentHashMap<>();

    public synchronized boolean registerSession(String username, String sessionId, String channel) {
        activeUsers.putIfAbsent(channel, new ConcurrentHashMap<>());
        Map<String, String> userSessions = activeUsers.get(channel);

        // String existingSession = userSessions.get(username);
        // log.info(channel);

        // // Nếu user đã có session trong kênh "EXAM" và sessionId khác -> Từ chối kết
        // nối
        // if ("EXAM".equals(channel) && existingSession != null &&
        // !existingSession.equals(sessionId)) {
        // return false; // Không cho phép user kết nối nhiều lần vào kênh "EXAM"
        // }

        // Lưu session mới
        userSessions.put(username, sessionId);
        return true;
    }

    public synchronized void removeSession(String username, String channel) {
        if (activeUsers.containsKey(channel)) {
            activeUsers.get(channel).remove(username);
        }
    }

    public boolean isUserConnected(String username, String channel) {
        return activeUsers.containsKey(channel) && activeUsers.get(channel).containsKey(username);
    }
}
