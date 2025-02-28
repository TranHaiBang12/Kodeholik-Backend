package com.g44.kodeholik.model.dto.response.problem.submission.submit;

import java.sql.Timestamp;

import com.g44.kodeholik.model.dto.response.problem.submission.SubmissionResponseDto;
import com.g44.kodeholik.model.enums.problem.SubmissionStatus;

public class CompileErrorResposneDto extends SubmissionResponseDto {
    private String message;
    private Timestamp createdAt;

    public CompileErrorResposneDto(String message, String code, String languageName, Timestamp createdAt,
            SubmissionStatus status) {
        super(code, languageName, status);
        this.message = message;
        this.createdAt = createdAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getLanguageName() {
        return languageName;
    }

    @Override
    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public SubmissionStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(SubmissionStatus status) {
        this.status = status;
    }

}
