package com.Ev3FS.categorias.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.Ev3FS.categorias.controller.CategoriaController;
import com.Ev3FS.categorias.model.Categoria;

@Component
public class CategoriaModelAssembler implements RepresentationModelAssembler<Categoria, EntityModel<Categoria>> {
    @Override
    public EntityModel<Categoria> toModel(Categoria categoria) {
        return EntityModel.of(categoria, // Envuelve Categoria y le agrega links de navegacion
            linkTo(                                          // Construye la URL
                methodOn(CategoriaController.class)          // Apunta al controller
                .buscarPorId(categoria.getIdCategoria()))    // Simula llamar buscarPorId con el id
            .withSelfRel(),                                  // Link "self" = URL de esta categoria (/api/v1/categoria/1)
            linkTo(                                          // Construye la URL
                methodOn(CategoriaController.class)          // Apunta al controller
                .getAll())                                   // Simula llamar getAll
            .withRel("categorias")                           // Link "categorias" = URL de la lista (/api/v1/categoria)
        );
    }
}
