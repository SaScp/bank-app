package ru.alex.testcasebankapp.service.add;

import ru.alex.testcasebankapp.model.user.User;

public interface AddComponent {
    void execute(ru.alex.testcasebankapp.model.dto.UserDto updateUserDtoDto, User userDto);
}
