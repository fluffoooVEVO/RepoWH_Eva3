package Figs40K.Edicion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Figs40K.Edicion.DTO.EdicionDTO;
import Figs40K.Edicion.assemblers.EdicionModelAssembler;
import Figs40K.Edicion.model.Edicion;
import Figs40K.Edicion.service.EdicionService;

@RestController
@RequestMapping("/api/v1/edicion")
public class EdicionController {

    @Autowired
    private EdicionService edicionService;

    @Autowired
    private EdicionModelAssembler edicionAssembler;

    @GetMapping
    public ResponseEntity<List<EdicionDTO>> obtenerEdiciones(){
        List<Edicion> ediciones = edicionService.obtenerTodos();
        List<EdicionDTO> dtos = edicionService.convertirListaADTO(ediciones)
            .stream()
            .map(edicionAssembler::toModel)
            .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EdicionDTO> obtenerPorId(@PathVariable("id") Integer id_Edicion){
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