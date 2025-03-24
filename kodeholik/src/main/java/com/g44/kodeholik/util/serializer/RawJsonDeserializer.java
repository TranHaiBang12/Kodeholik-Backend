package com.g44.kodeholik.util.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RawJsonDeserializer extends JsonDeserializer<Object> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // Parse chuỗi JSON thành object
        String json = p.getText();
        try {
            return objectMapper.readTree(p); // Tự động nhận diện kiểu dữ liệu JSON
        } catch (Exception e) {
            return json.startsWith("\"") && json.endsWith("\"") ? json : "\"" + json + "\"";
        }
    }
}
