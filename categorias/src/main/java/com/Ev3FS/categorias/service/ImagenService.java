package com.Ev3FS.categorias.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.Ev3FS.categorias.DTO.ImagenDTO;
import com.Ev3FS.categorias.DTO.ProductoExternoDTO;
import com.Ev3FS.categorias.model.Imagen;
import com.Ev3FS.categorias.repository.ImagenRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImagenService {

    private final ImagenRepository imagenRepository;
    private final WebClient webClient;

    ImagenService(
            ImagenRepository imagenRepository,
            WebClient.Builder webClientBuilder,
            @Value("${producto.service.url}") String productoServiceUrl) {
        this.imagenRepository = imagenRepository;
        this.webClient = webClientBuilder.baseUrl(productoServiceUrl).build();
    }

    public ProductoExternoDTO obtenerDatosDelProducto(Integer idProducto) {
        try {
            return webClient.get()
                    .uri("/{id}", idProducto)
                    .retrieve()
                    .bodyToMono(ProductoExternoDTO.class)
                    .block();
        } catch (Exception e) {
            log.error("Error al obtener el producto con ID {}: {}", idProducto, e.getMessage());
            return null;
        }
    }

    public Imagen convertirAEntidad(ImagenDTO dto) {
        Imagen imagen = new Imagen();
        imagen.setIdImagen(dto.getId());
        imagen.setUrl(dto.getUrl());
        imagen.setOrden(dto.getOrden());
        imagen.setDescripcion(dto.getDescripcion());
        imagen.setIdProducto(dto.getIdProducto());
        return imagen;
    }

    public ImagenDTO convertirADTO(Imagen imagen) {
        ImagenDTO dto = new ImagenDTO();
        dto.setId(imagen.getIdImagen());
        dto.setUrl(imagen.getUrl());
        dto.setOrden(imagen.getOrden());
        dto.setDescripcion(imagen.getDescripcion());
        dto.setIdProducto(imagen.getIdProducto());
        
        if (imagen.getIdProducto() != null) {
            ProductoExternoDTO producto = obtenerDatosDelProducto(imagen.getIdProducto());
            dto.setProducto(producto);
        }
        
        return dto;
    }

    public List<ImagenDTO> obtenerTodasDTO() {
        log.info("Listando todas las imágenes");

        return imagenRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    public ImagenDTO obtenerPorIdDTO(Integer id) {
        log.info("Buscando imagen con ID: {}", id);

        Imagen imagen = imagenRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró la imagen con ID: {}", id);
                    return new RuntimeException("No se encontró la imagen con ID: " + id);
                });

        log.info("Imagen encontrada exitosamente con ID: {}", id);

        return convertirADTO(imagen);
    }

    public ImagenDTO guardarImagenDTO(ImagenDTO dto) {
        log.info("Validando existencia del producto con ID: {}", dto.getIdProducto());
        
        // Validación utilizando WebClient en lugar de ProductoClient
        ProductoExternoDTO productoExistente = obtenerDatosDelProducto(dto.getIdProducto());
        if (productoExistente == null) {
            log.error("No existe un producto con ID: {}", dto.getIdProducto());
            throw new RuntimeException("No existe un producto con ID: " + dto.getIdProducto());
        }

        log.info("Guardando nueva imagen");

        Imagen imagen = convertirAEntidad(dto);
        Imagen imagenGuardada = imagenRepository.save(imagen);

        log.info("Imagen guardada exitosamente con ID: {}", imagenGuardada.getIdImagen());

        return convertirADTO(imagenGuardada);
    }

    public ImagenDTO actualizarImagenDTO(Integer id, ImagenDTO dto) {
        log.info("Actualizando imagen con ID: {}", id);
        Imagen imagenExistente = imagenRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró la imagen con ID: {}", id);
                    return new RuntimeException("No se encontró la imagen con ID: " + id);
                });
                
        imagenExistente.setUrl(dto.getUrl());
        imagenExistente.setOrden(dto.getOrden());
        imagenExistente.setDescripcion(dto.getDescripcion());
        
        if (dto.getIdProducto() != null) {
            imagenExistente.setIdProducto(dto.getIdProducto());
        }
        
        Imagen imagenActualizada = imagenRepository.save(imagenExistente);
        log.info("Imagen actualizada exitosamente con ID: {}", id);
        return convertirADTO(imagenActualizada);
    }

    public String deleteImagen(Integer id) {
        log.info("Eliminando imagen con ID: {}", id);
        Imagen imagen = imagenRepository.findById(id)
            .orElseThrow(() -> {
            log.error("No se encontró la imagen con ID: {}", id);
            return new RuntimeException("No se encontró la imagen con ID: " + id);
            });
        imagenRepository.delete(imagen);
        log.info("Imagen eliminada exitosamente con ID: {}", id);
        return "Imagen eliminada exitosamente con ID: " + id;
    }
}