package com.Ev3FS.categorias.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.Ev3FS.categorias.DTO.CategoriasDTO;
import com.Ev3FS.categorias.controller.CategoriasController;

@Component
public class CategoriasModelAssembler implements RepresentationModelAssembler<CategoriasDTO, EntityModel<CategoriasDTO>> {
    @Override
    public EntityModel<CategoriasDTO> toModel(CategoriasDTO dto) {
        return EntityModel.of(dto,
            linkTo(methodOn(CategoriasController.class)
                .obtenerPorID(dto.getId_categorias_producto()))
            .withSelfRel(),
            linkTo(methodOn(CategoriasController.class)
                .getAll())
            .withRel("todasLasRelaciones"),
            linkTo(methodOn(CategoriasController.class)
                .crearCategorias(null))
            .withRel("crearRelacion")
        );
    }
}
