package com.Ev3FS.categorias.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import com.Ev3FS.categorias.DTO.CategoriaDTO;
import com.Ev3FS.categorias.controller.CategoriaController;

@Component
public class CategoriaModelAssembler implements RepresentationModelAssembler<CategoriaDTO, EntityModel<CategoriaDTO>> {
    @Override
    public EntityModel<CategoriaDTO> toModel(CategoriaDTO categoria) {
        return EntityModel.of(categoria,
            linkTo(methodOn(CategoriaController.class)
                .buscarPorId(categoria.getIdCategoria()))
            .withSelfRel(),
            linkTo(methodOn(CategoriaController.class)
                .getAll())
            .withRel("categorias"),
            linkTo(methodOn(CategoriaController.class)
                .guardarCategoria(null))
            .withRel("agregar-categoria"),
            linkTo(methodOn(CategoriaController.class)
                .actualizarCategoria(categoria.getIdCategoria(), null))
            .withRel("actualizar-categoria")
        );
    }
}
