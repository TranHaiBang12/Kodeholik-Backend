package com.g44.kodeholik.model.dto.request.setting;

import com.g44.kodeholik.model.enums.setting.TagType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FilterTagRequestDto {
    @NotNull(message = "MSG02")
    private String name;

    @NotNull(message = "MSG02")
    private TagType type;

    @Min(0)
    @NotNull(message = "MSG02")
    private int page;

    private Integer size;

    private Boolean ascending;
}
