package Figs40K.Edicion.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Figs40K.Edicion.model.Edicion;

public interface EdicionRepository extends JpaRepository<Edicion, Integer> {
}
