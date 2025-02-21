package com.g44.kodeholik.config;

import org.springframework.stereotype.Component;

import com.g44.kodeholik.message.YamlMessageSource;

import java.util.Locale;

@Component
public class MessageProperties {
    private final YamlMessageSource yamlMessageSource;

    public MessageProperties(YamlMessageSource yamlMessageSource) {
        this.yamlMessageSource = yamlMessageSource;
    }

    public String getMessage(String code) {
        return yamlMessageSource.getMessage(code, null, Locale.getDefault());
    }
}
