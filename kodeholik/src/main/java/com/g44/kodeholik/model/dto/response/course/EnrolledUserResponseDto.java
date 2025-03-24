package com.g44.kodeholik.model.dto.response.course;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.g44.kodeholik.model.dto.response.user.UserResponseDto;
import com.g44.kodeholik.util.serializer.TimestampSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EnrolledUserResponseDto {
    private UserResponseDto user;
    @JsonSerialize(using = TimestampSerializer.class)
    private Long enrolledAt;
    private Double progress;
}
