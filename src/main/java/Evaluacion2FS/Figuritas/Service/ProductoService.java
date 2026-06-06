package Evaluacion2FS.Figuritas.Service;

import Evaluacion2FS.Figuritas.DTO.ProductoDTO;
import Evaluacion2FS.Figuritas.Exception.ResourceNotFoundException;
import Evaluacion2FS.Figuritas.Model.Edicion;
import Evaluacion2FS.Figuritas.Model.Marca;
import Evaluacion2FS.Figuritas.Model.Producto;
import Evaluacion2FS.Figuritas.Repository.EdicionRepository;
import Evaluacion2FS.Figuritas.Repository.MarcaRepository;
import Evaluacion2FS.Figuritas.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// la anotacion @Service indica que esta clase maneja la logica de negocio.
// aqui es donde procesamos los datos antes de que toquen la base de datos o el controlador.
@Service
public class ProductoService {

    // inyeccion de dependencias: traemos los repositorios necesarios para interactuar con la BD.
    // necesitamos los repositorios de Marca y Edicion para validar que existan antes de asociarlos a un producto.
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private EdicionRepository edicionRepository;

    // metodo auxiliar (privado) para transformar la Entidad (Producto) a DTO (ProductoDTO).
    // esto es una buena practica de seguridad para no exponer el objeto completo de la base de datos al usuario final.
    private ProductoDTO convertirADto(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId_producto(producto.getId_producto());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setFechaCreacion(producto.getFechaCreacion());
        // extraemos unicamente los IDs de las relaciones para el DTO
        dto.setId_marca(producto.getMarca().getId_marca());
        dto.setId_edicion(producto.getEdicion().getId_edicion());
        return dto;
    }

    // obtiene todos los productos de la BD, los convierte a DTO uno por uno y los devuelve en una lista
    public List<ProductoDTO> obtenerTodos() {
        List<Producto> listaProductos = productoRepository.findAll();
        List<ProductoDTO> listaDtos = new ArrayList<>();
        
        // iteramos sobre cada entidad producto recuperada y la mapeamos a DTO
        for (Producto producto : listaProductos) {
            listaDtos.add(convertirADto(producto));
        }
        return listaDtos;
    }

    // busca un producto especifico. si no lo encuentra en la BD, lanza nuestra excepcion personalizada.
    public ProductoDTO buscarPorId(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("no se encontro producto con el id especificado: " + id));
        return convertirADto(producto);
    }

    // metodo critico: crea un nuevo producto validando primero la integridad referencial (llaves foraneas)
    public ProductoDTO guardarProducto(ProductoDTO dto) {
        
        // 1. Validacion de existencia: verificamos que la marca enviada en el JSON realmente exista en la BD
        Marca marca = marcaRepository.findById(dto.getId_marca())
                .orElseThrow(() -> new ResourceNotFoundException("operacion rechazada: no existe la marca con id " + dto.getId_marca()));
        
        // 2. Validacion de existencia: verificamos que la edicion enviada en el JSON realmente exista en la BD
        Edicion edicion = edicionRepository.findById(dto.getId_edicion())
                .orElseThrow(() -> new ResourceNotFoundException("operacion rechazada: no existe la edicion con id " + dto.getId_edicion()));

        // 3. Construccion de la entidad: si las validaciones pasan, creamos el objeto Producto
        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre(dto.getNombre());
        nuevoProducto.setDescripcion(dto.getDescripcion());
        nuevoProducto.setFechaCreacion(dto.getFechaCreacion());
        
        // 4. Asignacion de dependencias: aca guardamos los objetos completos de Marca y Edicion, no solo el ID
        nuevoProducto.setMarca(marca);
        nuevoProducto.setEdicion(edicion);

        // 5. Persistencia: guardamos en la base de datos y retornamos el resultado mapeado a DTO
        Producto guardado = productoRepository.save(nuevoProducto);
        return convertirADto(guardado);
    }

    // elimina un producto, pero primero verifica que exista para evitar errores de base de datos
    public void eliminarProducto(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("no se puede eliminar: no se encontro producto con id " + id));
        productoRepository.delete(producto);
    }
}