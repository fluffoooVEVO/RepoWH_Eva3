package com.MainProducto.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import DTO.ProductoDTO;
import Exception.ResourceNotFoundException;
import model.Producto;
import repository.ProductoRepository;

// la anotacion @Service indica que esta clase maneja la logica de negocio.
// aqui es donde procesamos los datos antes de que toquen la base de datos o el controlador.
@Service
public class ProductoService {

    // inyeccion de dependencias: traemos los repositorios necesarios para interactuar con la BD.
    @Autowired
    private ProductoRepository productoRepository;

    // metodo auxiliar (privado) para transformar la Entidad (Producto) a DTO (ProductoDTO).
    private ProductoDTO convertirADto(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId_producto(producto.getId_producto());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setFechaCreacion(producto.getFechaCreacion());
        dto.setIdCategoria(producto.getIdCategoria());
        return dto;
    }

    // obtiene todos los productos de la BD, los convierte a DTO uno por uno y los devuelve en una lista
    public List<ProductoDTO> obtenerTodos() {
        List<Producto> listaProductos = productoRepository.findAll();
        List<ProductoDTO> listaDtos = new ArrayList<>();
        for (Producto producto : listaProductos) {
            listaDtos.add(convertirADto(producto));
        }
        return listaDtos;
    }

    // busca un producto especifico. si no lo encuentra en la BD, lanza nuestra excepcion personalizada.
    public ProductoDTO buscarPorId(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("no se encontro producto con el id especificado: " + id));
        return convertirADto(producto);
    }

    // metodo critico: crea un nuevo producto
    public ProductoDTO guardarProducto(ProductoDTO dto) {
        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre(dto.getNombre());
        nuevoProducto.setDescripcion(dto.getDescripcion());
        nuevoProducto.setFechaCreacion(dto.getFechaCreacion());
        nuevoProducto.setIdCategoria(dto.getIdCategoria());

        Producto guardado = productoRepository.save(nuevoProducto);
        return convertirADto(guardado);
    }

    // elimina un producto, pero primero verifica que exista
    public void eliminarProducto(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("no se puede eliminar: no se encontro producto con id " + id));
        productoRepository.delete(producto);
    }
}
