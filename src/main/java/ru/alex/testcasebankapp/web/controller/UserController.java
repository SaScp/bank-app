package ru.alex.testcasebankapp.web.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alex.testcasebankapp.model.dto.UserDto;
import ru.alex.testcasebankapp.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    private UserService userService;

    private ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable("id") String id) {
        return modelMapper.map(userService.findById(UUID.fromString(id)), UserDto.class);
    }
}
