package Figs40K.Figura.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Figs40K.Figura.DTO.FigurasDTO;
import Figs40K.Figura.model.Figura;
import Figs40K.Figura.model.Figuras;
import Figs40K.Figura.repository.FiguraRepository;
import Figs40K.Figura.repository.FigurasRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FigurasService {

    @Autowired
    private FigurasRepository figurasRepository;
    @Autowired
    private FiguraRepository figuraRepository;

    private FigurasDTO convertirADTO(Figuras figura){
        FigurasDTO dto=new FigurasDTO();
        dto.setId_producto_figura(figura.getId_producto_figura());
        dto.setId_figura(figura.getFigura().getId_figura());
        return dto;
    }

    private Figuras convertirAEntidad(FigurasDTO dto){
        Figuras figura=new Figuras();
        figura.setId_producto_figura(dto.getId_producto_figura());


        if (dto.getId_figura() != null) {
            Figura figuraEntidad = figuraRepository.findById(dto.getId_figura())
                .orElseThrow(() -> new RuntimeException("Figura no encontrada con ID: " + dto.getId_figura()));
            figura.setFigura(figuraEntidad);
        }

        return figura;
    }

    public List<FigurasDTO> obtenerTodos(){
        return figurasRepository.findAll().stream()
            .map(this::convertirADTO)
            .toList();
    }

    public FigurasDTO guardarFigura(FigurasDTO dto){
        log.info("Guardando figura asociada");
        Figuras figura = convertirAEntidad(dto);
        Figuras guardada = figurasRepository.save(figura);
        log.info("Figura asociada guardada con exito: {}", guardada.getId_producto_figura());
        return convertirADTO(guardada);
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

    public FigurasDTO actualizarFigura(Integer id, FigurasDTO dto) {
        log.info("Iniciando actualizacion de Figura ID: {}", id);
        Figuras existente=figurasRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se puede actualizar. ID " + id + " no encontrado."));

        if (dto.getId_figura()!=null) {
            Figura figurita = figuraRepository.findById(dto.getId_figura())
                .orElseThrow(() -> new RuntimeException("Entidad Figura no encontrada con ID: " + dto.getId_figura()));
            existente.setFigura(figurita);
        }

        Figuras actualizada = figurasRepository.save(existente);
        log.info("Figura ID {} actualizada correctamente", id);
        return convertirADTO(actualizada);
    }
}
