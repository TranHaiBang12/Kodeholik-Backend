package com.g44.kodeholik.model.dto.request.user;

import org.springframework.web.multipart.MultipartFile;

import com.g44.kodeholik.util.validation.image.ValidImage;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EditProfileRequestDto {
    @ValidImage
    private MultipartFile avatar;

    @NotNull(message = "MSG02")
    @NotBlank(message = "MSG02")
    private String username;

    @NotNull(message = "MSG02")
    @NotBlank(message = "MSG02")
    @Size(min = 1, max = 50, message = "MSG49")
    private String fullname;
}
