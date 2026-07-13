package com.Ev3FS.Edicion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name="Edicion")
public class Edicion {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id_edicion;

    @NotBlank(message="El nombre no puede quedar con atributo vacio")
    @Size(min=3, max=60, message="El nombre debe tener entre 3 y 60 caracteres")
    @Column(nullable=false,length=60)
    private String nombre;

    @NotBlank(message="La descripcion no puede quedar con atributo vacio")
    @Size(min=3, max=255, message="La descripcion debe tener entre3 y 255 caracteres")
    @Column(nullable=false,length=255)
    private String descripcion;

    @Column(nullable=false)
    @Builder.Default
    private Boolean status = true;
}
