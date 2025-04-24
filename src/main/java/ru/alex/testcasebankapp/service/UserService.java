package ru.alex.testcasebankapp.service;


import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import ru.alex.testcasebankapp.model.dto.UserDto;
import ru.alex.testcasebankapp.model.entity.PaginationEntity;
import ru.alex.testcasebankapp.model.entity.SearchEntity;
import ru.alex.testcasebankapp.model.user.Account;
import ru.alex.testcasebankapp.model.user.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UserService {
    User findById(UUID uuid);

    User findByLogin(String login);

    Map<String, String> save(ru.alex.testcasebankapp.model.dto.UserDto userDto, BindingResult bindingResult);

    List<UserDto> searchClient(SearchEntity searchEntity, PaginationEntity paginationEntity);

    boolean update(ru.alex.testcasebankapp.model.dto.UserDto userDto, Authentication authentication, BindingResult bindingResult);

    boolean add(ru.alex.testcasebankapp.model.dto.UserDto userDto, Authentication authentication, BindingResult bindingResult);

    Account findAccountByCard(Authentication authentication, String card);

    boolean delete(ru.alex.testcasebankapp.model.dto.UserDto userDto, Authentication authentication, BindingResult bindingResult);

}
