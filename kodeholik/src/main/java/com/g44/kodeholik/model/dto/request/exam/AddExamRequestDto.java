package com.g44.kodeholik.model.dto.request.exam;

import java.sql.Timestamp;
import java.util.List;

import com.g44.kodeholik.model.enums.exam.ExamStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddExamRequestDto {
    @NotNull(message = "MSG34")
    @NotBlank(message = "MSG34")
    @Size(min = 10, max = 200, message = "MSG34")
    private String title;

    @NotNull(message = "MSG29")
    @NotBlank(message = "MSG34")
    @Size(min = 10, max = 5000, message = "MSG29")
    private String description;

    @NotNull
    private Timestamp startTime;

    @NotNull
    private Timestamp endTime;

    @NotNull
    @NotEmpty
    private List<String> languageSupports;

    @NotNull
    @NotEmpty
    private List<ExamProblemRequestDto> problemRequests;
}
