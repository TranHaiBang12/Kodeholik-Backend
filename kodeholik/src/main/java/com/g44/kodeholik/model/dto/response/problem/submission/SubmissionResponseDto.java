package com.g44.kodeholik.model.dto.response.problem.submission;

import com.g44.kodeholik.model.enums.problem.SubmissionStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public abstract class SubmissionResponseDto {
    protected Long submissionId;
    protected String code;
    protected String languageName;
    protected SubmissionStatus status;

    public abstract String getCode();

    public abstract void setCode(String code);

    public abstract String getLanguageName();

    public abstract void setLanguageName(String languageName);

    public abstract SubmissionStatus getStatus();

    public abstract void setStatus(SubmissionStatus status);
}
