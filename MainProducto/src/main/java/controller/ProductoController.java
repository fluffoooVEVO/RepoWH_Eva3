package controller;

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

import DTO.ProductoDTO;
import jakarta.validation.Valid;
import service.ProductoService;

// @RequestMapping define la ruta base (URL) para todos los endpoints de esta clase
@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    // inyectamos el Service para que el Controller no tenga que hacer logica de negocio
    @Autowired
    private ProductoService productoService;

    // endpoint de lectura (GET): devuelve un codigo HTTP 200 (OK) junto con la lista completa de productos
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listarProductos() {
        return new ResponseEntity<>(productoService.obtenerTodos(), HttpStatus.OK);
    }

    // endpoint de lectura por ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerProducto(@PathVariable Integer id) {
        return new ResponseEntity<>(productoService.buscarPorId(id), HttpStatus.OK);
    }

    // endpoint de creacion (POST)
    @PostMapping
    public ResponseEntity<ProductoDTO> crearProducto(@Valid @RequestBody ProductoDTO dto) {
        return new ResponseEntity<>(productoService.guardarProducto(dto), HttpStatus.CREATED);
    }

    // endpoint de eliminacion (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Integer id) {
        productoService.eliminarProducto(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
