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
import org.springframework.http.MediaType;
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

import java.util.List;

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
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "get data by id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)
                    )
            ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Возвращает 403 если пользователь не авторизован"
                    ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Возвращает 404 если пользователь не найден"
            )}
    )
    @GetMapping("/")
    public UserDto findById(Authentication authentication) {
        return modelMapper.map(userService.findByLogin(authentication.getName()), UserDto.class);
    }

    @Operation(summary = "Перевод денег между счетами",
            parameters = @Parameter(
                    name = "AmountEntity",
                    description = "объект который хранит в себе карту на которую переводят деньги и сумма перевода"
                    , required = true
            ),
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Возвращает 200 в случае если перевод успешен и 400 если неудачен"
            ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Возвращает 403 если пользователь не авторизован"
                    )
            }
    )
    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(Authentication authentication, @RequestBody AmountEntity request) {
        return ResponseEntity
                .status(transactionService.transferMoney(authentication, request) ? 200 : 400)
                .build();
    }

    @Operation(summary = "Перевод денег между счетами",
            hidden = true,
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Возвращает 200 и список пользователей, если пользователей нету, то вернет пустой список",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)
                    )
            ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Возвращает 403 если пользователь не авторизован"
                    )}
    )
    @GetMapping(value = "/search")
    public List<UserDto> searchUser(@RequestBody SearchEntity searchEntity,
                                    @SearchParam PaginationEntity paginationEntity) {
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
                            description = "Возвращает 403 если пользователь не авторизован"
                    )}
    )
    @GetMapping("/transaction")
    public List<JsonNode> getTransaction(Authentication authentication) {
        return transactionService.getUserTransaction(authentication);
    }

    @Operation(summary = "Просмотр истории транзакций пользователя",
            parameters = @Parameter(
                    name = "UserDto",
                    description = "объект который хранит в себе почту и телефон для обновление"
                    , required = true
            ),
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Возвращает 200 и историю транзакций, если транзакций нету, то вернет пустой список"
            ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Возвращает 403 если пользователь не авторизован"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Возвращает 400 если у пользователя остался только 1 почта или телефон"
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
                            description = "Возвращает 400 если у пользователя остался только 1 почта или телефон"
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
            parameters = @Parameter(
                    name = "UserDto",
                    description = "объект который хранит в себе почту и телефон для удаление"
                    , required = true
            ),
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Возвращает 200 и удаляет телефон или почту пользователя",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)
                    )
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
