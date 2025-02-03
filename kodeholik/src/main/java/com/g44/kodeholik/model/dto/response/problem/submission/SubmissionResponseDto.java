package com.g44.kodeholik.model.dto.response.problem.submission;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public abstract class SubmissionResponseDto {
    protected String code;
    protected String languageName;

    public abstract String getCode();

    public abstract void setCode(String code);

    public abstract String getLanguageName();

    public abstract void setLanguageName(String languageName);
}
