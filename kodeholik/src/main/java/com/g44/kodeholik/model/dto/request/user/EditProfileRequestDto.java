package com.g44.kodeholik.model.dto.request.user;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EditProfileRequestDto {
    private MultipartFile avatar;

    @NotNull(message = "MSG02")
    private String username;

    @NotNull(message = "MSG02")
    private String fullname;
}
