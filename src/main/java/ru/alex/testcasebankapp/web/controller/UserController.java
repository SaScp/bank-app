package ru.alex.testcasebankapp.web.controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.alex.testcasebankapp.model.entity.AmountEntity;
import ru.alex.testcasebankapp.model.entity.PaginationEntity;
import ru.alex.testcasebankapp.model.entity.SearchEntity;
import ru.alex.testcasebankapp.model.dto.UserDto;
import ru.alex.testcasebankapp.service.TransactionService;
import ru.alex.testcasebankapp.service.UserService;
import ru.alex.testcasebankapp.util.SearchParam;
import ru.alex.testcasebankapp.util.exception.TransactionException;
import ru.alex.testcasebankapp.util.exception.TransactionNotFoundException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/user")
@Tag(name = "User Service", description = "User Service")
@SecurityRequirement(name = "bearerAuth")
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

    @Operation(summary = "Позволяет получить данные пользователя с помощью токена аунтификации",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "получение авторизованного пользователя",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Возвращает 403 если пользователь не авторизован",
                            content = @Content(
                                    schema = @Schema(hidden = true)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Возвращает 404 если пользователь не найден",
                            content = @Content(
                                    schema = @Schema(hidden = true)
                            )
                    )
            }
    )
    @GetMapping("/")
    public UserDto findById(Authentication authentication) {
        return modelMapper.map(userService.findByLogin(authentication.getName()), UserDto.class);
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
                            description = "Возвращает 200 в случае если перевод успешен"
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

    @Operation(summary = "Поиск пользователей",
            parameters = {
                    @Parameter(
                            name = "page-size",
                            description = "параметр для размера вывода всех пользоателей"
                            , required = true,
                            allowEmptyValue = true
                    ),
                    @Parameter(
                            name = "page-number",
                            description = "параметр номера странцы"
                            , required = true,
                            allowEmptyValue = true
                    ),
                    @Parameter(
                            name = "sort-param",
                            description = "параметр сортировки"
                            , required = true,
                            allowEmptyValue = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Возвращает 200 и список пользователей, если пользователей нету, то вернет пустой список",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Возвращает 403 если пользователь не авторизован",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PostMapping(value = "/search")
    public List<UserDto> searchUser(@RequestBody SearchEntity searchEntity,
                                    @Parameter(hidden = true) @SearchParam PaginationEntity paginationEntity) {
        return userService.searchClient(searchEntity, paginationEntity).stream()
                .map(user -> modelMapper.map(user, UserDto.class)).toList();
    }


    @Operation(summary = "Просмотр истории транзакций пользователя",
            responses = {@ApiResponse(
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
    public List<JsonNode> getTransaction(Authentication authentication) {
        return transactionService.getUserTransaction(authentication);
    }

    @Operation(summary = "Обновление почты или телефона",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Возвращает 200 если обновление произошло успешно"
            ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Возвращает 403 если пользователь не авторизован"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Возвращает 400 если почта или телефон уже существуют"
                    )
            }
    )
    @PostMapping("/update")
    public ResponseEntity<Void> update(@RequestBody UserDto userDto,
                                       Authentication authentication,
                                       BindingResult bindingResult) {
        return ResponseEntity
                .status(userService.update(userDto, authentication, bindingResult) ? 200 : 400)
                .build();
    }

    @Operation(summary = "добавляет почту или телефон",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Возвращает 200 и добавляет телефон или почту пользователя"
            ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Возвращает 403 если пользователь не авторизован"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Возвращает 400 если почта или телефон уже существуют"
                    )
            }
    )
    @PostMapping("/add")
    public ResponseEntity<Void> add(@RequestBody UserDto userDto,
                                    Authentication authentication,
                                    BindingResult bindingResult) {
        return ResponseEntity
                .status(userService.add(userDto, authentication, bindingResult) ? 200 : 400)
                .build();
    }

    @Operation(summary = "Удаляет почту или телефон",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Возвращает 200 и удаляет телефон или почту пользователя"
            ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Возвращает 403 если пользователь не авторизован"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Возвращает 400 если у пользователя остался только 1 почта или телефон"
                    )}
    )
    @PostMapping("/delete")
    public ResponseEntity<Void> delete(@RequestBody UserDto userDto,
                                       Authentication authentication,
                                       BindingResult bindingResult) {
        return ResponseEntity
                .status(userService.delete(userDto, authentication, bindingResult) ? 200 : 400)
                .build();
    }
}
