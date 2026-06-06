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

import Evaluacion2FS.Figuritas.DTO.CategoriasDTO;
import Evaluacion2FS.Figuritas.Service.CategoriasService;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriasController{
    @Autowired
    private CategoriasService categoriasService;

    @GetMapping
    public ResponseEntity<List<CategoriasDTO>> getAll() {
        List<CategoriasDTO> categorias = categoriasService.obtenerTodasDTO();
        if (categorias.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriasDTO>obtenerPorID(@PathVariable Integer id){
        try {
            CategoriasDTO categoria = categoriasService.obtenerPorID(id);
            return new ResponseEntity<>(categoria, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<CategoriasDTO>crearCategorias(@RequestBody CategoriasDTO categoriasDTO) {
        try {
            CategoriasDTO nuevaCategoria=categoriasService.guardarCategoriasDTO(categoriasDTO);
            return new ResponseEntity<>(nuevaCategoria, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriasDTO> actualizarCategorias(@PathVariable Integer id, @RequestBody CategoriasDTO categoriasDTO) {
        try {
            CategoriasDTO categoriaActualizada = categoriasService.actualizarCategoriasDTO(id, categoriasDTO);
            return new ResponseEntity<>(categoriaActualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCategorias(@PathVariable Integer id) {
        try {
            String mensaje = categoriasService.eliminarCategoriasDTO(id);
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}