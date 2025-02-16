package com.g44.kodeholik.model.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoginRequestDto {
    @NotBlank(message = "MSG02")
    private String username;

    @NotBlank(message = "MSG02")
    private String password;
}
