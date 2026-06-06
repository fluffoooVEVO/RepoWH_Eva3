package Evaluacion2FS.Figuritas.Model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    // relacion obligatoria con la entidad marca (muchos productos pueden tener una marca)
    @ManyToOne
    @JoinColumn(name = "id_marca", nullable = false)
    private Marca marca;

    // relacion obligatoria con la entidad edicion
    @ManyToOne
    @JoinColumn(name = "id_edicion", nullable = false)
    private Edicion edicion;

    // relacion obligatoria con la entidad categoria
    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

     // relacion obligatoria con la entidad figura
    @ManyToOne
    @JoinColumn(name = "id_figura", nullable = false)
    private Figura figura;

     // no se necesita relacion directa con imagen o enlace porque se accede a traves de las otras entidades
     // esto mantiene el modelo mas limpio
    
    
    // {
  //"nombre": "Escuadrón de Exterminadores de los Ángeles Sangrientos",
  //"descripcion": "Caja para montar 5 miniaturas de élite con armadura Terminator, incluyendo opciones de armas pesadas y sargento.",
  //"fechaCreacion": "2026-05-10",
  //"marca": { "id_marca": 1 },
  //"edicion": { "id_edicion": 1 },
  //"categoria": { "id_categoria": 1 },
  //"figura": { "id_figura": 1 }
//}
}
