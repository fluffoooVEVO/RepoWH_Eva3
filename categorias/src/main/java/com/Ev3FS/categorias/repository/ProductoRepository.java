package com.Ev3FS.categorias.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Ev3FS.categorias.model.Producto;

// interfaz de acceso a datos para la tabla producto
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}