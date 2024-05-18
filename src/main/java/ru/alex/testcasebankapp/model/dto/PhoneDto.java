package ru.alex.testcasebankapp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PhoneDto {
    private String phone;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String oldPhone;
}
