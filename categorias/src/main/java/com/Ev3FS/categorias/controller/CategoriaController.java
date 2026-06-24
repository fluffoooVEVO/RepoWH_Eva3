package com.Ev3FS.categorias.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
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

import com.Ev3FS.categorias.DTO.CategoriaDTO;
import com.Ev3FS.categorias.assemblers.CategoriaModelAssembler;
import com.Ev3FS.categorias.service.CategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/categoria")
@Tag(name="Categoria",description="CRUD relacionado al modelo *Categoria*")
@ApiResponses({
    @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
    @ApiResponse(responseCode = "200",description = "Operacion exitosa",content=@Content)
})
//PORQUE ESTA AL PRINCIPIO FAMILIA? porque cada vez que se quiera ejecutar este controller tendra que pasar por todos los api response puse los mas basicos 
//si quieren agregar uno perso haganlo en el metodo OJO SI METEN TODOS SE MUESTRAN TODOS Y QUEDA INEXACTO
public class CategoriaController {
    
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private CategoriaModelAssembler assembler;

    @GetMapping
    @Operation(
        summary = "Obtener todas las categorias",
        description = "Muestra todas las categorias existentes o un mensaje con opciones si está vacía."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operación exitosa (lista llena o vacía con enlaces HATEOAS)")})
    public ResponseEntity<EntityModel<?>> getAll() {
        // Trae todas las categorias desde la base de datos via el service
        List<CategoriaDTO> categorias = categoriaService.obtenerTodasDTO();
        // Link HATEOAS que apunta directo a la sección "guardarCategoria" en Swagger
        Link linkCrear = Link.of("http://localhost:8081/doc/swagger-ui/index.html#/Categoria/guardarCategoria")
                .withRel("crear-categoria");
                
        if (categorias.isEmpty()) {
            // Cambiamos a 200 OK. Si usamos 204, el cliente NO verá el EntityModel ni los Links.
            EntityModel<?> vacio = EntityModel.of(
                Map.of("mensaje", "No hay categorias en este momento. Puedes crear una nueva."),
                linkCrear
            );
            return ResponseEntity.status(HttpStatus.OK).body(vacio);
        }
        // Segundo link apuntando a buscarPorId
        Link linkVerUna = Link.of("http://localhost:8081/doc/swagger-ui/index.html#/Categoria/buscarPorId")
                .withRel("ver-una-categoria");
        // Caso: hay categorias. Empaquetamos la lista + mensaje + ambos links
        EntityModel<?> conDatos = EntityModel.of(
            Map.of(
                "categorias", categorias,
                "mensaje", "Puedes crear una nueva categoria o ver una en especifico"
            ),
            linkCrear,
            linkVerUna
        );
        return ResponseEntity.ok(conDatos);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Obtener una categoria", description = "Obtiene una categoria con el parametro *id*")
    @ApiResponse(responseCode="400",description="Hubo un error *Tipeo quizas*?",content=@Content)
    @ApiResponse(responseCode="404",description="Categoria no encontrada",content=@Content)
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            if (id<=0) {
                Link link = Link.of("http://localhost:8081/doc/swagger-ui/index.html#/Categoria/buscarPorId")
                    .withRel("reintentar");
                    EntityModel<?> error=EntityModel.of(
                        Map.of("mensaje","Hubo un error a la hora de colocar el id a buscar"),
                        link
                    );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
        CategoriaDTO categoria = categoriaService.obtenerPorID(id);
        return ResponseEntity.ok(categoria);
        } catch (RuntimeException e) {
            Link linkCrear = Link.of("http://localhost:8081/doc/swagger-ui/index.html#/Categoria/guardarCategoria")
            .withRel("crear-categoria");
            EntityModel<?>noEncontrado=EntityModel.of(
                Map.of("mensaje", "Categoria no encontrada,quizas quieras Crear una con el href."),
                linkCrear
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noEncontrado);
        }
    }

    @GetMapping("/categorias-true")
    @Operation(summary="Obtener categorias true",description="obtiene una lista con todas las categorias las cuales esten *Activas*")
    @ApiResponse(responseCode="204",description="Lista vacia")
    public ResponseEntity<List<CategoriaDTO>> getStatusTrue() {
        try {
            List<CategoriaDTO> categorias = categoriaService.obtenerStatusTrueDTO();
            if (categorias.isEmpty()) {
                EntityModel<?> vacio = EntityModel.of(
                Map.of("mensaje", "No hay categorias"), // mensaje cuando la lista esta vacia
                linkTo(methodOn(CategoriaController.class)
                    .guardarCategoria(null)) // apunta al metodo guardar
                    .withRel("ver ids disponibles") // le pone el nombre al link
            );
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
        List<CategoriaDTO> categorias = categoriaService.obtenerStatusFalseDTO();
        if (categorias.isEmpty()) {
                EntityModel<?> vacio = EntityModel.of(
                Map.of("mensaje", "No hay categorias"), // mensaje cuando la lista esta vacia
                linkTo(methodOn(CategoriaController.class)
                    .guardarCategoria(null)) // apunta al metodo guardar
                    .withRel("crear-categoria") // le pone el nombre al link
            );
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Crear una nueva categoria")
    @ApiResponse(responseCode = "201", description = "Categoria creada :D")
    public ResponseEntity<EntityModel<CategoriaDTO>>guardarCategoria(@RequestBody CategoriaDTO dto) {
        CategoriaDTO guardada = categoriaService.guardarCategoriaDTO(dto);
        Link linkEditar = Link.of("http://localhost:8081/doc/swagger-ui/index.html#/Categoria/actualizarCategoria")
                .withRel("Por si te has equivocado en algun dato, puedes editarlo con este link");
        EntityModel<CategoriaDTO> posibleEdicion = EntityModel.of(guardada, linkEditar);
        return new ResponseEntity<>(posibleEdicion, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar datos", description = "Edita los datos de una categoria")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoria actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Algun dato pudo haber estado mal escrito o el ID es invalido, verifique", content = @Content),
        @ApiResponse(responseCode = "404", description = "No se ha encontrado la categoria a editar", content = @Content)
    })
    public ResponseEntity<?> actualizarCategoria(@PathVariable Integer id, @RequestBody CategoriaDTO dto) {
        if (id <= 0) {
            Link linkReintentar = Link.of("http://localhost:8081/doc/swagger-ui/index.html#/Categoria/actualizarCategoria")
                .withRel("reintentar-edicion");
            EntityModel<?> error = EntityModel.of(
                Map.of("mensaje", "El ID debe ser mayor a 0 para poder editar."),
                linkReintentar
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        try {
            CategoriaDTO actualizada = categoriaService.actualizarCategoriaDTO(id, dto);
            Link linkVer = Link.of("http://localhost:8081/doc/swagger-ui/index.html#/Categoria/buscarPorId")
                .withRel("ver-detalle");
            EntityModel<CategoriaDTO> exito = EntityModel.of(actualizada, linkVer);
            return ResponseEntity.ok(exito);
            
        } catch (RuntimeException e) {
            Link linkCrear = Link.of("http://localhost:8081/doc/swagger-ui/index.html#/Categoria/guardarCategoria")
                .withRel("crear-nueva-categoria");
            EntityModel<?> noEncontrado = EntityModel.of(
                Map.of("mensaje", "No se ha encontrado la categoria a editar. Quizas quieras crear una nueva."),
                linkCrear
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noEncontrado);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Borrar datos", description = "Elimina una categoria de la lista")
    @ApiResponse(responseCode = "404", description = "No se encontro la categoria a eliminar", content = @Content)
    @ApiResponse(responseCode = "400", description = "El id que escribio puede que sea erroneo", content = @Content)
    public ResponseEntity<?> eliminarCategoria(@PathVariable Integer id) {
        try {
            String mensaje = categoriaService.eliminarCategoriaDTO(id);
            Link linkListar = Link.of("http://localhost:8081/doc/swagger-ui/index.html#/Categoria/listarCategorias")
                    .withRel("Ver todas las categorias");

            EntityModel<Map<String, String>> respuesta = EntityModel.of(
                Map.of("mensaje", mensaje),
                linkListar
            );
            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}