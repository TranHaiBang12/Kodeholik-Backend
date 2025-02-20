package com.g44.kodeholik.model.dto.request.problem.add;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProblemEditorialDto {
    @NotNull(message = "MSG02")
    private EditorialDto editorialDtos;
}
