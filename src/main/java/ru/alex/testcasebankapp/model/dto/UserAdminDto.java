package ru.alex.testcasebankapp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.alex.testcasebankapp.model.group.Login;
import ru.alex.testcasebankapp.model.group.Registration;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserAdminDto {
    @NotNull(groups = {Registration.class})
    private String login;

    @NotNull(groups = {Registration.class})
    private int initBalance;

    @NotNull(groups = {Registration.class})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty("email")
    @NotNull(groups = {Registration.class})
    private String email;

    @JsonProperty("phone")
    @NotNull(groups = {Registration.class})
    private String phone;
}
