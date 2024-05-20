package ru.alex.testcasebankapp.web.controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.alex.testcasebankapp.model.dto.UserAdminDto;
import ru.alex.testcasebankapp.model.dto.UserDto;
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

    @Operation(summary = "Позволяет создать пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получение токенов созданного пользователя",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Tokens.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Возвращает 400 если не удалось создать ползьователя"
                    )
            }
    )
    @PostMapping("/create")
    public Tokens create(@Validated({Registration.class}) @RequestBody UserAdminDto userAdminDto,
                         BindingResult bindingResult){
        return adminService.create(userAdminDto, bindingResult);
    }

    @Operation(summary = "Позволяет вывести все транзакции отсартированные по убыванию",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "вывод все транзакции"
                    )
            }
    )
    @GetMapping("/transaction")
    public List<JsonNode> getTransaction() {
        return transactionService.getAllTransactions();
    }
}
