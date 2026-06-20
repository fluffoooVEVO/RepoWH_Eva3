package com.Ev3FS.enlaces.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EnlaceDTO {
    private Integer id_enlace;

    @NotBlank(message = "Tienes que ponerle un nombre al enlace")
    @Size(max = 100, message = "El nombre es muy largo")
    private String nombre;

    @NotBlank(message = "La URL no puede ir en blanco")
    @Size(max = 255, message = "La URL se paso del limite")
    private String url;
}