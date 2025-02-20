package com.g44.kodeholik.model.dto.response.user;

import java.sql.Date;

import com.g44.kodeholik.model.enums.user.UserRole;
import com.g44.kodeholik.model.enums.user.UserStatus;

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

    private String password;

    private String email;

    private UserStatus status;

    private UserRole role;

    private Date createdDate;
}
