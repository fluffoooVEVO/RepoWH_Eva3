package Figs40K.Figura.DTO;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

// Respuesta enriquecida: combina los datos locales de la Figura con los datos
// de su Edicion obtenidos del microservicio ms-edicion via WebClient.
// Extiende RepresentationModel para poder agregar links HATEOAS (self, edicion).
@Data
@EqualsAndHashCode(callSuper = false)
public class FiguraConEdicionDTO extends RepresentationModel<FiguraConEdicionDTO> {
    private Integer id_figura;
    private String nombre;
    private String descripcion;
    private String url;
    private EdicionExternoDTO edicion;
}