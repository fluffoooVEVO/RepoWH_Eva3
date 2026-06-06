package Evaluacion2FS.Figuritas.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// el DTO (data transfer object) sirve para recibir los datos desde postman
// sin exponer directamente la tabla de la base de datos por seguridad
@Data
public class MarcaDTO {
    
    private Integer id_marca;

    // @NotBlank revisa que el texto no venga vacio ni lleno de puros espacios
    @NotBlank(message = "el nombre de la marca es obligatorio compa")
    // @Size valida que el texto que mandan no sea mas largo que lo que aguanta la bd
    @Size(max = 100, message = "el nombre no puede pasar los 100 caracteres")
    private String nombre;

    @Size(max = 255, message = "la descripcion es muy larga")
    private String descripcion;
}