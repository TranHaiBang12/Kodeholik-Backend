package com.g44.kodeholik.message;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.Yaml;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class YamlMessageSource extends AbstractMessageSource {
    private final Map<String, Object> messages;

    public YamlMessageSource() {
        this.messages = loadYaml();
    }

    private Map<String, Object> loadYaml() {
        try {
            InputStream inputStream = new ClassPathResource("message.yml").getInputStream();
            return new Yaml().load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Không thể load message.yml", e);
        }
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        String message = getMessageFromYaml(code);
        return new MessageFormat(message, locale);
    }

    private String getMessageFromYaml(String key) {
        key = "messages." + key;
        String[] keys = key.split("\\.");
        Map<String, Object> temp = messages;
        for (String k : keys) {
            if (temp.get(k) instanceof Map) {
                temp = (Map<String, Object>) temp.get(k);
            } else {
                return temp.getOrDefault(k, "Mã lỗi không tồn tại").toString();
            }
        }
        return key;
    }
}
