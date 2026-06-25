package com.Ev3FS.categorias.DTO;

import lombok.Data; // Asumiendo que usas Lombok para los getters y setters

@Data
public class CategoriasDTO {
    
    private Integer id_categorias_producto;
    private Integer id_categorias;
    private Integer id_producto;
    private ProductoExternoDTO producto; 
    

}