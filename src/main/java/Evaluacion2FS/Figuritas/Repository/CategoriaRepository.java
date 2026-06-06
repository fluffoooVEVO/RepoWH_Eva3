package Evaluacion2FS.Figuritas.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Evaluacion2FS.Figuritas.Model.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    // Se declaran como firmas de métodos (con paréntesis)
    List<Categoria> findByStatusTrue();
    List<Categoria> findByStatusFalse();

}