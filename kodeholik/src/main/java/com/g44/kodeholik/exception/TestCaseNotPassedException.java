package com.g44.kodeholik.exception;

import com.g44.kodeholik.model.dto.response.problem.submission.SubmissionResponseDto;

public class TestCaseNotPassedException extends RuntimeException {
    private String message;

    private SubmissionResponseDto details;

    public TestCaseNotPassedException(String message, SubmissionResponseDto details) {
        super(message);
        this.message = message;
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public SubmissionResponseDto getDetails() {
        return details;
    }
}
