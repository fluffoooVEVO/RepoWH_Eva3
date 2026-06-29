package com.Ev3FS.categorias.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data; // Asumiendo que usas Lombok para los getters y setters

@Data
public class CategoriasDTO {
    
    private Integer id_categorias_producto;
    private Integer id_categorias;
    private Integer id_producto;
    @JsonIgnore
    private ProductoExternoDTO producto;    

}