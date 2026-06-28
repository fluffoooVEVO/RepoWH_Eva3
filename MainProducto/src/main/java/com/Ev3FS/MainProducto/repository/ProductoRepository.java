package com.Ev3FS.MainProducto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Ev3FS.MainProducto.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}