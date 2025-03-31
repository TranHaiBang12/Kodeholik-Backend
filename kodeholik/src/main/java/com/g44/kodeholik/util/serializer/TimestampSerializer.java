package com.g44.kodeholik.util.serializer;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.util.Date;

public class TimestampSerializer extends JsonSerializer<Long> {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm");

    @Override
    public void serialize(Long timestamp, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        if (timestamp != null) {
            String formattedDate = sdf.format(new Date(timestamp + 25200000));
            jsonGenerator.writeString(formattedDate);
        } else {
            jsonGenerator.writeNull();
        }
    }

}
