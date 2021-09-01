package com.vladveretilnyk.clinic.handler;

import com.vladveretilnyk.clinic.exception.NoteNotFoundException;
import com.vladveretilnyk.clinic.exception.UserNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = NoteNotFoundException.class)
    public String exception(NoteNotFoundException exception) {
        return "note_not_found";
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = UserNotFoundException.class)
    public String exception(UserNotFoundException exception) {
        return "user_not_found";
    }
}
