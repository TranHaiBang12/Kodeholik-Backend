package com.g44.kodeholik.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.g44.kodeholik.message.YamlMessageSource;

@Configuration
public class MessageConfig {
    @Bean
    public MessageSource messageSource() {
        return new YamlMessageSource();
    }
}
