package com.g44.kodeholik.model.dto.response.user;

import java.sql.Timestamp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.g44.kodeholik.model.enums.user.NotificationType;
import com.g44.kodeholik.util.serializer.TimestampSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NotificationResponseDto {
    private Long id;

    private UserResponseDto user;

    private String content;

    private String link;

    @JsonSerialize(using = TimestampSerializer.class)
    private Long date;

    private NotificationType type;
}
