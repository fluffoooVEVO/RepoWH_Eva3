package Evaluacion2FS.Figuritas.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

// objeto de transferencia para exponer los datos del producto de forma segura
@Data
public class ProductoDTO {
    private Integer id_producto;
    @NotBlank(message="el nombre del producto no puede quedar vacio")
    private String nombre;
    private String descripcion;
    private LocalDate fechaCreacion;
    // validacion estricta para asegurar la integridad referencial de marca
    @NotNull(message="se requiere el id de la marca para asociar el producto")
    private Integer id_marca;

    // validacion estricta para asegurar la integridad referencial de edicion
    @NotNull(message="se requiere el id de la edicion para asociar el producto")
    private Integer id_edicion;
}