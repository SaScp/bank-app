package ru.alex.testcasebankapp.util.validator.impl;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.alex.testcasebankapp.model.dto.UserDto;
import ru.alex.testcasebankapp.repository.UserRepository;
import ru.alex.testcasebankapp.util.validator.AuthValidator;

@Component
public class UserValidator implements AuthValidator {

    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDto userDto = (UserDto) target;
        if (userRepository.findByLogin(userDto.getLogin()).isPresent()) {
            errors.rejectValue("login", "500", "The user with this login already exists");
            return;
        }
    }
}
