package ru.alex.testcasebankapp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.alex.testcasebankapp.model.group.Login;
import ru.alex.testcasebankapp.model.group.Registration;


import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "обьект для взаимодействия с пользователем")
public class UserDto {

    @Schema(example = "ivan", description = "Уникальный")
    @NotNull(groups = {Registration.class, Login.class})
    private String login;

    @Schema(example = "StrongP@ssw0rd")
    @NotNull(groups = {Registration.class, Login.class})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;


    @Schema(example = "Ivan ivanich")
    @NotNull(groups = {Registration.class})
    private String fullName;


    @Schema(example = "2024-05-17T18:29:34.123Z")
    @NotNull(groups = {Registration.class})
    private LocalDateTime dataOfBirth;

    @Schema(description = "каждая почта должна быть уникальна")
    @JsonProperty("emails")
    @NotNull(groups = {Registration.class})
    private List<EmailDto> emails;

    @Schema(description = "каждый телефон должна быть уникальна")
    @JsonProperty("phones")
    @NotNull(groups = {Registration.class})
    private List<PhoneDto> phones;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private AccountDto account;
}
