package Figs40K.Edicion.assemblers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import Figs40K.Edicion.DTO.EdicionDTO;
import Figs40K.Edicion.controller.EdicionController;

@Component
public class EdicionModelAssembler implements RepresentationModelAssembler<EdicionDTO, EdicionDTO> {

    @Value("${ms.figura.url:http://localhost:8082}")
    private String figuraBaseUrl;
    @Override
    public EdicionDTO toModel(EdicionDTO dto) {
        dto.add(
            linkTo(methodOn(EdicionController.class).obtenerPorId(dto.getId_edicion()))
                .withSelfRel()
        );
        dto.add(
            Link.of(figuraBaseUrl + "/api/v1/figura/edicion/" + dto.getId_edicion())
                .withRel("figuras")
        );
        return dto;
    }
}