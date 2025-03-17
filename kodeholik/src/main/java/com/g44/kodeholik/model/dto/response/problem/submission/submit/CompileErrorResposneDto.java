package com.g44.kodeholik.model.dto.response.problem.submission.submit;

import java.sql.Timestamp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.g44.kodeholik.model.dto.response.problem.submission.SubmissionResponseDto;
import com.g44.kodeholik.model.enums.problem.SubmissionStatus;
import com.g44.kodeholik.util.serializer.TimestampSerializer;

public class CompileErrorResposneDto extends SubmissionResponseDto {
    private String message;

    @JsonSerialize(using = TimestampSerializer.class)
    private Long createdAt;

    public CompileErrorResposneDto(String message, String code, String languageName, Timestamp createdAt,
            SubmissionStatus status, Long submissionId) {
        super(submissionId, code, languageName, status);
        this.message = message;
        this.createdAt = createdAt.getTime();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Long submissionId) {
        this.submissionId = submissionId;
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

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
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
