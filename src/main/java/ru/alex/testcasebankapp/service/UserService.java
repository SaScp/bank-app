package ru.alex.testcasebankapp.service;


import ru.alex.testcasebankapp.model.PaginationEntity;
import ru.alex.testcasebankapp.model.SearchEntity;
import ru.alex.testcasebankapp.model.dto.UserDto;
import ru.alex.testcasebankapp.model.user.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UserService {
    User findById(UUID uuid);
    User findByLogin(String login);
    Map<String, String> save(UserDto userDto);
    List<User> searchClient(SearchEntity searchEntity, PaginationEntity paginationEntity);
}
