package Figs40K.Figura.DTO;

import lombok.Data;

// Respuesta enriquecida: combina los datos locales de la Figura con los datos
// de su Edicion obtenidos del microservicio ms-edicion via WebClient.
@Data
public class FiguraConEdicionDTO {
    private Integer id_figura;
    private String nombre;
    private String descripcion;
    private String url;
    private EdicionExternoDTO edicion;
}
