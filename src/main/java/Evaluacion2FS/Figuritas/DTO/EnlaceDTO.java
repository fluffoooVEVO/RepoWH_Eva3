package Evaluacion2FS.Figuritas.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// usamos este DTO para validar lo que nos mandan antes de guardarlo en la tabla enlace
@Data
public class EnlaceDTO {
    
    private Integer id_enlace;

    // validamos que si o si nos manden el nombre del enlace
    @NotBlank(message = "tienes que ponerle un nombre al enlace")
    @Size(max = 100, message = "el nombre es muy largo")
    private String nombre;

    // validamos que la url venga con texto y no se pase del limite
    @NotBlank(message = "la url no puede ir en blanco")
    @Size(max = 255, message = "la url se paso del limite de caracteres")
    private String url;

    //{
  //"nombre": "Documentación Oficial Spring",
  //"url": "https://spring.io/projects/spring-boot"
    //}
}