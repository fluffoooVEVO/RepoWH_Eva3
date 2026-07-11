package com.Ev3FS.Figura.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Ev3FS.Figura.model.Figura;

public interface FiguraRepository extends JpaRepository<Figura, Integer> {

    // Antes filtraba por f.edicion.id_edicion (relacion JPA). Ahora id_edicion es una columna simple.
    @Query("SELECT f FROM Figura f WHERE f.id_edicion = :idEdicion")
    List<Figura> findByIdEdicion(@Param("idEdicion") Integer idEdicion);
}
