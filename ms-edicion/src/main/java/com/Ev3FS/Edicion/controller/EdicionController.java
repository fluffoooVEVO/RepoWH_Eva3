package com.Ev3FS.Edicion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ev3FS.Edicion.DTO.EdicionDTO;
import com.Ev3FS.Edicion.assemblers.EdicionModelAssembler;
import com.Ev3FS.Edicion.model.Edicion;
import com.Ev3FS.Edicion.service.EdicionService;

@RestController
@RequestMapping("/api/v1/edicion")
public class EdicionController {

    @Autowired
    private EdicionService edicionService;

    @Autowired
    private EdicionModelAssembler edicionAssembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<EdicionDTO>>> obtenerEdiciones(){
        List<Edicion> ediciones = edicionService.obtenerTodos();
        List<EdicionDTO> dtos = edicionService.convertirListaADTO(ediciones);
        List<EntityModel<EdicionDTO>> modelos = dtos.stream()
            .map(edicionAssembler::toModel)
            .toList();
        return ResponseEntity.ok(CollectionModel.of(modelos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<EdicionDTO>> obtenerPorId(@PathVariable("id") Integer id_Edicion){
        Edicion edicion = edicionService.obtenerPorId(id_Edicion);
        EdicionDTO dto = edicionService.convertirADTO(edicion);
        return ResponseEntity.ok(edicionAssembler.toModel(dto));
    }

    @PostMapping
    public ResponseEntity<Edicion> crearEdicion(@RequestBody Edicion edicion){
        return ResponseEntity.ok(edicionService.guardarEdicion(edicion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Edicion> actualizarEdicion(@PathVariable("id") Integer id_Edicion, @RequestBody Edicion edicion){
        return ResponseEntity.ok(edicionService.actualizarEdicion(id_Edicion, edicion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarEdicion(@PathVariable("id") Integer id_Edicion){
        return ResponseEntity.ok(edicionService.eliminarEdicion(id_Edicion));
    }
}
