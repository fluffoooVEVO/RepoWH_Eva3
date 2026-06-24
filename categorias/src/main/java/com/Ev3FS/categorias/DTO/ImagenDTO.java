package com.Ev3FS.categorias.DTO;

import lombok.Data;

@Data
public class ImagenDTO {
    private Integer id;
    private String url;
    private Integer orden;
    private String descripcion;
    private Integer idProducto;
}
