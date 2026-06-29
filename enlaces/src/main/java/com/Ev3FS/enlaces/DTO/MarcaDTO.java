package com.Ev3FS.enlaces.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MarcaDTO {
    private Integer id_marca;

    @NotBlank(message = "El nombre de la marca es obligatorio")
    @Size(max = 100, message = "El nombre no puede pasar los 100 caracteres")
    private String nombre;

    @Size(max = 255, message = "La descripcion es muy larga")
    private String descripcion;
}