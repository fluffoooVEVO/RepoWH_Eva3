package Evaluacion2FS.Figuritas.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

// objeto de transferencia de datos utilizado para gestionar la creacion y actualizacion 
// de relaciones evitando exponer las entidades completas al cliente
@Data
public class EnlacesDTO {
    
    // identificador unico del registro intermedio
    private Integer id_enlace_producto;

    // validacion estricta para asegurar que la peticion incluya la referencia al enlace
    @NotNull(message = "el id del enlace es obligatorio para establecer la relacion")
    private Integer id_enlace;

    // validacion estricta para asegurar que la peticion incluya la referencia al producto
    @NotNull(message = "el id del producto es obligatorio para establecer la relacion")
    private Integer id_producto;
}