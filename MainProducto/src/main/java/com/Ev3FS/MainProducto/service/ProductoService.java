package com.Ev3FS.MainProducto.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Ev3FS.MainProducto.DTO.ProductoDTO;
import com.Ev3FS.MainProducto.exception.ResourceNotFoundException;
import com.Ev3FS.MainProducto.repository.ProductoRepository;

import lombok.extern.slf4j.Slf4j;
import model.Producto;

@Slf4j
@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    private ProductoDTO convertirADto(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId_producto(producto.getId_producto());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setFechaCreacion(producto.getFechaCreacion());
        dto.setIdCategoria(producto.getIdCategoria());
        return dto;
    }

    public List<ProductoDTO> obtenerTodos() {
        log.info("Obteniendo todos los productos del catalogo");
        return productoRepository.findAll().stream().map(this::convertirADto).toList();
    }

    public ProductoDTO buscarPorId(Integer id) {
        log.info("Buscando producto con ID: {}", id);
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Producto ID {} no encontrado", id);
                    return new ResourceNotFoundException("No se encontro producto con el id: " + id);
                });
        return convertirADto(producto);
    }

    public ProductoDTO guardarProducto(ProductoDTO dto) {
        log.info("Guardando nuevo producto: {}", dto.getNombre());
        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre(dto.getNombre());
        nuevoProducto.setDescripcion(dto.getDescripcion());
        nuevoProducto.setFechaCreacion(dto.getFechaCreacion() != null ? dto.getFechaCreacion() : LocalDate.now());
        nuevoProducto.setIdCategoria(dto.getIdCategoria());
        Producto guardado = productoRepository.save(nuevoProducto);
        return convertirADto(guardado);
    }

    public ProductoDTO actualizarProducto(Integer id, ProductoDTO dto) {
        log.info("Actualizando producto ID: {}", id);
        Producto existente = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro producto con id " + id));

        existente.setNombre(dto.getNombre());
        existente.setDescripcion(dto.getDescripcion());
        existente.setIdCategoria(dto.getIdCategoria());
        
        return convertirADto(productoRepository.save(existente));
    }

    public void eliminarProducto(Integer id) {
        log.info("Eliminando producto ID: {}", id);
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede eliminar: no se encontro producto con id " + id));
        productoRepository.delete(producto);
    }
}