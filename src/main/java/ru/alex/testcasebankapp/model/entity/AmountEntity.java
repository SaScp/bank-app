package ru.alex.testcasebankapp.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AmountEntity {

    @JsonProperty("to_card")
    @Schema(example = "0971093327478357")
    private String toCard;

    @Schema(example = "150.0")
    private double amount;
}
