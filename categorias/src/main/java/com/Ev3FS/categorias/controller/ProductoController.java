package com.Ev3FS.categorias.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ev3FS.categorias.DTO.ProductoDTO;
import com.Ev3FS.categorias.service.ProductoService;

import jakarta.validation.Valid;

// @RequestMapping define la ruta base (URL) para todos los endpoints de esta clase
@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    // inyectamos el Service para que el Controller no tenga que hacer logica de negocio, solo recibir y responder peticiones
    @Autowired
    private ProductoService productoService;

    // endpoint de lectura (GET): devuelve un codigo HTTP 200 (OK) junto con la lista completa de productos
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listarProductos() {
        return new ResponseEntity<>(productoService.obtenerTodos(), HttpStatus.OK);
    }

    // endpoint de lectura por ID (GET):captura el numero de la URL (/api/v1/productos/5)
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerProducto(@PathVariable Integer id) {
        return new ResponseEntity<>(productoService.buscarPorId(id), HttpStatus.OK);
    }

    // endpoint de creacion (POST):
    // @Valid ejecuta las validaciones definidas en el DTO (@NotBlank y @NotNull) antes de entrar al metodo
    // @RequestBody toma el JSON que envia el cliente (ej. por Postman) y lo transforma en el objeto ProductoDTO
    @PostMapping
    public ResponseEntity<ProductoDTO> crearProducto(@Valid @RequestBody ProductoDTO dto) {
        // devuelve un codigo HTTP 201 onfirmando que el recurso fue guardado con exito
        return new ResponseEntity<>(productoService.guardarProducto(dto), HttpStatus.CREATED);
    }

    // endpoint de eliminacion (DELETE): se indica el id en la URL para borrar el registro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Integer id) {
        productoService.eliminarProducto(id);
        // devuelve un codigo HTTP 204 indicando que la operacion fue exitosa pero no hay contenido extra que mostrar
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}