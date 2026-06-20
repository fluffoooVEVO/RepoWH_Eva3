package com.Ev3FS.enlaces.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;
import com.Ev3FS.enlaces.DTO.MarcaDTO;
import com.Ev3FS.enlaces.controller.MarcaController;

@Component
public class MarcaModelAssembler implements RepresentationModelAssembler<MarcaDTO, EntityModel<MarcaDTO>> {
    @Override
    public EntityModel<MarcaDTO> toModel(MarcaDTO marca) {
        return EntityModel.of(marca,
            linkTo(methodOn(MarcaController.class).obtenerMarca(marca.getId_marca())).withSelfRel(),
            linkTo(methodOn(MarcaController.class).listarMarcas()).withRel("marcas"),
            linkTo(methodOn(MarcaController.class).crearMarca(marca)).withRel("agregar-marca"),
            linkTo(methodOn(MarcaController.class).actualizarMarca(marca.getId_marca(), marca)).withRel("actualizar-marca")
        );
    }
}