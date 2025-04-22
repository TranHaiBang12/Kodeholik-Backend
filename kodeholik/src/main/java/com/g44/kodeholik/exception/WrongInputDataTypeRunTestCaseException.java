package com.g44.kodeholik.exception;

public class WrongInputDataTypeRunTestCaseException extends RuntimeException {
    private String message;

    private String details;

    private String testCaseName;

    private Object testCaseValue;

    public WrongInputDataTypeRunTestCaseException(String message, String details, String testCaseName,
            Object testCaseValue) {
        super(message);
        this.message = message;
        this.details = details;
        this.testCaseName = testCaseName;
        this.testCaseValue = testCaseValue;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public Object getTestCaseValue() {
        return testCaseValue;
    }
}
