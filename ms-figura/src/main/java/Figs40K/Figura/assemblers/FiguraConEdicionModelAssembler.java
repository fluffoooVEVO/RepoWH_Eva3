package Figs40K.Figura.assemblers;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import Figs40K.Figura.DTO.FiguraConEdicionDTO;
import Figs40K.Figura.client.EdicionClient;
import Figs40K.Figura.controller.FiguraController;

@Component
public class FiguraConEdicionModelAssembler implements RepresentationModelAssembler<FiguraConEdicionDTO, FiguraConEdicionDTO> {
    private final EdicionClient edicionClient;
    public FiguraConEdicionModelAssembler(EdicionClient edicionClient) {
        this.edicionClient = edicionClient;
    }
    @Override
    public FiguraConEdicionDTO toModel(FiguraConEdicionDTO dto) {
        dto.add(
            linkTo(methodOn(FiguraController.class).obtenerConEdicion(dto.getId_figura()))
                .withSelfRel()
        );
        if (dto.getEdicion() != null) {
            dto.add(
                Link.of(edicionClient.getBaseUrl() + "/api/v1/edicion/" + dto.getEdicion().getId_edicion())
                    .withRel("edicion")
            );
        }
        return dto;
    }
}
