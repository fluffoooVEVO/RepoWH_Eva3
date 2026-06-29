package com.Ev3FS.MainProducto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import com.Ev3FS.MainProducto.DTO.ProductoDTO;
import com.Ev3FS.MainProducto.assemblers.ProductoModelAssembler;
import com.Ev3FS.MainProducto.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name="Productos", description="CRUD del catalogo principal de productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private ProductoModelAssembler assembler;

    @GetMapping
    @Operation(
        summary = "Listar todos los productos",
        description = "Muestra todos los productos existentes o un mensaje con opciones si está vacía."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operación exitosa (lista llena o vacía con enlaces HATEOAS)")})
    public ResponseEntity<EntityModel<?>> listarProductos() {
        List<ProductoDTO> productos = productoService.obtenerTodos();
        Link linkCrear = Link.of("http://localhost:8080/doc/swagger-ui/index.html#/Productos/crearProducto")
                .withRel("crear-producto");
                
        if (productos.isEmpty()) {
            EntityModel<?> vacio = EntityModel.of(
                Map.of("mensaje", "No hay productos en este momento. Puedes crear uno nuevo."),
                linkCrear
            );
            return ResponseEntity.status(HttpStatus.OK).body(vacio);
        }
        
        Link linkVerUno = Link.of("http://localhost:8080/doc/swagger-ui/index.html#/Productos/obtenerProducto")
                .withRel("ver-un-producto");
                
        EntityModel<?> conDatos = EntityModel.of(
            Map.of(
                "productos", productos.stream().map(assembler::toModel).toList(),
                "mensaje", "Puedes crear un nuevo producto o ver uno en especifico"
            ),
            linkCrear,
            linkVerUno
        );
        return ResponseEntity.ok(conDatos);
    }

    @GetMapping("/{id}")
    @Operation(summary="Obtener un producto por ID")
    public ResponseEntity<EntityModel<ProductoDTO>> obtenerProducto(@PathVariable Integer id) {
        return ResponseEntity.ok(assembler.toModel(productoService.buscarPorId(id)));
    }

    @PostMapping
    @Operation(summary="Crear un nuevo producto")
    public ResponseEntity<EntityModel<ProductoDTO>> crearProducto(@Valid @RequestBody ProductoDTO dto) {
        return new ResponseEntity<>(assembler.toModel(productoService.guardarProducto(dto)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary="Actualizar producto existente")
    public ResponseEntity<EntityModel<ProductoDTO>> actualizarProducto(@PathVariable Integer id, @Valid @RequestBody ProductoDTO dto) {
        return ResponseEntity.ok(assembler.toModel(productoService.actualizarProducto(id, dto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Eliminar producto")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Integer id) {
        productoService.eliminarProducto(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}