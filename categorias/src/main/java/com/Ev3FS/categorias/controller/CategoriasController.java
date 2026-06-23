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
public class CategoriasController {

    @Autowired
    private CategoriasService categoriasService;

    @GetMapping
    @Operation(summary="Obtener todas las relaciones Producto-Categoria", 
    description="Obtiene todas las relaciones existentes entre productos y categorias, incluyendo su id propia, id de producto e id de categoria")
    @ApiResponse(responseCode="204",description="La lista esta vacia")
    public ResponseEntity<?> getAll() {
        List<CategoriasDTO> categorias = categoriasService.obtenerTodas();

        Link linkCrear = Link.of("http://localhost:8081/doc/swagger-ui/index.html#/Categorias/crearCategorias")
            .withRel("crear-relacion");

        if (categorias.isEmpty()) {
            EntityModel<?> vacio = EntityModel.of(
                Map.of("mensaje", "No hay relaciones producto-categoria"),
                linkCrear
            );
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(vacio);
        }

        Link linkVerUna = Link.of("http://localhost:8081/doc/swagger-ui/index.html#/Categorias/obtenerPorID")
            .withRel("ver-una-relacion");

        EntityModel<?> conDatos = EntityModel.of(
            Map.of(
                "relaciones", categorias,
                "mensaje", "Puedes crear una nueva relacion o ver una en especifico"
            ),
            linkCrear, linkVerUna
        );
        return new ResponseEntity<>(conDatos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener ", description = "Obtiene el conjunto de categoria-producto basado con id")
    @ApiResponse(responseCode="400",description="Hubo un error de tipeo?",content=@Content)
    @ApiResponse(responseCode="404",description="Conjunto de categoria no encontrada",content=@Content)
    public ResponseEntity<?> obtenerPorID(@PathVariable Integer id) {
        try {
            if (id <= 0) {
                Link link = Link.of("http://localhost:8081/doc/swagger-ui/index.html#/Categorias/obtenerPorID")
                    .withRel("reintentar");
                EntityModel<?> error = EntityModel.of(
                    Map.of("mensaje", "Hubo un error a la hora de colocar el id a buscar"),
                    link
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            CategoriasDTO categoria = categoriasService.obtenerPorID(id);
            return ResponseEntity.ok(categoria);
        } catch (RuntimeException e) {
            Link linkCrear = Link.of("http://localhost:8081/doc/swagger-ui/index.html#/Categorias/crearCategorias")
                .withRel("crear-relacion");
            EntityModel<?> noEncontrado = EntityModel.of(
                Map.of("mensaje", "Relacion no encontrada, quizas quieras crear una con el href."),
                linkCrear
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noEncontrado);
        }
    }

    @PostMapping
    @Operation(summary = "Crear una nueva relacion Producto-Categoria", description = "Crea una nueva relacion entre un producto y una categoria")
    @ApiResponse(responseCode = "201", description = "Relacion creada correctamente", content = @Content)
    @ApiResponse(responseCode = "400", description = "Hubo un error al crear la relacion", content = @Content)
    public ResponseEntity<?> crearCategorias(@RequestBody CategoriasDTO categoriasDTO) {
        try {
            CategoriasDTO nuevaCategoria = categoriasService.guardarCategorias(categoriasDTO);
            Link linkEditar = Link.of("http://localhost:8081/doc/swagger-ui/index.html#/Categorias/actualizarCategorias")
                .withRel("Por si te has equivocado en algun dato, puedes editarlo con este link");
            EntityModel<CategoriasDTO> posibleEdicion = EntityModel.of(nuevaCategoria, linkEditar);
            return new ResponseEntity<>(posibleEdicion, HttpStatus.CREATED);
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
            CategoriasDTO categoriaActualizada = categoriasService.actualizarCategorias(id, categoriasDTO);
            return new ResponseEntity<>(categoriaActualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una relacion Producto-Categoria", description = "Elimina una relacion existente entre producto y categoria")
    @ApiResponse(responseCode = "400", description = "El id ingresado es invalido", content = @Content)
    @ApiResponse(responseCode = "404", description = "No se encontro la relacion a eliminar", content = @Content)
    public ResponseEntity<?> eliminarCategorias(@PathVariable Integer id) {
        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            String mensaje = categoriasService.eliminarCategorias(id);
            Link linkListar = Link.of("http://localhost:8081/doc/swagger-ui/index.html#/Categorias/getAll")
                .withRel("Ver todas las relaciones");
            EntityModel<String> respuesta = EntityModel.of(mensaje, linkListar);
            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}