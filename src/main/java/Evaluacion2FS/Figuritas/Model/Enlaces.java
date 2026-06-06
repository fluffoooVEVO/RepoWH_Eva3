package Evaluacion2FS.Figuritas.Model;

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

// entidad que representa la tabla intermedia fisica en la base de datos relacional
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Enlaces")
public class Enlaces {

    // define la llave primaria autoincrementable exclusiva de esta tabla intermedia
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_enlace_producto;

    // establece la relacion de llave foranea (FK) con la entidad Enlace
    // usamos ManyToOne indicando que multiples registros intermedios pueden apuntar a un solo Enlace
    @ManyToOne
    @JoinColumn(name = "id_enlace", nullable = false)
    private Enlace enlace;

    // establece la relacion de llave foranea (FK) con la entidad Producto
    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;
}