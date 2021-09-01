package com.vladveretilnyk.clinic.exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
