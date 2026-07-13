package com.Ev3FS.Figura.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import com.Ev3FS.Figura.DTO.FiguraConEdicionDTO;
import com.Ev3FS.Figura.client.EdicionClient;
import com.Ev3FS.Figura.controller.FiguraController;

@Component
public class FiguraConEdicionModelAssembler implements RepresentationModelAssembler<FiguraConEdicionDTO, EntityModel<FiguraConEdicionDTO>> {
    private final EdicionClient edicionClient;
    public FiguraConEdicionModelAssembler(EdicionClient edicionClient) {
        this.edicionClient = edicionClient;
    }
    @Override
    public EntityModel<FiguraConEdicionDTO> toModel(FiguraConEdicionDTO dto) {
        EntityModel<FiguraConEdicionDTO> model = EntityModel.of(dto,
            linkTo(methodOn(FiguraController.class).obtenerConEdicion(dto.getId_figura())).withSelfRel()
        );
        if (dto.getEdicion() != null) {
            model.add(
                Link.of(edicionClient.getBaseUrl() + "/api/v1/edicion/" + dto.getEdicion().getId_edicion())
                    .withRel("edicion")
            );
        }
        return model;
    }
}
