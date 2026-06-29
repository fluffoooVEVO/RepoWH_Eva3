package com.Ev3FS.enlaces.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;
import com.Ev3FS.enlaces.DTO.EnlacesDTO;
import com.Ev3FS.enlaces.controller.EnlacesController;

@Component
public class EnlacesModelAssembler implements RepresentationModelAssembler<EnlacesDTO, EntityModel<EnlacesDTO>> {
    @Override
    public EntityModel<EnlacesDTO> toModel(EnlacesDTO enlaces) {
        return EntityModel.of(enlaces,
            linkTo(methodOn(EnlacesController.class).obtenerEnlace(enlaces.getId_enlace_producto())).withSelfRel(),
            linkTo(methodOn(EnlacesController.class).listarEnlaces()).withRel("relaciones-enlaces")
        );
    }
}