package com.Ev3FS.enlaces.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Enlaces")
public class Enlaces {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_enlace_producto;

    @ManyToOne
    @JoinColumn(name = "id_enlace", nullable = false)
    private Enlace enlace;

    @Column(name = "id_producto", nullable = false)
    private Integer id_producto;
}