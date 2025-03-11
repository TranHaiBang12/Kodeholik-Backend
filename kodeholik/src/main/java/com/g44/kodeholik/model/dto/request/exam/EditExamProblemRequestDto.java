package com.g44.kodeholik.model.dto.request.exam;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EditExamProblemRequestDto {
    @NotNull
    @NotEmpty
    private List<String> languageSupports;

    @NotNull
    @NotEmpty
    private List<ExamProblemRequestDto> problemRequests;
}
