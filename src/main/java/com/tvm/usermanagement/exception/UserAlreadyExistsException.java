package com.tvm.usermanagement.exception;

public class UserAlreadyExistsException extends RuntimeException {
    private final String field; // Could be "email" or "username"

    public UserAlreadyExistsException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
