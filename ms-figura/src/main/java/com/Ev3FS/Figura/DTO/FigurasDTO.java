package com.Ev3FS.Figura.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FigurasDTO {
    private Integer id_producto_figura;

    @NotNull(message = "Debe especificar el id de la figura")
    private Integer id_figura;

    @NotNull(message = "Debe especificar el id del producto")
    private Integer id_producto;
}
