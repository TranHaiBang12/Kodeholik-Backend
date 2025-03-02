package com.g44.kodeholik.model.dto.response.problem.submission.submit;

import java.sql.Timestamp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.g44.kodeholik.model.dto.request.lambda.InputVariable;
import com.g44.kodeholik.model.dto.request.lambda.TestResult;
import com.g44.kodeholik.model.dto.response.problem.submission.SubmissionResponseDto;
import com.g44.kodeholik.model.enums.problem.SubmissionStatus;
import com.g44.kodeholik.util.serializer.TimestampSerializer;

public class FailedSubmissionResponseDto extends SubmissionResponseDto {
    private int noSuccessTestcase;
    private int noTestcase;
    private TestResult inputWrong;

    @JsonSerialize(using = TimestampSerializer.class)
    private Long createdAt;

    public FailedSubmissionResponseDto(int noSuccessTestcase, int noTestcase, TestResult inputWrong, String code,
            String languageName, Timestamp createdAt, SubmissionStatus status) {
        super(code, languageName, status);
        this.noSuccessTestcase = noSuccessTestcase;
        this.noTestcase = noTestcase;
        this.inputWrong = inputWrong;
        this.createdAt = createdAt.getTime();
    }

    public int getNoSuccessTestcase() {
        return noSuccessTestcase;
    }

    public void setNoSuccessTestcase(int noSuccessTestcase) {
        this.noSuccessTestcase = noSuccessTestcase;
    }

    public int getNoTestcase() {
        return noTestcase;
    }

    public void setNoTestcase(int noTestcase) {
        this.noTestcase = noTestcase;
    }

    public TestResult getInputWrong() {
        return inputWrong;
    }

    public void setInputWrong(TestResult inputWrong) {
        this.inputWrong = inputWrong;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
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

    @Override
    public SubmissionStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(SubmissionStatus status) {
        this.status = status;
    }
}
