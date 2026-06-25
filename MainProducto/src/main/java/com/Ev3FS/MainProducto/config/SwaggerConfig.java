package com.Ev3FS.MainProducto.config;

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
                        .title("Figuritas API - MainProducto")
                        .version("1.0")
                        .description("Microservicio central para el catálogo de productos y figuritas"));
    }
}