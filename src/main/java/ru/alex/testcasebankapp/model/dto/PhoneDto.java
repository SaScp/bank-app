package ru.alex.testcasebankapp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PhoneDto {

    @Schema(example = "+79991112233")
    private String phone;


    @Schema(accessMode = Schema.AccessMode.WRITE_ONLY, example = "+79991112233")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String oldPhone;
}
