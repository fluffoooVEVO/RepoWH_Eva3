package Evaluacion2FS.Figuritas.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Evaluacion2FS.Figuritas.DTO.ImagenDTO;
import Evaluacion2FS.Figuritas.Service.ImagenService;

@RestController
// Sugerencia: Si es un controlador de imágenes, el path debería ser 'imagenes'
@RequestMapping("api/v1/imagenes") 
public class ImagenController {

    @Autowired
    private ImagenService imagenService;

    @GetMapping
    public ResponseEntity<List<ImagenDTO>> getAll() {
        List<ImagenDTO> imagenes = imagenService.obtenerTodasDTO();
        if (imagenes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(imagenes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImagenDTO> getPorId(@PathVariable Integer id) {
        try {
            ImagenDTO imagen = imagenService.obtenerPorIdDTO(id);
            return new ResponseEntity<>(imagen, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Cambiado a NOT_FOUND (404) porque el recurso no existe
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<ImagenDTO> postImagen(@RequestBody ImagenDTO dto) {
        // ERROR CORREGIDO: Eliminamos @PathVariable Integer id porque no existe en la URL del mapping
        ImagenDTO imagen = imagenService.guardarImagenDTO(dto);
        return new ResponseEntity<>(imagen, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImagenDTO> putImagen(@PathVariable Integer id, @RequestBody ImagenDTO dto) {
        try {
            ImagenDTO actualizada = imagenService.actualizarImagenDTO(id, dto);
            return new ResponseEntity<>(actualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteImagen(@PathVariable Integer id) {
        try {
            String mensaje = imagenService.deleteImagen(id);
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}