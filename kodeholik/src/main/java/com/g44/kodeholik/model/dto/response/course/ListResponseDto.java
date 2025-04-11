package com.g44.kodeholik.model.dto.response.course;

import com.google.auto.value.AutoValue.Builder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ListResponseDto {
    private Long id;

    private String title;
}
