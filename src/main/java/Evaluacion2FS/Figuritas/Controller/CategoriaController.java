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

import Evaluacion2FS.Figuritas.DTO.CategoriaDTO;
import Evaluacion2FS.Figuritas.Service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/categoria")
@Tag(name="Categoria",description="CRUD relacionado al modelo *Categoria*")
@ApiResponses({
    @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
    @ApiResponse(responseCode = "200",description = "Operacion exitosa")
})
//PORQUE ESTA AL PRINCIPIO FAMILIA? porque cada vez que se quiera ejecutar este controller tendra que pasar por todos los api response puse los mas basicos 
//si quieren agregar uno perso haganlo en el metodo OJO SI METEN TODOS SE MUESTRAN TODOS Y QUEDA INEXACTO
public class CategoriaController {
    
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    @Operation(summary="Obtener todas las categorias",description="Muestra todas las categorias existentes")
    @ApiResponse(responseCode="204",description="La lista esta vacia")
    public ResponseEntity<List<CategoriaDTO>> getAll() {
        List<CategoriaDTO> categorias = categoriaService.obtenerTodasDTO();
        if (categorias.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una categoria", description = "Obtiene una categoria con el parametro *id*")
    @ApiResponse(responseCode="400",description="Hubo un error *Tipeo quizas*?")
    @ApiResponse(responseCode="404",description="Categoria no encontrada")
    public ResponseEntity<CategoriaDTO> buscarPorId(@PathVariable Integer id) {
        try {
        if (id<=0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
            CategoriaDTO categoria = categoriaService.obtenerPorID(id);
            return new ResponseEntity<>(categoria, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/categorias-true")
    @Operation(summary="Obtener categorias true",description="obtiene una lista con todas las categorias las cuales esten *Activas*")
    @ApiResponse(responseCode="204",description="Lista vacia")
    public ResponseEntity<List<CategoriaDTO>> getStatusTrue() {
        try {
            List<CategoriaDTO> categorias = categoriaService.obtenerStatusTrueDTO();
            if (categorias.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(categorias, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/categorias-false")
    @Operation(summary="Obtener categorias false",description="Obtiene todas las categorias que esten *Inactivas*")
    @ApiResponse(responseCode="204",description="Lista vacia")
    public ResponseEntity<List<CategoriaDTO>> getStatusFalse() {
        try {
            List<CategoriaDTO> categorias = categoriaService.obtenerStatusFalseDTO();
            return new ResponseEntity<>(categorias, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping
    @Operation(summary="Crear una nueva categoria")
    @ApiResponse(responseCode="201",description="Categoria creada :D")
    public ResponseEntity<CategoriaDTO> guardarCategoria(@RequestBody CategoriaDTO dto) {
        CategoriaDTO guardada = categoriaService.guardarCategoriaDTO(dto);
        return new ResponseEntity<>(guardada, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary="Editar datos",description="Edita los datos de una categoria")
    @ApiResponse(responseCode="404",description="No se a encontrado la categoria a editar")
    @ApiResponse(responseCode="400",description="Algun dato pudo haber estado mal escrito verifique")
    public ResponseEntity<CategoriaDTO> actualizarCategoria(@PathVariable Integer id, @RequestBody CategoriaDTO dto) {
        if(id <= 0){
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            CategoriaDTO actualizada = categoriaService.actualizarCategoriaDTO(id, dto);
            return new ResponseEntity<>(actualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Borrar datos",description="Elimina una categoria de la lista")
    @ApiResponse(responseCode="404",description="No se encontro la categoria a eliminar")
    @ApiResponse(responseCode="400",description="El id que escribio puede que sea erroneo")
    public ResponseEntity<String> eliminarCategoria(@PathVariable Integer id) {
        try {
            String mensaje = categoriaService.eliminarCategoriaDTO(id);
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
