package Evaluacion2FS.Figuritas.Repository;

import Evaluacion2FS.Figuritas.Model.Enlaces;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// interfaz que hereda anejar las operaciones en la tabla intermedia Enlaces
@Repository
public interface EnlacesRepository extends JpaRepository<Enlaces, Integer> {
}