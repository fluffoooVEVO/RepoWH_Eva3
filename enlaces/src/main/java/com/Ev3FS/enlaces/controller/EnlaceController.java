package com.Ev3FS.enlaces.controller;

import com.Ev3FS.enlaces.DTO.EnlaceDTO;
import com.Ev3FS.enlaces.assemblers.EnlaceModelAssembler;
import com.Ev3FS.enlaces.service.EnlaceService;
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
@RequestMapping("/api/v1/enlaces")
@Tag(name="Enlaces", description="CRUD de Enlaces principales")
public class EnlaceController {

    @Autowired
    private EnlaceService enlaceService;
    @Autowired
    private EnlaceModelAssembler assembler;

    @GetMapping
    @Operation(
        summary = "Listar todos los enlaces",
        description = "Muestra todos los enlaces existentes o un mensaje con opciones si está vacía."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operación exitosa (lista llena o vacía con enlaces HATEOAS)")})
    public ResponseEntity<EntityModel<?>> listarEnlaces() {
        List<EnlaceDTO> enlaces = enlaceService.obtenerTodos();
        Link linkCrear = Link.of("http://localhost:8082/doc/swagger-ui/index.html#/Enlaces/crearEnlace")
                .withRel("crear-enlace");
                
        if (enlaces.isEmpty()) {
            EntityModel<?> vacio = EntityModel.of(
                Map.of("mensaje", "No hay enlaces en este momento. Puedes crear uno nuevo."),
                linkCrear
            );
            return ResponseEntity.status(HttpStatus.OK).body(vacio);
        }
        
        Link linkVerUna = Link.of("http://localhost:8082/doc/swagger-ui/index.html#/Enlaces/obtenerEnlace")
                .withRel("ver-un-enlace");
                
        EntityModel<?> conDatos = EntityModel.of(
            Map.of(
                "enlaces", enlaces.stream().map(assembler::toModel).toList(),
                "mensaje", "Puedes crear un nuevo enlace o ver uno en especifico"
            ),
            linkCrear,
            linkVerUna
        );
        return ResponseEntity.ok(conDatos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<EnlaceDTO>> obtenerEnlace(@PathVariable Integer id) {
        return ResponseEntity.ok(assembler.toModel(enlaceService.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<EntityModel<EnlaceDTO>> crearEnlace(@Valid @RequestBody EnlaceDTO enlaceDTO) {
        return new ResponseEntity<>(assembler.toModel(enlaceService.guardarEnlace(enlaceDTO)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<EnlaceDTO>> actualizarEnlace(@PathVariable Integer id, @Valid @RequestBody EnlaceDTO enlaceDTO) {
        return ResponseEntity.ok(assembler.toModel(enlaceService.actualizarEnlace(id, enlaceDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEnlace(@PathVariable Integer id) {
        enlaceService.eliminarEnlace(id);
        return ResponseEntity.noContent().build();
    }
}