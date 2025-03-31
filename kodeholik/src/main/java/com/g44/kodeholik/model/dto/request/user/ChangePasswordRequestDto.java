package com.g44.kodeholik.model.dto.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChangePasswordRequestDto {
    @NotNull(message = "MSG02")
    private String oldPassword;

    @NotNull(message = "MSG02")
    private String newPassword;

    @NotNull(message = "MSG02")
    private String confirmPassword;
}
