package com.g44.kodeholik.model.dto.response.problem.submission.submit;

import java.sql.Timestamp;

import com.g44.kodeholik.model.dto.response.problem.submission.SubmissionResponseDto;

public class AcceptedSubmissionResponseDto extends SubmissionResponseDto {
    private String executionTime;
    private float memoryUsage;
    private int noTestcase;
    private Timestamp createdAt;

    public AcceptedSubmissionResponseDto(String executionTime, float memoryUsage, String code, String languageName,
            int noTestcase, Timestamp createdAt) {
        super(code, languageName);
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

    public float getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(float memoryUsage) {
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

}
