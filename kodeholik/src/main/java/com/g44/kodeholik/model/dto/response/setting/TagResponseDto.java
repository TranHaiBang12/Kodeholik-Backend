package com.g44.kodeholik.model.dto.response.setting;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.g44.kodeholik.model.dto.response.user.UserResponseDto;
import com.g44.kodeholik.model.enums.setting.Level;
import com.g44.kodeholik.model.enums.setting.TagType;
import com.g44.kodeholik.util.serializer.TimestampSerializer;
import com.google.auto.value.AutoValue.Builder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TagResponseDto {
    private Long id;

    private String name;

    private TagType type;

    private Level level;

    @JsonSerialize(using = TimestampSerializer.class)
    private Long createdAt;

    private UserResponseDto createdBy;

}
