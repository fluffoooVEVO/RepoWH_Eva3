package com.Ev3FS.Figura.DTO;

import lombok.Data;

// DTO externo: representa los datos de Edicion que viajan desde el microservicio ms-edicion.
// (Equivalente al patron SableExternoDTO de la bitacora del profesor.)
@Data
public class EdicionExternoDTO {
    private Integer id_edicion;
    private String nombre;
    private String descripcion;
    private Boolean status;
}
