package Evaluacion2FS.Figuritas.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Evaluacion2FS.Figuritas.DTO.FigurasDTO;
import Evaluacion2FS.Figuritas.Model.Figura;
import Evaluacion2FS.Figuritas.Model.Figuras;
import Evaluacion2FS.Figuritas.Model.Producto;
import Evaluacion2FS.Figuritas.Repository.FiguraRepository;
import Evaluacion2FS.Figuritas.Repository.FigurasRepository;
import Evaluacion2FS.Figuritas.Repository.ProductoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FigurasService {

    @Autowired
    private FigurasRepository figurasRepository;
    @Autowired
    private FiguraRepository figuraRepository;
    @Autowired
    private ProductoRepository productoRepository;

    private FigurasDTO convertirADTO(Figuras figura){
        FigurasDTO dto=new FigurasDTO();
        dto.setId_producto_figura(figura.getId_producto_figura());
        dto.setId_producto(figura.getProducto().getId_producto());
        dto.setId_figura(figura.getFigura().getId_figura());
        return dto;
    }

    private Figuras convertirAEntidad(FigurasDTO dto){
        Figuras figura=new Figuras();
        figura.setId_producto_figura(dto.getId_producto_figura());
        figura.setProducto(null);
        figura.setFigura(null);
        return figura;
    }

    public List<FigurasDTO> obtenerTodos(){
        return figurasRepository.findAll().stream()
            .map(this::convertirADTO)
            .toList();
    }

    public FigurasDTO guardarFigura(FigurasDTO dto){
        log.info("Guardando figura");
        Figuras figura = convertirAEntidad(dto);
        log.info("Figura guardad con exito");
        return convertirADTO(figurasRepository.save(figura));
    }

    public FigurasDTO obtenerPorID(Integer id){
        log.info("Buscando ID: {}", id);
        Figuras figura = figurasRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Error: no se encontro la figura con id {}", id);
                return new RuntimeException("No se encontro el registro con ID: " + id);
            });
        log.info("Figura encontrada exitosamente");
        return convertirADTO(figura);
    }

    public String eliminarFigura(Integer id){
        log.info("Eliminando figura con ID: {}", id);
        if (!figurasRepository.existsById(id)) {
            log.error("Error: no se encontro la figura con id {}", id);
            throw new RuntimeException("No se encontro el registro con ID: " + id);
        }
        figurasRepository.deleteById(id);
        log.info("Figura eliminada exitosamente");
        return "Figura eliminada exitosamente";
    }
    
public FigurasDTO actualizarFigura(Integer id, FigurasDTO dto) { // Cambiado de Figuras a FigurasDTO
    log.info("Iniciando actualizacion de Figura ID: {}", id);
    Figuras existente=figurasRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("No se puede actualizar. ID " + id + " no encontrado."));

    if (dto.getId_figura()!=null) {
        Figura figurita = figuraRepository.findById(dto.getId_figura()) // Usamos el ID del DTO
            .orElseThrow(() -> new RuntimeException("Entidad Figura no encontrada con ID: " + dto.getId_figura()));
        existente.setFigura(figurita);
    }
    if (dto.getId_producto()!=null) {
        Producto prod = productoRepository.findById(dto.getId_producto()) // Usamos el ID del DTO
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + dto.getId_producto()));
        existente.setProducto(prod);
    }

    Figuras actualizada = figurasRepository.save(existente);
    log.info("Figura ID {} actualizada correctamente", id);
    return convertirADTO(actualizada);
}






}
