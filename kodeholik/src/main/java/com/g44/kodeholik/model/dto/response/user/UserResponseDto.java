package com.g44.kodeholik.model.dto.response.user;

import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserResponseDto {

    private Long id;

    private String avatar;

    private String username;

    private UserRole role;

    public UserResponseDto(Users user) {
        if (user != null) {
            this.id = user.getId();
            this.avatar = user.getAvatar();
            this.username = user.getUsername();
            this.role = user.getRole();
        }
    }
}
