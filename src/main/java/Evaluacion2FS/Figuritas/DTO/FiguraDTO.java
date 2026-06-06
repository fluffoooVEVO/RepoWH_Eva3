package Evaluacion2FS.Figuritas.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FiguraDTO {
    private Integer id_figura;

    @NotBlank(message = "El nombre de la figura es obligatorio")
    @Size(min = 3, max = 60, message = "El nombre debe tener entre 3 y 60 caracteres")
    private String nombre;

    @NotBlank(message = "La descripción de la figura es obligatoria")
    @Size(min = 3, max = 255, message = "La descripción debe tener entre 3 y 255 caracteres")
    private String descripcion;

    @Size(max = 255, message = "La URL tiene un límite de 255 caracteres")
    private String url;
}

