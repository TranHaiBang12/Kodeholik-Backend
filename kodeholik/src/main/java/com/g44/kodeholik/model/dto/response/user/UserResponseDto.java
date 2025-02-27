package com.g44.kodeholik.model.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserResponseDto {

    private Long id;

    private String avatar;

    private String username;
}
