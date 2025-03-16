package com.g44.kodeholik.exception;

public class ExamNotReadyToStartException extends RuntimeException {
    private String message;

    private String details;

    private String startTime;

    public ExamNotReadyToStartException(String message, String details, String startTime) {
        super(message);
        this.message = message;
        this.details = details;
        this.startTime = startTime;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public String getStartTime() {
        return startTime;
    }
}
