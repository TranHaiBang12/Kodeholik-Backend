package com.g44.kodeholik.exception;

public class ForbiddenException extends RuntimeException {
    private String message;

    private String details;

    public ForbiddenException(String message, String details) {
        super(message);
        this.message = message;
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
