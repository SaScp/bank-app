package ru.alex.testcasebankapp.service;


import ru.alex.testcasebankapp.model.dto.UserDto;
import ru.alex.testcasebankapp.model.user.User;

import java.util.Map;

public interface UserService {
    User findByLogin(String login);
    Map<String, String> save(UserDto userDto);
}
