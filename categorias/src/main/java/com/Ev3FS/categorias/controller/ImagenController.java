package com.Ev3FS.categorias.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
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
    public ResponseEntity<Object> getAll() {
        List<ImagenDTO> imagenes = imagenService.obtenerTodasDTO();

        Link linkCrear = Link.of("http://localhost:8081/doc/swagger-ui/index.html#/Imagen/postImagen")
            .withRel("crear-imagen");

        if (imagenes.isEmpty()) {
            EntityModel<Object> vacio = EntityModel.of(
                Map.of("mensaje", "No hay imagenes"),
                linkCrear
            );
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(vacio);
        }

        Link linkVerUna = Link.of("http://localhost:8081/doc/swagger-ui/index.html#/Imagen/getPorId")
            .withRel("ver-una-imagen");

        EntityModel<Object> conDatos = EntityModel.of(
            Map.of(
                "imagenes", imagenes,
                "mensaje", "Puedes crear una nueva imagen o ver una en especifico"
            ),
            linkCrear, linkVerUna
        );
        return new ResponseEntity<>(conDatos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una imagen", description = "Obtiene una imagen con el parametro *id*")
    @ApiResponse(responseCode = "400", description = "El id ingresado es invalido", content = @Content)
    @ApiResponse(responseCode = "404", description = "Imagen no encontrada", content = @Content)
    public ResponseEntity<Object> getPorId(@PathVariable Integer id) {
        if (id <= 0) {
            Link link = Link.of("http://localhost:8081/doc/swagger-ui/index.html#/Imagen/getPorId")
                .withRel("reintentar");
            EntityModel<Object> error = EntityModel.of(
                Map.of("mensaje", "Hubo un error a la hora de colocar el id a buscar"),
                link
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        try {
            ImagenDTO imagen = imagenService.obtenerPorIdDTO(id);
            return ResponseEntity.ok(imagen);
        } catch (RuntimeException e) {
            Link linkCrear = Link.of("http://localhost:8081/doc/swagger-ui/index.html#/Imagen/postImagen")
                .withRel("crear-imagen");
            EntityModel<Object> noEncontrado = EntityModel.of(
                Map.of("mensaje", "Imagen no encontrada, quizas quieras crear una con el href."),
                linkCrear
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noEncontrado);
        }
    }

    @PostMapping
    @Operation(summary = "Crear una nueva imagen", description = "Crea y guarda una nueva imagen")
    @ApiResponse(responseCode = "201", description = "Imagen creada correctamente", content = @Content)
    @ApiResponse(responseCode = "400", description = "Hubo un error al crear la imagen", content = @Content)
    public ResponseEntity<Object> postImagen(@RequestBody ImagenDTO dto) {
        try {
            ImagenDTO imagen = imagenService.guardarImagenDTO(dto);
            Link linkEditar = Link.of("http://localhost:8081/doc/swagger-ui/index.html#/Imagen/putImagen")
                .withRel("Por si te has equivocado en algun dato, puedes editarlo con este link");
            EntityModel<ImagenDTO> posibleEdicion = EntityModel.of(imagen, linkEditar);
            return new ResponseEntity<>(posibleEdicion, HttpStatus.CREATED);
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
    public ResponseEntity<Object> deleteImagen(@PathVariable Integer id) {
        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            String mensaje = imagenService.deleteImagen(id);
            Link linkListar = Link.of("http://localhost:8081/doc/swagger-ui/index.html#/Imagen/getAll")
                .withRel("Ver todas las imagenes");
            EntityModel<String> respuesta = EntityModel.of(mensaje, linkListar);
            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}