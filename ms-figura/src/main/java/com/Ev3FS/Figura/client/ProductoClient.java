package com.Ev3FS.Figura.client;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.server.ResponseStatusException;

import com.Ev3FS.Figura.DTO.ProductoExternoDTO;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

// Cliente WebClient hacia el microservicio Producto (MainProducto).
// Sigue el mismo patron que EdicionClient: timeout, manejo de 4xx/5xx, validacion fail-fast,
// y 503 cuando Producto esta caido o no responde a tiempo.
@Slf4j
@Component
public class ProductoClient {

    private static final Duration TIMEOUT = Duration.ofSeconds(3);

    private final WebClient webClient;
    private final String productoBaseUrl;

    public ProductoClient(WebClient.Builder webClientBuilder,
                           @Value("${ms.producto.url}") String productoBaseUrl) {
        this.productoBaseUrl = productoBaseUrl;

        HttpClient httpClient = HttpClient.create()
                .responseTimeout(TIMEOUT);

        this.webClient = webClientBuilder
                .baseUrl(productoBaseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    // Expone la URL base de Producto para que otras capas (ej. assemblers) puedan
    // construir links HATEOAS sin duplicar la propiedad @Value.
    public String getBaseUrl() {
        return productoBaseUrl;
    }

    // Consulta un producto por id en Producto (MainProducto).
    // Lanza 404 si no existe alla. Lanza 503 si Producto no responde o esta caido.
    public ProductoExternoDTO obtenerProducto(Integer idProducto) {
        if (idProducto == null || idProducto <= 0) {
            log.warn("Se intento consultar Producto con un id invalido: {}", idProducto);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El id de producto debe ser un numero positivo");
        }

        log.info("Consultando producto con ID {} en Producto", idProducto);

        try {
            return webClient.get()
                    .uri("/api/v1/productos/{id}", idProducto)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, resp -> {
                        log.error("Producto respondio {} para el producto ID {}", resp.statusCode(), idProducto);
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "No existe el Producto con id " + idProducto));
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, resp -> {
                        log.error("Producto respondio error interno {} para el producto ID {}", resp.statusCode(), idProducto);
                        return Mono.error(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                                "Producto no pudo procesar la solicitud"));
                    })
                    .bodyToMono(ProductoExternoDTO.class)
                    .block();
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (WebClientException ex) {
            log.error("No se pudo completar la consulta a Producto para el producto ID {}: {}", idProducto, ex.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Producto no esta disponible en este momento");
        }
    }
}
