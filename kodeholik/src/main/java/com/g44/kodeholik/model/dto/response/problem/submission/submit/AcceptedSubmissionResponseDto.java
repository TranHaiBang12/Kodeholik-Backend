package com.g44.kodeholik.model.dto.response.problem.submission.submit;

import java.sql.Timestamp;

import com.g44.kodeholik.model.dto.response.problem.submission.SubmissionResponseDto;
import com.g44.kodeholik.model.enums.problem.SubmissionStatus;

public class AcceptedSubmissionResponseDto extends SubmissionResponseDto {
    private String executionTime;
    private double memoryUsage;
    private int noTestcase;
    private Timestamp createdAt;

    public AcceptedSubmissionResponseDto(String executionTime, double memoryUsage, String code, String languageName,
            int noTestcase, Timestamp createdAt, SubmissionStatus status) {
        super(code, languageName, status);
        this.executionTime = executionTime;
        this.memoryUsage = memoryUsage;
        this.noTestcase = noTestcase;
        this.createdAt = createdAt;
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    public double getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public int getNoTestcase() {
        return noTestcase;
    }

    public void setNoTestcase(int noTestcase) {
        this.noTestcase = noTestcase;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
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
