package ru.alex.testcasebankapp.util.validator.impl;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import ru.alex.testcasebankapp.model.dto.UserDto;
import ru.alex.testcasebankapp.repository.PhoneRepository;
import ru.alex.testcasebankapp.util.validator.DataValidator;

import java.util.Optional;

@Component
public class PhoneValidator implements DataValidator {

    private PhoneRepository phoneRepository;

    public PhoneValidator(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDto userDto = (UserDto) target;
        if (Optional.ofNullable(userDto.getPhones()).isPresent()) {
            for (var i : userDto.getPhones()) {
                if (Optional.ofNullable(i.getPhone()).isPresent() &&
                        phoneRepository.findByPhone(i.getPhone()).isPresent()) {
                    errors.rejectValue("phones", "500", "The user with this phone already exists");
                    return;
                }
            }
        }
    }
}
