package com.Ev3FS.enlaces.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;
import com.Ev3FS.enlaces.DTO.EnlaceDTO;
import com.Ev3FS.enlaces.controller.EnlaceController;

@Component
public class EnlaceModelAssembler implements RepresentationModelAssembler<EnlaceDTO, EntityModel<EnlaceDTO>> {
    @Override
    public EntityModel<EnlaceDTO> toModel(EnlaceDTO enlace) {
        return EntityModel.of(enlace,
            linkTo(methodOn(EnlaceController.class).obtenerEnlace(enlace.getId_enlace())).withSelfRel(),
            linkTo(methodOn(EnlaceController.class).listarEnlaces()).withRel("enlaces"),
            linkTo(methodOn(EnlaceController.class).crearEnlace(enlace)).withRel("agregar-enlace")
        );
    }
}