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
import org.springframework.hateoas.Link;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/marcas")
@Tag(name="Marcas", description="CRUD de Marcas con HATEOAS")
public class MarcaController {

    @Autowired
    private MarcaService marcaService;
    @Autowired
    private MarcaModelAssembler assembler;

    @GetMapping
    @Operation(
        summary = "Obtener todas las marcas",
        description = "Muestra todas las marcas existentes o un mensaje con opciones si está vacía."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operación exitosa (lista llena o vacía con enlaces HATEOAS)")})
    public ResponseEntity<EntityModel<?>> listarMarcas() {
        List<MarcaDTO> marcas = marcaService.obtenerTodas();
        Link linkCrear = Link.of("http://localhost:8082/doc/swagger-ui/index.html#/Marcas/crearMarca")
                .withRel("crear-marca");
                
        if (marcas.isEmpty()) {
            EntityModel<?> vacio = EntityModel.of(
                Map.of("mensaje", "No hay marcas en este momento. Puedes crear una nueva."),
                linkCrear
            );
            return ResponseEntity.status(HttpStatus.OK).body(vacio);
        }
        
        Link linkVerUna = Link.of("http://localhost:8082/doc/swagger-ui/index.html#/Marcas/obtenerMarca")
                .withRel("ver-una-marca");
                
        EntityModel<?> conDatos = EntityModel.of(
            Map.of(
                "marcas", marcas.stream().map(assembler::toModel).toList(),
                "mensaje", "Puedes crear una nueva marca o ver una en especifico"
            ),
            linkCrear,
            linkVerUna
        );
        return ResponseEntity.ok(conDatos);
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