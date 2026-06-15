package com.Ev3FS.categorias.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ev3FS.categorias.DTO.CategoriasDTO;
import com.Ev3FS.categorias.service.CategoriasService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/categorias")
@Tag(name="Categorias",description="CRUD relacionado a las categorias")
@ApiResponses({
    @ApiResponse(responseCode="500",description="Error de servidor"),
    @ApiResponse(responseCode="200",description="Operacion exitosa")
})
public class CategoriasController{
    @Autowired
    private CategoriasService categoriasService;

    @GetMapping
    @Operation(summary="Obtener todas las relaciones Producto-Categoria", 
    description="Obtiene todas las relaciones existentes entre productos y categorias, incluyendo su id propia, id de producto e id de categoria")
    @ApiResponse(responseCode="204",description="La lista esta vacia")
    public ResponseEntity<List<CategoriasDTO>> getAll() {
        List<CategoriasDTO> categorias = categoriasService.obtenerTodasDTO();
        if (categorias.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener ", description = "Obtiene el conjunto de categoria-producto basado con  *id*")
    @ApiResponse(responseCode="400",description="Hubo un error de tipeo?",content=@Content)
    @ApiResponse(responseCode="404",description="Conjunto de categoria no encontrada",content=@Content)
    public ResponseEntity<CategoriasDTO>obtenerPorID(@PathVariable Integer id){
        try {
            CategoriasDTO categoria = categoriasService.obtenerPorID(id);
            return new ResponseEntity<>(categoria, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @Operation(summary = "Crear una nueva relacion Producto-Categoria", description = "Crea una nueva relacion entre un producto y una categoria")
    @ApiResponse(responseCode = "201", description = "Relacion creada correctamente", content = @Content)
    @ApiResponse(responseCode = "400", description = "Hubo un error al crear la relacion", content = @Content)
    public ResponseEntity<CategoriasDTO> crearCategorias(@RequestBody CategoriasDTO categoriasDTO) {
        try {
            CategoriasDTO nuevaCategoria = categoriasService.guardarCategoriasDTO(categoriasDTO);
            return new ResponseEntity<>(nuevaCategoria, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una relacion Producto-Categoria", description = "Actualiza los datos de una relacion existente entre producto y categoria")
    @ApiResponse(responseCode = "400", description = "El id ingresado es invalido", content = @Content)
    @ApiResponse(responseCode = "404", description = "No se encontro la relacion a actualizar", content = @Content)
    public ResponseEntity<CategoriasDTO> actualizarCategorias(@PathVariable Integer id, @RequestBody CategoriasDTO categoriasDTO) {
        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            CategoriasDTO categoriaActualizada = categoriasService.actualizarCategoriasDTO(id, categoriasDTO);
            return new ResponseEntity<>(categoriaActualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una relacion Producto-Categoria", description = "Elimina una relacion existente entre producto y categoria")
    @ApiResponse(responseCode = "400", description = "El id ingresado es invalido", content = @Content)
    @ApiResponse(responseCode = "404", description = "No se encontro la relacion a eliminar", content = @Content)
    public ResponseEntity<String> eliminarCategorias(@PathVariable Integer id) {
        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            String mensaje = categoriasService.eliminarCategoriasDTO(id);
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}