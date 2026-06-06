package Evaluacion2FS.Figuritas.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Evaluacion2FS.Figuritas.Model.Figura;

@Repository
public interface FiguraRepository extends JpaRepository<Figura, Integer> {
}
