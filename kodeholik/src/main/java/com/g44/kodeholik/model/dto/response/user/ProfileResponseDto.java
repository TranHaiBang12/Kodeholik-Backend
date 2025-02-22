package com.g44.kodeholik.model.dto.response.user;

import java.sql.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.g44.kodeholik.model.enums.user.UserRole;
import com.g44.kodeholik.model.enums.user.UserStatus;
import com.g44.kodeholik.util.serializer.DateSerializer;
import com.g44.kodeholik.util.serializer.TimestampSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProfileResponseDto {
    private String avatar;

    private String username;

    private String fullname;

    private String email;

    private UserStatus status;

    private UserRole role;

    @JsonSerialize(using = DateSerializer.class)
    private Long createdDate;
}
