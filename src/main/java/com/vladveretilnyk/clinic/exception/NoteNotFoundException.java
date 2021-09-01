package com.vladveretilnyk.clinic.exception;

public class NoteNotFoundException extends Exception {
    public NoteNotFoundException(String message) {
        super(message);
    }

    public NoteNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
