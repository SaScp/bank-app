package ru.alex.testcasebankapp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.alex.testcasebankapp.model.group.Login;
import ru.alex.testcasebankapp.model.group.Registration;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "обьект для создание пользователей из панели админа")
public class UserAdminDto {

    @Schema(example = "ivan", description = "Уникальный")
    @NotNull(groups = {Registration.class})
    private String login;


    @Schema(example = "150.0")
    @NotNull(groups = {Registration.class})
    private int initBalance;

    @Schema(example = "StrongP@ssw0rd")
    @NotNull(groups = {Registration.class})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Schema(example = "31213@gmail.com",  description = "должна быть уникальная почта")
    @JsonProperty("email")
    @NotNull(groups = {Registration.class})
    private String email;

    @Schema(example = "+79992223322",  description = "должен быть уникальный телефон")
    @JsonProperty("phone")
    @NotNull(groups = {Registration.class})
    private String phone;
}
