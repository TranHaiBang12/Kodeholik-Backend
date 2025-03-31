package com.g44.kodeholik.model.dto.request.problem.search;

import java.sql.Date;

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
public class FilterSubmission {
    private String link;
    private SubmissionStatus status;
    private Date start;
    private Date end;

    @NotNull(message = "MSG02")
    @Min(0)
    private int page;
    private Integer size;
    private String sortBy;
    private Boolean ascending;
}
