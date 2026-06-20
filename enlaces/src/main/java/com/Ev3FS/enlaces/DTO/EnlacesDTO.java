package com.Ev3FS.enlaces.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnlacesDTO {
    private Integer id_enlace_producto;

    @NotNull(message = "El ID del enlace es obligatorio")
    private Integer id_enlace;

    @NotNull(message = "El ID del producto es obligatorio")
    private Integer id_producto;
}