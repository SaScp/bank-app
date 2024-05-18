package ru.alex.testcasebankapp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EmailDto {
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String oldEmail;
}
