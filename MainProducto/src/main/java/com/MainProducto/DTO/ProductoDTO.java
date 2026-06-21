package com.MainProducto.DTO;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// objeto de transferencia para exponer los datos del producto de forma segura
@Data
public class ProductoDTO {
    private Integer id_producto;

    @NotBlank(message="el nombre del producto no puede quedar vacio")
    private String nombre;

    private String descripcion;

    private LocalDate fechaCreacion;

    // validacion estricta para asegurar la integridad referencial de categoria
    @NotNull(message="se requiere el id de la categoria para asociar el producto")
    private Integer idCategoria;

    //@NotNull(message="se requiere el id de la marca para asociar el producto")
    //private Integer id_marca;

    //@NotNull(message="se requiere el id de la edicion para asociar el producto")
    //private Integer id_edicion;
}
