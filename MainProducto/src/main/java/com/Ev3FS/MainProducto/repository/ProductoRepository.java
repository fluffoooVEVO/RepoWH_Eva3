package com.Ev3FS.MainProducto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}