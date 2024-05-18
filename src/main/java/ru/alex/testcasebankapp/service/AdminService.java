package ru.alex.testcasebankapp.service;

import org.springframework.validation.BindingResult;
import ru.alex.testcasebankapp.model.dto.UserAdminDto;
import ru.alex.testcasebankapp.model.response.Tokens;

public interface AdminService {
    public Tokens create(UserAdminDto userAdminDto, BindingResult bindingResult);
}
