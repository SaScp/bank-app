package ru.alex.testcasebankapp.web.controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alex.testcasebankapp.model.dto.UserAdminDto;
import ru.alex.testcasebankapp.model.group.Registration;
import ru.alex.testcasebankapp.model.response.Tokens;
import ru.alex.testcasebankapp.service.AdminService;

@RestController
@RequestMapping("/v1/admin")
public class AdminController {

    private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/create")
    public Tokens create(@Validated({Registration.class}) @RequestBody UserAdminDto userAdminDto,
                         BindingResult bindingResult){
        return adminService.create(userAdminDto, bindingResult);
    }
}
