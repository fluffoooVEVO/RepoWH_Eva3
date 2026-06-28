package com.Ev3FS.categorias.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.Ev3FS.categorias.DTO.ImagenDTO;
import com.Ev3FS.categorias.DTO.ProductoExternoDTO;
import com.Ev3FS.categorias.model.Imagen;
import com.Ev3FS.categorias.repository.ImagenRepository;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@Service
public class ImagenService {

    private final ImagenRepository imagenRepository;
    private final WebClient productoWebClient;

    ImagenService(
            ImagenRepository imagenRepository,
            WebClient productoWebClient) {
        this.imagenRepository = imagenRepository;
        this.productoWebClient = productoWebClient;
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
        validarProductoExterno(dto.getIdProducto());

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
            validarProductoExterno(dto.getIdProducto());
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

    private void validarProductoExterno(Integer idProducto) {
        if (idProducto == null) {
            throw new RuntimeException("El ID del producto no puede ser nulo");
        }
        
        log.info("Validando producto ID: {} en microservicio externo", idProducto);
        try {
            ProductoExternoDTO producto = productoWebClient
                    .get()
                    .uri("/api/v1/productos/{id}", idProducto)
                    .retrieve()
                    .bodyToMono(ProductoExternoDTO.class)
                    .block();
            
            if (producto == null ||producto.getId_producto()== null) {
                log.error("El producto con ID {} no existe en el microservicio externo", idProducto);
                throw new RuntimeException("El producto con ID " + idProducto + " no existe en el sistema central");
            }
            
            log.info("Producto ID: {} validado correctamente", idProducto);
            
        } catch (WebClientResponseException.NotFound e) {
            log.error("El producto con ID {} no existe en el microservicio externo", idProducto);
            throw new RuntimeException("El producto con ID " + idProducto + " no existe en el sistema central");
        } catch (Exception e) {
            log.error("Error al comunicarse con el microservicio de productos: {}", e.getMessage());
            throw new RuntimeException("Error de conexion con el microservicio de productos");
        }
    }
}