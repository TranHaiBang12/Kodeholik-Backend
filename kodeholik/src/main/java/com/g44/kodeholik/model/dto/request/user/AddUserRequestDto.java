package com.g44.kodeholik.model.dto.request.user;

import com.g44.kodeholik.model.enums.user.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddUserRequestDto {
    private String fullname;

    private String username;

    private String email;

    private String avatar;

    private UserRole role;
}
