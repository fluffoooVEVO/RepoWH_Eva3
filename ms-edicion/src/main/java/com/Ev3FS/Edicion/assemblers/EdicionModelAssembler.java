package com.Ev3FS.Edicion.assemblers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import com.Ev3FS.Edicion.DTO.EdicionDTO;
import com.Ev3FS.Edicion.controller.EdicionController;

@Component
public class EdicionModelAssembler implements RepresentationModelAssembler<EdicionDTO, EntityModel<EdicionDTO>> {

    @Value("${ms.figura.url:http://localhost:8091}")
    private String figuraBaseUrl;

    @Override
    public EntityModel<EdicionDTO> toModel(EdicionDTO dto) {
        return EntityModel.of(dto,
            linkTo(methodOn(EdicionController.class).obtenerPorId(dto.getId_edicion())).withSelfRel(),
            Link.of(figuraBaseUrl + "/api/v1/figura/edicion/" + dto.getId_edicion()).withRel("figuras")
        );
    }
}
