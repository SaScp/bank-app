package ru.alex.testcasebankapp.util.validator.impl;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import ru.alex.testcasebankapp.model.dto.UserDto;
import ru.alex.testcasebankapp.util.validator.DataValidator;

import java.util.Optional;


@Component
public class PasswordValidator implements DataValidator {
    private final static String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";


    @Override
    public boolean supports(Class<?> clazz) {
        return UserDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        final var user = (UserDto) target;

        if (Optional.ofNullable(user.getPassword()).isPresent() && !user.getPassword().matches(passwordRegex)) {
            errors.rejectValue("password", "500", "password is invalid");
        }
    }
}
