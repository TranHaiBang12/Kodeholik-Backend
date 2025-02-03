package com.g44.kodeholik.model.dto.response.problem.submission;

import java.sql.Timestamp;

public class CompileErrorResposneDto extends SubmissionResponseDto {
    private String message;
    private Timestamp createdAt;

    public CompileErrorResposneDto(String message, String code, String languageName, Timestamp createdAt) {
        super(code, languageName);
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

}
