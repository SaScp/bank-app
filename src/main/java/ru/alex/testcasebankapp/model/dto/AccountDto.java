package ru.alex.testcasebankapp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class AccountDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String card;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double initialDeposit;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double currentBalance;
}
