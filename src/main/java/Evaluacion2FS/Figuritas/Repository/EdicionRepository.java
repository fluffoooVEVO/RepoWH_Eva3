package Evaluacion2FS.Figuritas.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Evaluacion2FS.Figuritas.Model.Edicion;

@Repository
public interface EdicionRepository extends JpaRepository<Edicion, Integer> {
}
