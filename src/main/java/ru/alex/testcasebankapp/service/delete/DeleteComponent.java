package ru.alex.testcasebankapp.service.delete;

import ru.alex.testcasebankapp.model.user.User;

public interface DeleteComponent {
    void execute(ru.alex.testcasebankapp.model.dto.UserDto updateUserDtoDto, User userDto);
}
