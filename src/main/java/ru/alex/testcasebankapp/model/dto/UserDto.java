package ru.alex.testcasebankapp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.alex.testcasebankapp.model.group.Login;
import ru.alex.testcasebankapp.model.group.Registration;


import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDto {

    @NotNull(groups = {Registration.class, Login.class})
    private String login;

    @NotNull(groups = {Registration.class, Login.class})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(groups = {Registration.class})
    private String fullName;

    @NotNull(groups = {Registration.class})
    private LocalDateTime dataOfBirth;

    @JsonProperty("emails")
    @NotNull(groups = {Registration.class})
    private List<EmailDto> emails;

    @JsonProperty("phones")
    @NotNull(groups = {Registration.class})
    private List<PhoneDto> phones;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private AccountDto account;
}
