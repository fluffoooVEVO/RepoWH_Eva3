package Evaluacion2FS.Figuritas.Repository;

import Evaluacion2FS.Figuritas.Model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// interfaz de acceso a datos para la tabla producto
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}