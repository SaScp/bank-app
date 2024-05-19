package ru.alex.testcasebankapp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

import static io.swagger.v3.oas.annotations.enums.SecuritySchemeType.APIKEY;

@OpenAPIDefinition(
        info = @Info(
                title = "Bank API",
                description = "Сервис для банковских операций ", version = "1.0.0",
                contact = @Contact(
                        name = "Alex",
                        email = "3048453@gmail.com",
                        url = "https://github.com/SaScp"
                )
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(type = SecuritySchemeType.HTTP, paramName = "Authorization", bearerFormat = "JWT", scheme = "bearer", name = "bearerAuth")
public class OpenApiConfiguration {
}
