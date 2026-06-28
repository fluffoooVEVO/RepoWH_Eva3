package com.Ev3FS.categorias.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.Ev3FS.categorias.DTO.CategoriasDTO;
import com.Ev3FS.categorias.DTO.ProductoExternoDTO;
import com.Ev3FS.categorias.model.Categoria;
import com.Ev3FS.categorias.model.Categorias;
import com.Ev3FS.categorias.repository.CategoriaRepository;
import com.Ev3FS.categorias.repository.CategoriasRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoriasService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CategoriasRepository categoriasRepository;

    @Autowired
    private WebClient productoWebClient;

    public List<CategoriasDTO> obtenerTodas() {
        List<CategoriasDTO> listaDTOs = new ArrayList<>();
        List<Categorias> categoriasReales = categoriasRepository.findAll();
        for (Categorias c : categoriasReales) {
            listaDTOs.add(convertirADTO(c));
        }
        return listaDTOs;
    }

    public CategoriasDTO buscarPorId(Integer id) {
        Categorias c = categoriasRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró el registro con ID: " + id));
        return convertirADTO(c);
    }

    public CategoriasDTO guardarCategorias(CategoriasDTO dto) {
        validarProductoExterno(dto.getId_producto());
        
        Categorias entidadParaGuardar = convertirAEntidad(dto);
        Categorias guardada = categoriasRepository.save(entidadParaGuardar);
        return convertirADTO(guardada);
    }

    public CategoriasDTO actualizarCategorias(Integer id, CategoriasDTO dto) {
        Categorias existente = categoriasRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se puede actualizar. ID " + id + " no encontrado."));
        
        if (dto.getId_categorias() != null) {
            Categoria cat = categoriaRepository.findById(dto.getId_categorias())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + dto.getId_categorias()));
            existente.setCategoria(cat);
        }
        
        if (dto.getId_producto() != null) {
            validarProductoExterno(dto.getId_producto());
            existente.setIdProducto(dto.getId_producto());
        }
        
        Categorias actualizada = categoriasRepository.save(existente);
        return convertirADTO(actualizada);
    }

    public String eliminarCategorias(Integer id) {
        Categorias categoria = categoriasRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró el registro con ID: " + id));
        String info = "Relación producto-categoría con ID " + id;
        categoriasRepository.delete(categoria);
        return info + " ha sido eliminada exitosamente";
    }

    private Categorias convertirAEntidad(CategoriasDTO dto) {
        Categorias categoria = new Categorias();
        categoria.setIdProductoCategoria(dto.getId_categorias_producto());
        Categoria cat = categoriaRepository.findById(dto.getId_categorias())
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + dto.getId_categorias()));
        categoria.setCategoria(cat);
        categoria.setIdProducto(dto.getId_producto());
        return categoria;
    }

    private CategoriasDTO convertirADTO(Categorias categoria) {
        CategoriasDTO dto = new CategoriasDTO();
        dto.setId_categorias_producto(categoria.getIdProductoCategoria());
        dto.setId_categorias(categoria.getCategoria().getIdCategoria());
        dto.setId_producto(categoria.getIdProducto());
        return dto;
    }

    private void validarProductoExterno(Integer idProducto) {
        if (idProducto == null) {
            throw new RuntimeException("El ID del producto no puede ser nulo");
        }
        log.info("Validando producto ID: {} en microservicio ", idProducto);
        try {
            ProductoExternoDTO producto = productoWebClient
                    .get()
                    .uri("/api/v1/productos/{id}", idProducto)
                    .retrieve()
                    .bodyToMono(ProductoExternoDTO.class)
                    .block();
            if (producto == null || producto.getId_producto() == null) {
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