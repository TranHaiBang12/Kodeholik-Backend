package com.g44.kodeholik.model.dto.request.exam;

import java.sql.Timestamp;

import com.g44.kodeholik.model.enums.exam.ExamStatus;
import com.g44.kodeholik.model.enums.problem.SubmissionStatus;

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
public class FilterExamRequestDto {
    private String title;
    private ExamStatus status;
    private Timestamp start;
    private Timestamp end;

    @NotNull
    @Min(0)
    private int page;
    private Integer size;
    private String sortBy;
    private Boolean ascending;
}
