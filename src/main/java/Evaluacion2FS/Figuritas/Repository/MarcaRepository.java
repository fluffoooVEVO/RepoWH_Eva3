package Evaluacion2FS.Figuritas.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Evaluacion2FS.Figuritas.Model.Marca;

// la anotacion @Repository le dice a spring boot que este archivo se va a encargar 
// de hablar directamente con la base de datos para guardar, buscar o borrar
@Repository
public interface MarcaRepository extends JpaRepository<Marca, Integer> {
    
    // al extender de JpaRepository spring nos regala todos los metodos base
    // como findAll(), findById(), save() y delete().
    // le pasamos la clase (Marca) y el tipo de dato de su llave primaria (Integer)
    // asi nos evitamos escribir los tipicos "SELECT * FROM marca" a mano
}