package ru.alex.testcasebankapp.service.delete;

import ru.alex.testcasebankapp.model.dto.UserDto;
import ru.alex.testcasebankapp.model.user.User;

public interface DeleteComponent {
    void execute(UserDto updateUserDto, User user);
}
