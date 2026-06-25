package Figs40K.Figura.client;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.server.ResponseStatusException;

import Figs40K.Figura.DTO.EdicionExternoDTO;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

// Cliente WebClient hacia el microservicio ms-edicion.
@Slf4j
@Component
public class EdicionClient {

    private static final Duration TIMEOUT = Duration.ofSeconds(3);

    private final WebClient webClient;
    private final String edicionBaseUrl;

    public EdicionClient(WebClient.Builder webClientBuilder,
                        @Value("${ms.edicion.url}") String edicionBaseUrl) {
        this.edicionBaseUrl = edicionBaseUrl;

        HttpClient httpClient = HttpClient.create()
                .responseTimeout(TIMEOUT);

        this.webClient = webClientBuilder
                .baseUrl(edicionBaseUrl)
                .clientConnector(new org.springframework.http.client.reactive.ReactorClientHttpConnector(httpClient))
                .build();
    }

    // Expone la URL base de ms-edicion para que otras capas (ej. FiguraService)
    // puedan construir links HATEOAS sin duplicar la propiedad @Value.
    public String getBaseUrl() {
        return edicionBaseUrl;
    }

    // Consulta una edicion por id en ms-edicion.
    // Lanza 404 si no existe alla. Lanza 503 si ms-edicion no responde o esta caido.
    public EdicionExternoDTO obtenerEdicion(Integer idEdicion) {
        // Fail-fast: no tiene sentido llamar por red si el id ya es invalido.
        if (idEdicion == null || idEdicion <= 0) {
            log.warn("Se intento consultar ms-edicion con un id invalido: {}", idEdicion);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El id de edicion debe ser un numero positivo");
        }

        log.info("Consultando edicion con ID {} en ms-edicion", idEdicion);

        try {
            return webClient.get()
                    .uri("/api/v1/edicion/{id}", idEdicion)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, resp -> {
                        log.error("ms-edicion respondio {} para la edicion ID {}", resp.statusCode(), idEdicion);
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "No existe la Edicion con id " + idEdicion + " en ms-edicion"));
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, resp -> {
                        log.error("ms-edicion respondio error interno {} para la edicion ID {}", resp.statusCode(), idEdicion);
                        return Mono.error(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                                "ms-edicion no pudo procesar la solicitud"));
                    })
                    .bodyToMono(EdicionExternoDTO.class)
                    .block();
        } catch (ResponseStatusException ex) {
            // Ya es nuestra propia excepcion (lanzada en los onStatus de arriba): la dejamos pasar tal cual.
            throw ex;
        } catch (WebClientException ex) {
            // Cubre tanto "conexion rechazada" (ms-edicion apagado) como timeout (ms-edicion colgado).
            // WebClientException es la clase base para ambos casos en Spring WebFlux.
            log.error("No se pudo completar la consulta a ms-edicion para la edicion ID {}: {}", idEdicion, ex.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "ms-edicion no esta disponible en este momento");
        }
    }
}