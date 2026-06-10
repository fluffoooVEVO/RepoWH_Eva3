package Evaluacion2FS.Figuritas.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Evaluacion2FS.Figuritas.Model.Categorias;

@Repository
public interface  CategoriasRepository extends  JpaRepository<Categorias, Integer>{

}
