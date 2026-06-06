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

import Evaluacion2FS.Figuritas.DTO.FigurasDTO;
import Evaluacion2FS.Figuritas.Service.FigurasService;

@RestController
@RequestMapping("/api/v1/Figuras")
public class FigurasController {

    @Autowired
    private FigurasService figurasService;

    @GetMapping
    public ResponseEntity<List<FigurasDTO>> obtenerFiguras() {
        // Asumiendo que tu service ahora devuelve DTOs para ser consistente
        return ResponseEntity.ok(figurasService.obtenerTodos());
    }

    @PostMapping
    public ResponseEntity<FigurasDTO> crearFiguras(@RequestBody FigurasDTO dto) {
        return ResponseEntity.ok(figurasService.guardarFigura(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FigurasDTO> actualizarFiguras(@PathVariable("id") Integer id, @RequestBody FigurasDTO dto) {
        // Importante: El nombre en @PathVariable debe coincidir con {id}
        return ResponseEntity.ok(figurasService.actualizarFigura(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarFiguras(@PathVariable("id") Integer id) {
        // Corregido el nombre del parámetro para que coincida con el @PathVariable
        return ResponseEntity.ok(figurasService.eliminarFigura(id));
    }
}