package com.vladveretilnyk.clinic.validator;

import com.vladveretilnyk.clinic.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        if (user.getUsername().length() < 4 || user.getUsername().length() > 20) {
            errors.rejectValue("username", "size.user.username");
        }

        if (user.getPassword().length() < 4 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "size.user.password");
        }

        if (!user.getConfirmPassword().equals(user.getPassword())) {
            errors.rejectValue("password", "different.user.password");
        }
    }
}