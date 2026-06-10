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

import com.Ev3FS.categorias.DTO.ImagenDTO;
import com.Ev3FS.categorias.service.ImagenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/imagenes")
@Tag(name = "Imagen", description = "CRUD relacionado al modelo *Imagen*")
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "Operacion exitosa", content = @Content),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
})
public class ImagenController {

    @Autowired
    private ImagenService imagenService;

    @GetMapping
    @Operation(summary = "Obtener todas las imagenes", description = "Muestra todas las imagenes existentes")
    @ApiResponse(responseCode = "204", description = "La lista esta vacia", content = @Content)
    public ResponseEntity<List<ImagenDTO>> getAll() {
        List<ImagenDTO> imagenes = imagenService.obtenerTodasDTO();
        if (imagenes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(imagenes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una imagen", description = "Obtiene una imagen con el parametro *id*")
    @ApiResponse(responseCode = "400", description = "El id ingresado es invalido", content = @Content)
    @ApiResponse(responseCode = "404", description = "Imagen no encontrada", content = @Content)
    public ResponseEntity<ImagenDTO> getPorId(@PathVariable Integer id) {
        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            ImagenDTO imagen = imagenService.obtenerPorIdDTO(id);
            return new ResponseEntity<>(imagen, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @Operation(summary = "Crear una nueva imagen", description = "Crea y guarda una nueva imagen")
    @ApiResponse(responseCode = "201", description = "Imagen creada correctamente", content = @Content)
    @ApiResponse(responseCode = "400", description = "Hubo un error al crear la imagen", content = @Content)
    public ResponseEntity<ImagenDTO> postImagen(@RequestBody ImagenDTO dto) {
        try {
            ImagenDTO imagen = imagenService.guardarImagenDTO(dto);
            return new ResponseEntity<>(imagen, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una imagen", description = "Actualiza los datos de una imagen existente")
    @ApiResponse(responseCode = "400", description = "El id ingresado es invalido", content = @Content)
    @ApiResponse(responseCode = "404", description = "Imagen no encontrada", content = @Content)
    public ResponseEntity<ImagenDTO> putImagen(@PathVariable Integer id, @RequestBody ImagenDTO dto) {
        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            ImagenDTO actualizada = imagenService.actualizarImagenDTO(id, dto);
            return new ResponseEntity<>(actualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una imagen", description = "Elimina una imagen existente")
    @ApiResponse(responseCode = "400", description = "El id ingresado es invalido", content = @Content)
    @ApiResponse(responseCode = "404", description = "Imagen no encontrada", content = @Content)
    public ResponseEntity<String> deleteImagen(@PathVariable Integer id) {
        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            String mensaje = imagenService.deleteImagen(id);
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}