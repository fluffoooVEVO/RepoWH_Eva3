package Evaluacion2FS.Figuritas.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// estas tres anotaciones de lombok (@Data, @NoArgsConstructor, @AllArgsConstructor) 
// nos ahorran escribir los getters, setters y constructores a mano
@Data
@NoArgsConstructor
@AllArgsConstructor
// @Entity le avisa a spring que esta clase representa una tabla real en la base de datos
@Entity
@Table(name="Marca")
public class Marca {

    // @Id indica que este campo es la llave primaria de la tabla
    // @GeneratedValue hace que el id sea autoincrementable (1, 2, 3, 4 etc)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_marca;

    // @Column configura la columna en la base de datos. 
    // nullable = false significa que este dato no puede ser nulo (obligatorio)
    @Column(nullable = false, length = 100)
    private String nombre;

    // aca solo limitamos el largo maximo a 255 caracteres
    @Column(length = 255)
    private String descripcion;

    //{
  //"nombre": "Games Workshop",
  //"descripcion": "La marca más reconocida en el mundo de las figuras de Warhammer,
// conocida por su calidad y detalle en cada una de sus figuras"}

}