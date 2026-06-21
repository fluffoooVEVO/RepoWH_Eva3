package com.MainProducto.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// entidad principal que almacena el catalogo fisico de productos
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Producto")
public class Producto {

    // identificador unico del producto
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id_producto;

    // atributo principal con limite de caracteres definido en el esquema relacional
    @Column(nullable=false, length=150)
    private String nombre;

    // detalle extendido del producto
    @Column(length=1000)
    private String descripcion;

    // registro temporal de ingreso al sistema
    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;

    // como este es un microservicio independiente, solo guardamos el id de la categoria (no el objeto)
    @Column(name = "id_categoria", nullable = false)
    private Integer idCategoria;

    //@Column(name = "id_marca", nullable = false)
    //private Integer idMarca;

    //@Column(name = "id_edicion", nullable = false)
    //private Integer idEdicion;

    //@Column(name = "id_figura", nullable = false)
    //private Integer idFigura;
}
