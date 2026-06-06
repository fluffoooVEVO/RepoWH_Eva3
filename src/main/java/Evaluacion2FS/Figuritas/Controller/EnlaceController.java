package Evaluacion2FS.Figuritas.Controller;

import Evaluacion2FS.Figuritas.DTO.EnlaceDTO;
import Evaluacion2FS.Figuritas.Service.EnlaceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// con esto le decimos a spring que este archivo va a recibir las peticiones de internet (postman)
@RestController
@RequestMapping("/api/v1/enlaces")
public class EnlaceController {

    @Autowired
    private EnlaceService enlaceService;

    // trae todos los enlaces
    @GetMapping
    public ResponseEntity<List<EnlaceDTO>> listarEnlaces() {
        List<EnlaceDTO> enlaces = enlaceService.obtenerTodos();
        return new ResponseEntity<>(enlaces, HttpStatus.OK);
    }

    // trae un enlace segun el id que le pases en la ruta
    @GetMapping("/{id}")
    public ResponseEntity<EnlaceDTO> obtenerEnlace(@PathVariable Integer id) {
        EnlaceDTO enlace = enlaceService.buscarPorId(id);
        return new ResponseEntity<>(enlace, HttpStatus.OK);
    }

    // guarda un enlace nuevo revisando que venga con los datos correctos por el @Valid
    @PostMapping
    public ResponseEntity<EnlaceDTO> crearEnlace(@Valid @RequestBody EnlaceDTO enlaceDTO) {
        EnlaceDTO nuevoEnlace = enlaceService.guardarEnlace(enlaceDTO);
        return new ResponseEntity<>(nuevoEnlace, HttpStatus.CREATED);
    }

    // putmapping es para editar algo que ya existe
    @PutMapping("/{id}")
    public ResponseEntity<EnlaceDTO> actualizarEnlace(@PathVariable Integer id, @Valid @RequestBody EnlaceDTO enlaceDTO) {
        EnlaceDTO enlaceActualizado = enlaceService.actualizarEnlace(id, enlaceDTO);
        return new ResponseEntity<>(enlaceActualizado, HttpStatus.OK);
    }

    // elimina el enlace y devuelve un estado de sin contenido
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEnlace(@PathVariable Integer id) {
        enlaceService.eliminarEnlace(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}