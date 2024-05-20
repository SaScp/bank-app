package ru.alex.testcasebankapp.service;

import org.springframework.validation.BindingResult;
import ru.alex.testcasebankapp.model.dto.UserDto;
import ru.alex.testcasebankapp.model.response.Tokens;


public interface RegistrationService {
    Tokens registration(UserDto userDto, BindingResult bindingResult);
}
