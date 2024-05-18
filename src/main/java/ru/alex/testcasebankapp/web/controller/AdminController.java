package ru.alex.testcasebankapp.web.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.alex.testcasebankapp.model.dto.UserAdminDto;
import ru.alex.testcasebankapp.model.group.Registration;
import ru.alex.testcasebankapp.model.response.Tokens;
import ru.alex.testcasebankapp.service.AdminService;
import ru.alex.testcasebankapp.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/v1/admin")
public class AdminController {

    private AdminService adminService;

    private TransactionService transactionService;

    public AdminController(AdminService adminService, TransactionService transactionService) {
        this.adminService = adminService;
        this.transactionService = transactionService;
    }

    @PostMapping("/create")
    public Tokens create(@Validated({Registration.class}) @RequestBody UserAdminDto userAdminDto,
                         BindingResult bindingResult){
        return adminService.create(userAdminDto, bindingResult);
    }

    @GetMapping("/transaction")
    public List<JsonNode> getTransaction() {
        return transactionService.getAllTransactions();
    }
}
