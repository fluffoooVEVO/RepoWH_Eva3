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

// usamos las mismas anotaciones base para mapear la tabla enlace
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Enlace")
public class Enlace {

    // llave primaria autoincrementable igual que en marca
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_enlace;

    // el nombre del enlace es obligatorio y maximo 100 caracteres
    @Column(nullable = false, length = 100)
    private String nombre;

    // la url tambien es obligatoria para que no guarden enlaces vacios
    @Column(nullable = false, length = 255)
    private String url;

    //{
  //"nombre": "Lexicanum - Warhammer 40k Wiki",
  //"url": "https://wh40k.lexicanum.com/wiki/Main_Page"
//}
}