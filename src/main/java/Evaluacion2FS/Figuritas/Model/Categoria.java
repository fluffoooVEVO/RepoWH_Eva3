package Evaluacion2FS.Figuritas.Model;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_categoria")
    private Integer idCategoria;

    @NotBlank(message="El nombre no puede quedar vacio")
    @Column(nullable=false, length=50)
    private String nombre;

    @NotBlank(message="La descripcion no puede quedar vacia")
    @Column(nullable=false, length=200)
    private String descripcion;

    @NotNull(message="El atributo status no puede quedar vacio")
    @Column(nullable=false)
    private Boolean status = true;

    @OneToMany(mappedBy = "categoria")
    private List<Categorias> productos;

    //{
  //"nombre": "Mundial WH40K 2026",
  //"descripcion": "categoria del mundial de warhammer 40k del año 2026",
  //"status": true}
}



