package ru.alex.testcasebankapp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class AccountDto {

    @Schema(example = "0971093327478357")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String card;

    @Schema(example = "100.0")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double initialDeposit;

    @Schema(example = "100.0")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double currentBalance;
}
