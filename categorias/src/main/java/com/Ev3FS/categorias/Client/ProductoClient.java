package com.Ev3FS.categorias.Client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class ProductoClient {

    private final WebClient webClient;

    public ProductoClient(@Qualifier("productoWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public boolean existeProducto(Integer idProducto) {
        try {
            webClient.get()
                    .uri("/api/v1/productos/{id}", idProducto)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return true;
        } catch (WebClientResponseException.NotFound e) {
            return false;
        }
    }
}