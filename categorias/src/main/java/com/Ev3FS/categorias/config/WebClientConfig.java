package com.Ev3FS.categorias.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${producto.service.url}")
    private String productoServiceUrl;

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient productoWebClient() {
        return WebClient.builder()
                .baseUrl(productoServiceUrl)
                .build();
    }
}