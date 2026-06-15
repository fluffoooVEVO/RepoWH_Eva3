package com.Ev3FS.categorias.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Ev3FS.categorias.model.Categoria;


@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    // Se declaran como firmas de métodos (con paréntesis)
    List<Categoria> findByStatusTrue();
    List<Categoria> findByStatusFalse();

}