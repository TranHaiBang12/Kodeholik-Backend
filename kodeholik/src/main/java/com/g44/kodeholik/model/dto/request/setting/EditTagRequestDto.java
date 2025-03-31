package com.g44.kodeholik.model.dto.request.setting;

import com.g44.kodeholik.model.enums.setting.Level;
import com.g44.kodeholik.model.enums.setting.TagType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EditTagRequestDto {
    @NotNull(message = "MSG02")
    @NotBlank(message = "MSG02")
    private String name;

    @NotNull(message = "MSG02")
    private TagType type;

    private Level level;

    public EditTagRequestDto(String name, TagType type) {
        this.name = name;
        this.type = type;
    }
}
