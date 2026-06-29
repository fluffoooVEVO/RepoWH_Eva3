package Figs40K.Figura.model;

import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    // Antes era @ManyToOne hacia Edicion. Al separar el microservicio de Edicion,
    // se guarda solo el id (sin FK). La existencia de la edicion se valida via WebClient hacia ms-edicion.
    @NotNull(message="La figura debe pertenecer a una edicion")
    @Column(name = "id_edicion", nullable = false)
    private Integer id_edicion;

    //no tocar o se chinga el diagrama
    @OneToMany(mappedBy = "figura")
    private List<Figuras> productos;
}
