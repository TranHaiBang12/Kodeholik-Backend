package com.g44.kodeholik.util.serializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DateSerializer extends JsonSerializer<Long> {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public void serialize(Long timestamp, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        if (timestamp != null) {
            String formattedDate = sdf.format(new Date(timestamp));
            jsonGenerator.writeString(formattedDate);
        } else {
            jsonGenerator.writeNull();
        }
    }
}
