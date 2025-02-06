package com.g44.kodeholik.exception;

public class MalformedJwtException extends RuntimeException {
    private String message;

    private String details;

    public MalformedJwtException(String message, String details) {
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
