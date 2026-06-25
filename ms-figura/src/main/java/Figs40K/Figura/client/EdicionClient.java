package Figs40K.Figura.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import Figs40K.Figura.DTO.EdicionExternoDTO;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

// Cliente WebClient hacia el microservicio ms-edicion.
@Slf4j
@Component
public class EdicionClient {

    private final WebClient webClient;

    public EdicionClient(WebClient.Builder webClientBuilder,
                        @Value("${ms.edicion.url}") String edicionBaseUrl) {
        this.webClient = webClientBuilder.baseUrl(edicionBaseUrl).build();
    }

    // Consulta una edicion por id en ms-edicion. Lanza 404 si no existe alla.
    public EdicionExternoDTO obtenerEdicion(Integer idEdicion) {
        log.info("Consultando edicion con ID {} en ms-edicion", idEdicion);
        return webClient.get()
            .uri("/api/v1/edicion/{id}", idEdicion)
            .retrieve()
            .onStatus((HttpStatusCode status) -> status.value() == 404,
                resp -> {
                    log.error("ms-edicion respondio 404 para la edicion ID {}", idEdicion);
                    return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No existe la Edicion con id " + idEdicion + " en ms-edicion"));
                })
            .bodyToMono(EdicionExternoDTO.class)
            .block();
    }
}
