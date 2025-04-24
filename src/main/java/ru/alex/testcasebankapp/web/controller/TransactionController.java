package ru.alex.testcasebankapp.web.controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.alex.testcasebankapp.model.entity.AmountEntity;
import ru.alex.testcasebankapp.service.TransactionService;

import java.util.List;
import java.util.UUID;

@Tag(name = "Transaction Service", description = "Transaction Service")
@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @Operation(summary = "Перевод денег между счетами",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Возвращает 200 в случае если перевод успешен"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Возвращает 403 если пользователь не авторизован",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Возвращает 400 в случае если у пользователя остался 1 телефон или почта",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PostMapping("/transfer")
    public UUID transfer(Authentication authentication, @RequestBody AmountEntity request) {
        return transactionService.transferMoney(authentication, request);
    }

    @Operation(summary = "Получение транзакции по Id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Возвращает 200 в случае если перевод успешен",

                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = JsonNode.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Возвращает 404 в случае если транзакция не существует",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping("/transaction")
    public JsonNode findById(@RequestParam("transaction-id") String transactionId) {
        return transactionService.getTransactionById(transactionId);
    }
    @Operation(summary = "Просмотр истории транзакций пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Возвращает 200 и историю транзакций, если транзакций нету, то вернет пустой список",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = JsonNode.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Возвращает 403 если пользователь не авторизован",
                            content = @Content(schema = @Schema(hidden = true))
                    )}
    )
    @GetMapping("/transactions")
    public List<JsonNode> getTransaction(Authentication authentication, @RequestParam(value = "card") String params) {
        return transactionService.getUserTransactionByCard(authentication,params);
    }
}
