package com.Ev3FS.enlaces.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Enlaces y Marcas")
                        .version("1.0")
                        .description("Microservicio encargado de gestionar enlaces de referencia y marcas de figuras."));
    }
}