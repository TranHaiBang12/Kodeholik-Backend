package com.g44.kodeholik.model.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChangePasswordRequestDto {
    private String oldPassword;

    private String newPassword;

    private String confirmPassword;
}
