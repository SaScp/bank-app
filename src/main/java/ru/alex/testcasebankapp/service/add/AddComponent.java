package ru.alex.testcasebankapp.service.add;

import ru.alex.testcasebankapp.model.dto.UserDto;
import ru.alex.testcasebankapp.model.user.User;

public interface AddComponent {
    void execute(UserDto updateUserDto, User user);
}
