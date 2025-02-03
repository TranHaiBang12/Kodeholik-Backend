package com.g44.kodeholik.model.dto.response.problem.submission;

import java.sql.Timestamp;

import com.g44.kodeholik.model.dto.request.lambda.InputVariable;
import com.g44.kodeholik.model.dto.request.lambda.TestResult;

public class FailedSubmissionResponseDto extends SubmissionResponseDto {
    private int noSuccessTestcase;
    private int noTestcase;
    private TestResult inputWrong;
    private Timestamp createdAt;

    public FailedSubmissionResponseDto(int noSuccessTestcase, int noTestcase, TestResult inputWrong, String code,
            String languageName, Timestamp createdAt) {
        super(code, languageName);
        this.noSuccessTestcase = noSuccessTestcase;
        this.noTestcase = noTestcase;
        this.inputWrong = inputWrong;
        this.createdAt = createdAt;
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
