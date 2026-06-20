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
import java.util.List;

@RestController
@RequestMapping("/api/v1/enlaces")
@Tag(name="Enlaces", description="CRUD de Enlaces principales")
public class EnlaceController {

    @Autowired
    private EnlaceService enlaceService;
    @Autowired
    private EnlaceModelAssembler assembler;

    @GetMapping
    @Operation(summary="Listar todos los enlaces")
    public ResponseEntity<?> listarEnlaces() {
        List<EnlaceDTO> enlaces = enlaceService.obtenerTodos();
        if(enlaces.isEmpty()){ return ResponseEntity.noContent().build(); }
        return ResponseEntity.ok(enlaces.stream().map(assembler::toModel).toList());
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