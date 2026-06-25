package com.Ev3FS.enlaces.controller;

import com.Ev3FS.enlaces.DTO.EnlacesDTO;
import com.Ev3FS.enlaces.assemblers.EnlacesModelAssembler;
import com.Ev3FS.enlaces.service.EnlacesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.Link;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/relacion-enlaces")
@Tag(name="Relaciones Enlaces", description="CRUD tabla intermedia Enlace-Producto")
public class EnlacesController {

    @Autowired
    private EnlacesService enlacesService;
    @Autowired
    private EnlacesModelAssembler assembler;

    @GetMapping
    @Operation(
        summary = "Listar tabla intermedia",
        description = "Muestra todas las relaciones existentes o un mensaje con opciones si está vacía."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operación exitosa (lista llena o vacía con enlaces HATEOAS)")})
    public ResponseEntity<EntityModel<?>> listarEnlaces() {
        List<EnlacesDTO> relaciones = enlacesService.obtenerTodos();
        Link linkCrear = Link.of("http://localhost:8082/doc/swagger-ui/index.html#/Relaciones%20Enlaces/crearEnlace")
                .withRel("crear-relacion");
                
        if (relaciones.isEmpty()) {
            EntityModel<?> vacio = EntityModel.of(
                Map.of("mensaje", "No hay relaciones en este momento. Puedes crear una nueva."),
                linkCrear
            );
            return ResponseEntity.status(HttpStatus.OK).body(vacio);
        }
        
        Link linkVerUna = Link.of("http://localhost:8082/doc/swagger-ui/index.html#/Relaciones%20Enlaces/obtenerEnlace")
                .withRel("ver-una-relacion");
                
        EntityModel<?> conDatos = EntityModel.of(
            Map.of(
                "relaciones", relaciones.stream().map(assembler::toModel).toList(),
                "mensaje", "Puedes crear una nueva relacion o ver una en especifico"
            ),
            linkCrear,
            linkVerUna
        );
        return ResponseEntity.ok(conDatos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<EnlacesDTO>> obtenerEnlace(@PathVariable Integer id) {
        return ResponseEntity.ok(assembler.toModel(enlacesService.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<EntityModel<EnlacesDTO>> crearEnlace(@Valid @RequestBody EnlacesDTO enlacesDTO) {
        return new ResponseEntity<>(assembler.toModel(enlacesService.guardarEnlaces(enlacesDTO)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<EnlacesDTO>> actualizarEnlace(@PathVariable Integer id, @Valid @RequestBody EnlacesDTO enlacesDTO) {
        return ResponseEntity.ok(assembler.toModel(enlacesService.actualizarEnlaces(id, enlacesDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEnlace(@PathVariable Integer id) {
        enlacesService.eliminarEnlaces(id);
        return ResponseEntity.noContent().build();
    }
}