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
import java.util.List;

@RestController
@RequestMapping("/api/v1/relacion-enlaces")
@Tag(name="Relaciones Enlaces", description="CRUD tabla intermedia Enlace-Producto")
public class EnlacesController {

    @Autowired
    private EnlacesService enlacesService;
    @Autowired
    private EnlacesModelAssembler assembler;

    @GetMapping
    @Operation(summary="Listar tabla intermedia")
    public ResponseEntity<?> listarEnlaces() {
        List<EnlacesDTO> relaciones = enlacesService.obtenerTodos();
        if(relaciones.isEmpty()){ return ResponseEntity.noContent().build(); }
        return ResponseEntity.ok(relaciones.stream().map(assembler::toModel).toList());
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