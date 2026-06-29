package Figs40K.Figura.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ProductoExternoDTO {
    private Integer id_producto;
    private String nombre;
    private String descripcion;
    private LocalDate fechaCreacion;
    private Integer idCategoria;
}