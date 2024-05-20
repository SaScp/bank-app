package ru.alex.testcasebankapp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class PhoneDto {

    @Schema(example = "+79991112233")
    private String phone;


    @Schema(accessMode = Schema.AccessMode.WRITE_ONLY, example = "+79991112233")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, value = "old_photo")
    private String oldPhone;

    public PhoneDto(String phone) {
        this.phone = phone;
    }
}
