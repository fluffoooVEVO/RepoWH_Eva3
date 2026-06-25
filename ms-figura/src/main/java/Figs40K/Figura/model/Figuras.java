package Figs40K.Figura.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Figuras")
public class Figuras {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_producto_figura;

    @NotNull(message="Debe asociarse a una figura")
    @ManyToOne
    @JoinColumn(name = "id_figura", nullable = false)
    private Figura figura;
}
