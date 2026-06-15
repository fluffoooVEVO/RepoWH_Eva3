package repository;

import org.springframework.data.jpa.repository.JpaRepository;

import model.Producto;

// interfaz de acceso a datos para la tabla producto
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}
