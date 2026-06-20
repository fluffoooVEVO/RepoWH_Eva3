package com.Ev3FS.enlaces.DTO;

import lombok.Data;

// Este DTO solo sirve para atrapar la respuesta del otro microservicio
@Data
public class ProductoExternoDTO {
    private Integer id_producto;
    private String nombre;
}