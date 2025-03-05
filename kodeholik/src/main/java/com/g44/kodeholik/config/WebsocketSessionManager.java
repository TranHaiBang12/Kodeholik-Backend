package com.g44.kodeholik.config;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class WebsocketSessionManager {
    private final ConcurrentHashMap<String, String> activeUsers = new ConcurrentHashMap<>();

    public synchronized boolean registerSession(String username, String sessionId) {
        String existingSession = activeUsers.get(username);
        if (existingSession != null && !existingSession.equals(sessionId)) {
            return false; // Từ chối kết nối nếu user đã đăng nhập
        }
        activeUsers.put(username, sessionId);
        return true;
    }

    public synchronized void removeSession(String username) {
        activeUsers.remove(username);
    }

    public boolean isUserConnected(String username) {
        return activeUsers.containsKey(username);
    }
}
