package com.Ev3FS.Edicion.DTO;

import lombok.Data;

// DTO de salida para Edicion. Se usa en vez de exponer la entidad JPA directamente,
// y permite agregar links HATEOAS (self, figuras) sin mezclar la capa de persistencia
// con la capa de presentacion de la API.
@Data
public class EdicionDTO {
    private Integer id_edicion;
    private String nombre;
    private String descripcion;
    private Boolean status;
}
