package Figs40K.Figura.controller;

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

import Figs40K.Figura.DTO.FiguraConEdicionDTO;
import Figs40K.Figura.DTO.FiguraDTO;
import Figs40K.Figura.assemblers.FiguraConEdicionModelAssembler;
import Figs40K.Figura.model.Figura;
import Figs40K.Figura.service.FiguraService;

@RestController
@RequestMapping("/api/v1/figura")
public class FiguraController {

    @Autowired
    private FiguraService figuraService;

    @Autowired
    private FiguraConEdicionModelAssembler edicionAssembler;

    @GetMapping
    public ResponseEntity<List<FiguraDTO>> obtenerFiguras(){
        List<Figura> figuras = figuraService.obtenerTodos();
        List<FiguraDTO> figurasDTO = figuraService.convertirListaADTO(figuras);
        return ResponseEntity.ok(figurasDTO);
    }

    @GetMapping("/edicion/{idEdicion}")
    public ResponseEntity<List<FiguraDTO>> obtenerPorEdicion(@PathVariable("idEdicion") Integer idEdicion){
        List<FiguraDTO> figuras = figuraService.obtenerPorEdicion(idEdicion);
        return ResponseEntity.ok(figuras);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FiguraDTO> obtenerPorId(@PathVariable("id") Integer id_Figura){
        Figura figura = figuraService.obtenerPorId(id_Figura);
        FiguraDTO figuraDTO = figuraService.convertirADTO(figura);
        return ResponseEntity.ok(figuraDTO);
    }

    // Demostracion de comunicacion entre microservicios: arma la respuesta consultando ms-edicion via WebClient.
    // El Assembler agrega los links HATEOAS (self hacia este mismo endpoint, edicion hacia ms-edicion).
    @GetMapping("/{id}/con-edicion")
    public ResponseEntity<FiguraConEdicionDTO> obtenerConEdicion(@PathVariable("id") Integer id_Figura){
        FiguraConEdicionDTO dto = figuraService.obtenerFiguraConEdicion(id_Figura);
        return ResponseEntity.ok(edicionAssembler.toModel(dto));
    }

    @PostMapping
    public ResponseEntity<FiguraDTO> crearFigura(@RequestBody FiguraDTO figuraDTO){
        Figura figura = figuraService.convertirAEntidad(figuraDTO);
        Figura saved = figuraService.guardarFigura(figura);
        FiguraDTO savedDTO = figuraService.convertirADTO(saved);
        return ResponseEntity.ok(savedDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FiguraDTO> actualizarFigura(@PathVariable("id") Integer id_Figura, @RequestBody FiguraDTO figuraDTO){
        Figura figura = figuraService.convertirAEntidad(figuraDTO);
        Figura updated = figuraService.actualizarFigura(id_Figura, figura);
        FiguraDTO updatedDTO = figuraService.convertirADTO(updated);
        return ResponseEntity.ok(updatedDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarFigura(@PathVariable("id") Integer id_Figura){
        return ResponseEntity.ok(figuraService.eliminarFigura(id_Figura));
    }
}