package ru.alex.testcasebankapp.service.update;

import ru.alex.testcasebankapp.model.dto.UserDto;
import ru.alex.testcasebankapp.model.user.User;

public interface UpdateComponent {

    void execute(UserDto updateUserDto, User user);
}
