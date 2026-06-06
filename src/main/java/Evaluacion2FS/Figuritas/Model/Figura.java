package Evaluacion2FS.Figuritas.Model;
//id_figura, nombre, descripcion y url

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Figura")
public class Figura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_figura;

    @NotBlank(message="El nombre no puede quedar con atributo vacio")
    @Size(min=3, max=60, message="El nombre debe tener entre 3 y 60 caracteres")
    @Column(nullable=false, length=60)
    private String nombre;
    
    @NotBlank(message="La descripcion no puede quedar con atributo vacio")      
    @Size(min=3, max=255, message="La descripcion debe tener entre 3 y 255 caracteres")
    @Column(nullable=false, length=255)
    private String descripcion;

    @Column(nullable=true, length=255)
    private String url;

    //no tocar o se chinga el diagrama
    @OneToMany(mappedBy = "figura")
    private List<Figuras> productos;

    //{
  //"nombre": "Horus Lupercal",
  //"descripcion": "El primarca de la legión de los Hijos de Horus, conocido por su traición a la humanidad y su papel central en la Herejía de Horus",
  //"url": "https://wh40k.lexicanum.com/wiki/Horus_Lupercal"
//}
}