package Evaluacion2FS.Figuritas.Controller;

import Evaluacion2FS.Figuritas.DTO.EnlacesDTO;
import Evaluacion2FS.Figuritas.Service.EnlacesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// controlador rest para exponer los endpoints de la tabla intermedia
@RestController
@RequestMapping("/api/v1/relacion-enlaces")
public class EnlacesController {

    @Autowired
    private EnlacesService enlacesService;

    // endpoint para listar todas las relaciones
    @GetMapping
    public ResponseEntity<List<EnlacesDTO>> listarEnlaces() {
        List<EnlacesDTO> enlaces = enlacesService.obtenerTodos();
        return new ResponseEntity<>(enlaces, HttpStatus.OK);
    }

    // endpoint para buscar una relacion por id
    @GetMapping("/{id}")
    public ResponseEntity<EnlacesDTO> obtenerEnlace(@PathVariable Integer id) {
        EnlacesDTO enlace = enlacesService.buscarPorId(id);
        return new ResponseEntity<>(enlace, HttpStatus.OK);
    }

    // endpoint para crear una nueva relacion
    @PostMapping
    public ResponseEntity<EnlacesDTO> crearEnlace(@Valid @RequestBody EnlacesDTO enlacesDTO) {
        EnlacesDTO nuevoEnlace = enlacesService.guardarEnlaces(enlacesDTO);
        return new ResponseEntity<>(nuevoEnlace, HttpStatus.CREATED);
    }

    // endpoint para actualizar una relacion existente
    @PutMapping("/{id}")
    public ResponseEntity<EnlacesDTO> actualizarEnlace(@PathVariable Integer id, @Valid @RequestBody EnlacesDTO enlacesDTO) {
        EnlacesDTO enlaceActualizado = enlacesService.actualizarEnlaces(id, enlacesDTO);
        return new ResponseEntity<>(enlaceActualizado, HttpStatus.OK);
    }

    // endpoint para eliminar una relacion
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEnlace(@PathVariable Integer id) {
        enlacesService.eliminarEnlaces(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}