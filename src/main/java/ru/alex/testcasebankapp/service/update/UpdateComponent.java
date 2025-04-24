package ru.alex.testcasebankapp.service.update;

import ru.alex.testcasebankapp.model.user.User;

public interface UpdateComponent {

    void execute(ru.alex.testcasebankapp.model.dto.UserDto updateUserDtoDto, User userDto);
}
