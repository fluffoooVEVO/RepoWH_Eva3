package Figs40K.Figura.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Figs40K.Figura.model.Figuras;

public interface FigurasRepository extends JpaRepository<Figuras, Integer> {
}
