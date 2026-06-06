package Evaluacion2FS.Figuritas.Controller;

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

import Evaluacion2FS.Figuritas.DTO.FiguraDTO;
import Evaluacion2FS.Figuritas.Model.Figura;
import Evaluacion2FS.Figuritas.Service.FiguraService;

@RestController
@RequestMapping("/figura")
public class FiguraController {

    @Autowired
    private FiguraService figuraService;

    @GetMapping
    public ResponseEntity<List<FiguraDTO>> obtenerFiguras(){
        List<Figura> figuras = figuraService.obtenerTodos();
        List<FiguraDTO> figurasDTO = figuraService.convertirListaADTO(figuras);
        return ResponseEntity.ok(figurasDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FiguraDTO> obtenerPorId(@PathVariable Integer id_Figura){
        Figura figura = figuraService.obtenerPorId(id_Figura);
        FiguraDTO figuraDTO = figuraService.convertirADTO(figura);
        return ResponseEntity.ok(figuraDTO);
    }

    @PostMapping
    public ResponseEntity<FiguraDTO> crearFigura(@RequestBody FiguraDTO figuraDTO){
        Figura figura = figuraService.convertirAEntidad(figuraDTO);
        Figura saved = figuraService.guardarFigura(figura);
        FiguraDTO savedDTO = figuraService.convertirADTO(saved);
        return ResponseEntity.ok(savedDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FiguraDTO> actualizarFigura(@PathVariable Integer id_Figura, @RequestBody FiguraDTO figuraDTO){
        Figura figura = figuraService.convertirAEntidad(figuraDTO);
        Figura updated = figuraService.actualizarFigura(id_Figura, figura);
        FiguraDTO updatedDTO = figuraService.convertirADTO(updated);
        return ResponseEntity.ok(updatedDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarFigura(@PathVariable Integer id_Figura){
        return ResponseEntity.ok(figuraService.eliminarFigura(id_Figura));
    }

}
