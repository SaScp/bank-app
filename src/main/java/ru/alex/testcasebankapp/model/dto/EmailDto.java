package ru.alex.testcasebankapp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class EmailDto {

    @Email
    @Schema(example = "31213@gmail.com")
    private String email;

    @Email
    @Schema(accessMode = Schema.AccessMode.AUTO ,example = "31213@gmail.com")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, value = "old_email")
    private String oldEmail;

    public EmailDto(String email) {
        this.email = email;
    }

}
