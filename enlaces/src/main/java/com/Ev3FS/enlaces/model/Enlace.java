package com.Ev3FS.enlaces.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Enlace")
public class Enlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_enlace;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 255)
    private String url;
}