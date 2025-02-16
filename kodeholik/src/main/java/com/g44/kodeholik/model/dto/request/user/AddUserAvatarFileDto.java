package com.g44.kodeholik.model.dto.request.user;

import org.springframework.web.multipart.MultipartFile;

import com.g44.kodeholik.model.enums.user.UserRole;
import com.g44.kodeholik.util.validation.image.ValidImage;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddUserAvatarFileDto {
    @NotBlank(message = "MSG02")
    @Size(min = 1, max = 50, message = "MSG49")
    private String fullname;

    @NotBlank(message = "MSG02")
    @Size(min = 1, max = 50, message = "MSG48")
    private String username;

    @NotBlank(message = "MSG02")
    @Email(message = "MSG47")
    private String email;

    private String avatar;

    @ValidImage
    private MultipartFile avatarFile;

    private UserRole role;
}
