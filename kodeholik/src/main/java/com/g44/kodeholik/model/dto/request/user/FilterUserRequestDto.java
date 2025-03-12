package com.g44.kodeholik.model.dto.request.user;

import java.sql.Date;

import com.g44.kodeholik.model.enums.user.UserRole;
import com.g44.kodeholik.model.enums.user.UserStatus;
import com.google.auto.value.AutoValue.Builder;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FilterUserRequestDto {
    private String text;

    private UserRole role;

    private UserStatus status;

    private Date start;

    private Date end;

    @Min(0)
    @NotNull(message = "MSG02")
    private int page;

    private Integer size;

    private Boolean ascending;
}
