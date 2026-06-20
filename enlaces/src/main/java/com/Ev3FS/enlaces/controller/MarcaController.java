package com.Ev3FS.enlaces.controller;

import com.Ev3FS.enlaces.DTO.MarcaDTO;
import com.Ev3FS.enlaces.assemblers.MarcaModelAssembler;
import com.Ev3FS.enlaces.service.MarcaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/marcas")
@Tag(name="Marcas", description="CRUD de Marcas con HATEOAS")
public class MarcaController {

    @Autowired
    private MarcaService marcaService;
    @Autowired
    private MarcaModelAssembler assembler;

    @GetMapping
    @Operation(summary="Obtener todas las marcas")
    public ResponseEntity<?> listarMarcas() {
        List<MarcaDTO> marcas = marcaService.obtenerTodas();
        if(marcas.isEmpty()){ return ResponseEntity.noContent().build(); }
        return ResponseEntity.ok(marcas.stream().map(assembler::toModel).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary="Obtener una marca por ID")
    public ResponseEntity<EntityModel<MarcaDTO>> obtenerMarca(@PathVariable Integer id) {
        return ResponseEntity.ok(assembler.toModel(marcaService.buscarPorId(id)));
    }

    @PostMapping
    @Operation(summary="Crear marca")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Marca Creada")})
    public ResponseEntity<EntityModel<MarcaDTO>> crearMarca(@Valid @RequestBody MarcaDTO marcaDTO) {
        return new ResponseEntity<>(assembler.toModel(marcaService.guardarMarca(marcaDTO)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary="Editar marca")
    public ResponseEntity<EntityModel<MarcaDTO>> actualizarMarca(@PathVariable Integer id, @Valid @RequestBody MarcaDTO marcaDTO) {
        return ResponseEntity.ok(assembler.toModel(marcaService.actualizarMarca(id, marcaDTO)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Eliminar marca")
    public ResponseEntity<Void> eliminarMarca(@PathVariable Integer id) {
        marcaService.eliminarMarca(id);
        return ResponseEntity.noContent().build();
    }
}