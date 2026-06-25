package com.Ev3FS.enlaces.service;

import com.Ev3FS.enlaces.DTO.EnlacesDTO;
import com.Ev3FS.enlaces.DTO.ProductoExternoDTO;
import com.Ev3FS.enlaces.Exception.ResourceNotFoundException;
import com.Ev3FS.enlaces.model.Enlace;
import com.Ev3FS.enlaces.model.Enlaces;
import com.Ev3FS.enlaces.repository.EnlaceRepository;
import com.Ev3FS.enlaces.repository.EnlacesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Slf4j
@Service
public class EnlacesService {
    @Autowired
    private EnlacesRepository enlacesRepository;
    @Autowired
    private EnlaceRepository enlaceRepository;
    
    @Autowired
    private WebClient.Builder webClientBuilder;

    private EnlacesDTO convertirADto(Enlaces enlaces) {
        EnlacesDTO dto = new EnlacesDTO();
        dto.setId_enlace_producto(enlaces.getId_enlace_producto());
        dto.setId_enlace(enlaces.getEnlace().getId_enlace());
        dto.setId_producto(enlaces.getId_producto());
        return dto;
    }

    public List<EnlacesDTO> obtenerTodos() {
        log.info("Obteniendo todas las relaciones producto-enlace");
        return enlacesRepository.findAll().stream().map(this::convertirADto).toList();
    }

    public EnlacesDTO buscarPorId(Integer id) {
        log.info("Buscando relacion ID: {}", id);
        Enlaces enlaces = enlacesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro la relacion ID " + id));
        return convertirADto(enlaces);
    }

    public EnlacesDTO guardarEnlaces(EnlacesDTO enlacesDTO) {
        log.info("Creando nueva relacion enlace-producto");
        
        // 1. Validamos que el enlace exista en nuestra propia BD
        Enlace enlace = enlaceRepository.findById(enlacesDTO.getId_enlace())
                .orElseThrow(() -> new ResourceNotFoundException("No existe el enlace ID " + enlacesDTO.getId_enlace()));

        // 2. Validamos que el producto exista en el OTRO microservicio usando WebClient
        validarProductoExterno(enlacesDTO.getId_producto());

        // 3. Si todo esta OK, guardamos
        Enlaces nuevaRelacion = new Enlaces();
        nuevaRelacion.setEnlace(enlace);
        nuevaRelacion.setId_producto(enlacesDTO.getId_producto());

        return convertirADto(enlacesRepository.save(nuevaRelacion));
    }

    public EnlacesDTO actualizarEnlaces(Integer id, EnlacesDTO enlacesDTO) {
        log.info("Actualizando relacion ID: {}", id);
        Enlaces relacionExistente = enlacesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro relacion ID " + id));

        Enlace enlace = enlaceRepository.findById(enlacesDTO.getId_enlace())
                .orElseThrow(() -> new ResourceNotFoundException("No existe el enlace ID " + enlacesDTO.getId_enlace()));

        // Validamos el producto externamente antes de actualizar
        validarProductoExterno(enlacesDTO.getId_producto());

        relacionExistente.setEnlace(enlace);
        relacionExistente.setId_producto(enlacesDTO.getId_producto());

        return convertirADto(enlacesRepository.save(relacionExistente));
    }

    public void eliminarEnlaces(Integer id) {
        log.info("Eliminando relacion ID: {}", id);
        Enlaces enlaces = enlacesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro relacion ID " + id));
        enlacesRepository.delete(enlaces);
    }

    //Metodo Auxiliar para hacer la llamada con WebClient
    private void validarProductoExterno(Integer idProducto) {
        log.info("Llamando al microservicio de Productos para validar el ID: {}", idProducto);
        try {
            // Asumimos que el microservicio de Productos corre en el puerto 8080
            ProductoExternoDTO producto = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8080/api/v1/productos/" + idProducto)
                    .retrieve()
                    .bodyToMono(ProductoExternoDTO.class)
                    .block(); // .block() espera la respuesta asíncrona de forma síncrona
            
            log.info("Producto validado correctamente: {}", producto.getNombre());
            
        } catch (WebClientResponseException.NotFound e) {
            log.error("El producto con ID {} no existe en el microservicio externo", idProducto);
            throw new ResourceNotFoundException("El producto con ID " + idProducto + " no existe en el sistema central");
        } catch (Exception e) {
            log.error("Error al comunicarse con el microservicio de productos: {}", e.getMessage());
            throw new RuntimeException("Error de conexion con el microservicio de productos");
        }
    }
}