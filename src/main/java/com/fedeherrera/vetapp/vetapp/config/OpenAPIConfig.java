package com.fedeherrera.vetapp.vetapp.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Vet API",
        version = "1.0",
        description = "API para gestionar veterinarios"
    )
)
public class OpenAPIConfig {

}
