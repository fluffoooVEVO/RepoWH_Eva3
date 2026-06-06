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

import Evaluacion2FS.Figuritas.Model.Edicion;
import Evaluacion2FS.Figuritas.Service.EdicionService;

@RestController
@RequestMapping("/api/v1/edicion")
public class EdicionController {

    @Autowired
    private EdicionService edicionService;

    @GetMapping
    public ResponseEntity<List<Edicion>> obtenerEdiciones(){
        List<Edicion> ediciones=edicionService.obtenerTodos();
        return ResponseEntity.ok(ediciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Edicion> obtenerPorId(@PathVariable Integer id_Edicion){
        return ResponseEntity.ok(edicionService.obtenerPorId(id_Edicion));
    }

    @PostMapping
    public ResponseEntity<Edicion> crearEdicion(@RequestBody Edicion edicion){
        return ResponseEntity.ok(edicionService.guardarEdicion(edicion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Edicion> actualizarEdicion(@PathVariable Integer id_Edicion, @RequestBody Edicion edicion){
        return ResponseEntity.ok(edicionService.actualizarEdicion(id_Edicion, edicion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarEdicion(@PathVariable Integer id_Edicion){
        return ResponseEntity.ok(edicionService.eliminarEdicion(id_Edicion));
    }
    
}