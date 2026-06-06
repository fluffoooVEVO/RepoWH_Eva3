package Evaluacion2FS.Figuritas.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Evaluacion2FS.Figuritas.Model.Enlace;

// mismo cuento aca. esto crea el puente entre nuestro modelo Enlace y la base de datos
@Repository
public interface EnlaceRepository extends JpaRepository<Enlace, Integer> {
    
    // queda vacio por dentro porque heredamos todas las funciones listas para usar
}