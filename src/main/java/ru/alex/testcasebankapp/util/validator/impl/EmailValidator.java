package ru.alex.testcasebankapp.util.validator.impl;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import ru.alex.testcasebankapp.model.dto.UserDto;
import ru.alex.testcasebankapp.repository.EmailRepository;
import ru.alex.testcasebankapp.util.validator.DataValidator;

import java.util.Optional;

@Component
public class EmailValidator implements DataValidator {

    private EmailRepository emailRepository;

    public EmailValidator(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDto userDto = (UserDto) target;
        if (Optional.ofNullable(userDto.getEmails()).isPresent()) {
            for (var i : userDto.getEmails()) {
                if (Optional.ofNullable(i.getEmail()).isPresent() && emailRepository.findByEmail(i.getEmail()).isPresent()) {
                    errors.rejectValue("emails", "500", "The user with this email already exists");
                    return;
                }
            }
        }


    }
}
