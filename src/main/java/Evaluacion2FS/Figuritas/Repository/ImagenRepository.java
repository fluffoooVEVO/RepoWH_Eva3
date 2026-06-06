package Evaluacion2FS.Figuritas.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Evaluacion2FS.Figuritas.Model.Imagen;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen,Integer>{

    
} 
