package ru.alex.testcasebankapp.web.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.alex.testcasebankapp.model.entity.AmountEntity;
import ru.alex.testcasebankapp.model.entity.PaginationEntity;
import ru.alex.testcasebankapp.model.entity.SearchEntity;
import ru.alex.testcasebankapp.model.dto.UserDto;
import ru.alex.testcasebankapp.service.TransactionService;
import ru.alex.testcasebankapp.service.UserService;
import ru.alex.testcasebankapp.util.SearchParam;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    private UserService userService;

    private TransactionService transactionService;

    private ModelMapper modelMapper;

    public UserController(UserService userService,
                          TransactionService transactionService,
                          ModelMapper modelMapper) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    public UserDto findById(Authentication authentication) {
        return modelMapper.map(userService.findByLogin(authentication.getName()), UserDto.class);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(Authentication authentication, @RequestBody AmountEntity request) {
        return ResponseEntity
                .status(transactionService.transferMoney(authentication, request) ? 200 : 404)
                .build();
    }

    @GetMapping(value = "/search",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDto> searchUser(@RequestBody SearchEntity searchEntity,
                                    @SearchParam PaginationEntity paginationEntity) {
        return userService.searchClient(searchEntity, paginationEntity).stream()
                .map(user -> modelMapper.map(user, UserDto.class)).toList();
    }
}
