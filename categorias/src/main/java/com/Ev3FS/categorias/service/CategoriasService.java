package com.Ev3FS.categorias.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.Ev3FS.categorias.DTO.CategoriasDTO;
import com.Ev3FS.categorias.model.Categoria;
import com.Ev3FS.categorias.model.Categorias;
import com.Ev3FS.categorias.repository.CategoriaRepository;
import com.Ev3FS.categorias.repository.CategoriasRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoriasService {
    final CategoriaRepository categoriaRepository;
    final CategoriasRepository categoriasRepository;

    CategoriasService(CategoriaRepository categoriaRepository, CategoriasRepository categoriasRepository) {
        this.categoriaRepository = categoriaRepository;
        this.categoriasRepository = categoriasRepository;
    }
    public CategoriasDTO convertirADTO(Categorias categoria){
        CategoriasDTO dto = new CategoriasDTO();
        dto.setId_categorias_producto(categoria.getIdProductoCategoria());
        dto.setId_categorias(categoria.getCategoria().getIdCategoria());
        dto.setId_producto(categoria.getIdProducto());
        return dto;
    }

    public Categorias convertirAEntidad(CategoriasDTO dto) {
        Categorias categoria = new Categorias();
        categoria.setIdProductoCategoria(dto.getId_categorias_producto());
        Categoria cat = categoriaRepository.findById(dto.getId_categorias())
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + dto.getId_categorias()));
        categoria.setCategoria(cat);
        categoria.setIdProducto(dto.getId_producto());
        return categoria;
    }

    public List<CategoriasDTO> obtenerTodasDTO() {
        return categoriasRepository.findAll().stream()
            .map(this::convertirADTO)
            .toList();
    }

    public CategoriasDTO obtenerPorID(Integer id){
        log.info("Buscando ID: {}", id);
        Categorias categoria = categoriasRepository.findById(id)    
            .orElseThrow(() -> {
                log.error("Error: no se encontró la categoría con id {}", id);
                return new RuntimeException("No se encontró el registro con ID: " + id);
            });
        log.info("Categoría encontrada exitosamente");
        return convertirADTO(categoria);
    }

    public CategoriasDTO guardarCategoriasDTO(CategoriasDTO dto) {
        log.info("Recibiendo DTO para guardar");
        Categorias entidadParaGuardar = convertirAEntidad(dto);
        Categorias guardada = categoriasRepository.save(entidadParaGuardar);
        log.info("Categoría guardada con éxito");
        return convertirADTO(guardada);
    }

    public CategoriasDTO actualizarCategoriasDTO(Integer id, CategoriasDTO dto) {
        log.info("Iniciando actualización de ID: {}", id);
        Categorias existente = categoriasRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se puede actualizar. ID " + id + " no encontrado."));
        if (dto.getId_categorias_producto()!= null) {
            Categoria cat = categoriaRepository.findById(dto.getId_categorias())
                .orElseThrow(()->new RuntimeException("Categoría no encontrada con ID: " + dto.getId_categorias()));
            existente.setCategoria(cat);
        }
        // REEMPLAZAR por:
        if (dto.getId_producto() != null) {
            existente.setIdProducto(dto.getId_producto());
        }
        Categorias actualizada = categoriasRepository.save(existente);
        log.info("Categoría ID {} actualizada correctamente", id);
        return convertirADTO(actualizada);
    }

    public String eliminarCategoriasDTO(Integer id) {
        log.info("Intentando eliminar físicamente la categoría con ID: {}", id);
        Categorias categoria = categoriasRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Error: No se puede eliminar. ID {} no encontrado", id);
                return new RuntimeException("No se encontró el registro con ID: " + id);
            });
        String info = "Relación producto-categoría con ID " + id;
        categoriasRepository.delete(categoria);
        log.info("Relación eliminada exitosamente");
        return info + " ha sido eliminada exitosamente";
    }

    public CategoriasDTO obtenerPorIdDTO(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
