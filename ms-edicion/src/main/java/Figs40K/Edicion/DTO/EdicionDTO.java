package Figs40K.Edicion.DTO;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

// DTO de salida para Edicion. Se usa en vez de exponer la entidad JPA directamente,
// y permite agregar links HATEOAS (self, figuras) sin mezclar la capa de persistencia
// con la capa de presentacion de la API.
@Data
@EqualsAndHashCode(callSuper = false)
public class EdicionDTO extends RepresentationModel<EdicionDTO> {
    private Integer id_edicion;
    private String nombre;
    private String descripcion;
    private Boolean status;
}