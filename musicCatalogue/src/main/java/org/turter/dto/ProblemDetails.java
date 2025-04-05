package org.turter.dto;

public class ProblemDetails {
    private String message;

    public ProblemDetails(Throwable cause) {
        this.message = cause.getMessage();
    }

    public String getMessage() {
        return message;
    }

    public ProblemDetails setMessage(String message) {
        this.message = message;
        return this;
    }
}
