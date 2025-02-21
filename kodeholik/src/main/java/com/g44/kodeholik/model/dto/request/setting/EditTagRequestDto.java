package com.g44.kodeholik.model.dto.request.setting;

import com.g44.kodeholik.model.enums.setting.Level;
import com.g44.kodeholik.model.enums.setting.TagType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EditTagRequestDto {
    private Long id;

    private String name;

    private TagType type;

    private Level level;

    public EditTagRequestDto(Long id, String name, TagType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
}
