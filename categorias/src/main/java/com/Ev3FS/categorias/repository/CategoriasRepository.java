package com.Ev3FS.categorias.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Ev3FS.categorias.model.Categorias;

@Repository
public interface  CategoriasRepository extends  JpaRepository<Categorias, Integer>{

}
