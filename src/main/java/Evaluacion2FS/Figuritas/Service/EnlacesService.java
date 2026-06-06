package Evaluacion2FS.Figuritas.Service;

import Evaluacion2FS.Figuritas.DTO.EnlacesDTO;
import Evaluacion2FS.Figuritas.Exception.ResourceNotFoundException;
import Evaluacion2FS.Figuritas.Model.Enlace;
import Evaluacion2FS.Figuritas.Model.Enlaces;
import Evaluacion2FS.Figuritas.Model.Producto;
import Evaluacion2FS.Figuritas.Repository.EnlaceRepository;
import Evaluacion2FS.Figuritas.Repository.EnlacesRepository;
import Evaluacion2FS.Figuritas.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// clase que maneja la logica de negocio para la tabla intermedia
@Service
public class EnlacesService {

    @Autowired
    private EnlacesRepository enlacesRepository;

    @Autowired
    private EnlaceRepository enlaceRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // transforma la entidad recuperada de la bd a un dto para enviarlo al cliente
    private EnlacesDTO convertirADto(Enlaces enlaces) {
        EnlacesDTO dto = new EnlacesDTO();
        dto.setId_enlace_producto(enlaces.getId_enlace_producto());
        dto.setId_enlace(enlaces.getEnlace().getId_enlace());
        dto.setId_producto(enlaces.getProducto().getId_producto());
        return dto;
    }

    // obtiene todos los registros de la tabla intermedia
    public List<EnlacesDTO> obtenerTodos() {
        List<Enlaces> listaEnlaces = enlacesRepository.findAll();
        List<EnlacesDTO> listaDtos = new ArrayList<>();

        for (Enlaces enlaces : listaEnlaces) {
            listaDtos.add(convertirADto(enlaces));
        }
        return listaDtos;
    }

    // busca una relacion especifica por su id principal
    public EnlacesDTO buscarPorId(Integer id) {
        Enlaces enlaces = enlacesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("no se encontro la relacion con el id " + id));
        return convertirADto(enlaces);
    }

    // crea una nueva relacion verificando previamente que las llaves foraneas existan
    public EnlacesDTO guardarEnlaces(EnlacesDTO enlacesDTO) {
        // valida la existencia del enlace
        Enlace enlace = enlaceRepository.findById(enlacesDTO.getId_enlace())
                .orElseThrow(() -> new ResourceNotFoundException("no existe el enlace con id " + enlacesDTO.getId_enlace()));

        // valida la existencia del producto
        Producto producto = productoRepository.findById(enlacesDTO.getId_producto())
                .orElseThrow(() -> new ResourceNotFoundException("no existe el producto con id " + enlacesDTO.getId_producto()));

        Enlaces nuevaRelacion = new Enlaces();
        nuevaRelacion.setEnlace(enlace);
        nuevaRelacion.setProducto(producto);

        Enlaces relacionGuardada = enlacesRepository.save(nuevaRelacion);
        return convertirADto(relacionGuardada);
    }

    // actualiza las llaves foraneas de un registro existente
    public EnlacesDTO actualizarEnlaces(Integer id, EnlacesDTO enlacesDTO) {
        // verifica que el registro a editar exista
        Enlaces relacionExistente = enlacesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("no se encontro la relacion con id " + id));

        // valida que las nuevas llaves foraneas existan
        Enlace enlace = enlaceRepository.findById(enlacesDTO.getId_enlace())
                .orElseThrow(() -> new ResourceNotFoundException("no existe el enlace con id " + enlacesDTO.getId_enlace()));
        Producto producto = productoRepository.findById(enlacesDTO.getId_producto())
                .orElseThrow(() -> new ResourceNotFoundException("no existe el producto con id " + enlacesDTO.getId_producto()));

        relacionExistente.setEnlace(enlace);
        relacionExistente.setProducto(producto);

        Enlaces relacionActualizada = enlacesRepository.save(relacionExistente);
        return convertirADto(relacionActualizada);
    }

    // elimina un registro de la tabla intermedia
    public void eliminarEnlaces(Integer id) {
        Enlaces enlaces = enlacesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("no se encontro la relacion con id " + id));
        enlacesRepository.delete(enlaces);
    }
}