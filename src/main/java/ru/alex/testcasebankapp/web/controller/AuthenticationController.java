package ru.alex.testcasebankapp.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alex.testcasebankapp.model.dto.UserDto;
import ru.alex.testcasebankapp.model.group.Login;
import ru.alex.testcasebankapp.model.group.Registration;
import ru.alex.testcasebankapp.model.response.Tokens;
import ru.alex.testcasebankapp.service.LoginService;
import ru.alex.testcasebankapp.service.RegistrationService;


import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
public class AuthenticationController {

    private RegistrationService registrationService;

    private LoginService loginService;

    public AuthenticationController(RegistrationService registrationService,
                                    LoginService loginService) {
        this.registrationService = registrationService;
        this.loginService = loginService;
    }

    @PostMapping(value = "/registration",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)

    @Operation(summary = "Позволяет зарегистрироваться в системе",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class))
            ),
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "user login",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Tokens.class)
                    )
            )
    )
    public ResponseEntity<Tokens> registration(@Validated({Registration.class}) @RequestBody UserDto userDto, BindingResult bindingResult) {
        return ResponseEntity.created(URI.create("/v1/auth/registration"))
                .body(registrationService.registration(userDto, bindingResult));
    }

    @Operation(summary = "Позволяет залогиниться",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class))
            ),
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "user login",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Tokens.class)
                    )
            )
    )
    @PostMapping(value = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Tokens login(@Validated({Login.class}) @RequestBody UserDto userDto, BindingResult bindingResult) {
        return loginService.login(userDto, bindingResult);
    }
}
