package com.g44.kodeholik.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "messages")
public class MessageProperties {
    private Map<String, String> messages;

    public Map<String, String> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, String> messages) {
        this.messages = messages;
    }

    public String getMessage(String code) {
        return messages.getOrDefault(code, "Mã lỗi không tồn tại");
    }
}
