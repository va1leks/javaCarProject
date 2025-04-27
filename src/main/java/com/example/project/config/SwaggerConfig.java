package com.example.project.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Dealership Application API",
                description = "API для приложения Dealership,"
                        + " позволяющего пользователям покупать и машины.",
                version = "1.0.0",
                contact = @Contact(
                        name = "va1leks",
                        email = "elinevskijegor@gmail.com",
                        url = "https://github.com/va1leks"
                )
        )
)
public class SwaggerConfig {
}
