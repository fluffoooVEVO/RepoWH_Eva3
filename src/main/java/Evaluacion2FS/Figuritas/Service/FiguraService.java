package Evaluacion2FS.Figuritas.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Evaluacion2FS.Figuritas.DTO.FiguraDTO;
import Evaluacion2FS.Figuritas.Model.Figura;
import Evaluacion2FS.Figuritas.Repository.FiguraRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FiguraService {
        @Autowired
        FiguraRepository figuraRepository;

        public List<Figura>obtenerTodos(){
            log.info("Obteniendo Figura");
            List<Figura> figura = figuraRepository.findAll(); 
            log.debug("Cantidad de figuras encontradas: ", figura.size()); 
            return figuraRepository.findAll();
        }

        public Figura obtenerPorId(Integer id_Figura){
            log.info("Buscando figura con ID: ", id_Figura);
            Figura figura = figuraRepository.findById(id_Figura)
            .orElseThrow(() -> {
                log.error("Figura no encontrada con ID: ", id_Figura);
                return new RuntimeException("No se encontro Figura con la id");
            });
            log.debug("Figura encontrada: ", figura.getNombre());
            return figura;
        }

        public String eliminarFigura(Integer id_Figura){
            log.info("Iniciando eliminación de figura con ID: ", id_Figura);
        try {
            Figura figura=figuraRepository.findById(id_Figura)
            .orElseThrow(()-> {
                log.error("Figura no encontrada al eliminar con ID: ", id_Figura);
                return new RuntimeException("No se encontro Figura con la id");
            });
            figuraRepository.delete(figura);
            log.info("Figura eliminada correctamente con ID: ", id_Figura);
            return "La figura a sido eliminada exitosamente";
        } catch (Exception e) {
            log.error("Error al eliminar figura: ", e.getMessage(), e);
            return e.getMessage();
        }
    }

    public Figura guardarFigura(Figura figura) {
        log.info("Guardando figura: {}", figura.getNombre());
        Figura saved = figuraRepository.save(figura);
        log.info("Figura guardada correctamente con ID: {}", saved.getId_figura());
        return saved;
    }

    public Figura actualizarFigura(Integer id_Figura, Figura figura) {
        log.info("Actualizando figura con ID: {}", id_Figura);
        Figura fig = figuraRepository.findById(id_Figura)
            .orElseThrow(() -> {
                log.error("Figura no encontrada al actualizar con ID: {}", id_Figura);
                return new RuntimeException("No se encontró Figura con la id");
            });
        if (figura.getNombre() != null) {
            log.debug("Actualizando nombre de figura a: {}", figura.getNombre());
            fig.setNombre(figura.getNombre());
        }
        if (figura.getDescripcion() != null) {
            log.debug("Actualizando descripcion de figura a: {}", figura.getDescripcion());
            fig.setDescripcion(figura.getDescripcion());
        }
        if (figura.getUrl() != null) {
            log.debug("Actualizando URL de figura a: {}", figura.getUrl());
            fig.setUrl(figura.getUrl());
        }
        Figura updated = figuraRepository.save(fig);
        log.info("Figura actualizada correctamente con ID: {}", id_Figura);
        return updated;
    }

        public FiguraDTO convertirADTO(Figura figura){
            FiguraDTO figuraDTO = new FiguraDTO();
            figuraDTO.setId_figura(figura.getId_figura());
            figuraDTO.setNombre(figura.getNombre());
            figuraDTO.setDescripcion(figura.getDescripcion());
            figuraDTO.setUrl(figura.getUrl());
            return figuraDTO;
        }

        public List<FiguraDTO> convertirListaADTO(List<Figura> figuras){
            return figuras.stream()
            .map(this::convertirADTO)
            .toList();
        }

        public Figura convertirAEntidad(FiguraDTO figuraDTO){
            Figura figura = new Figura();
            figura.setId_figura(figuraDTO.getId_figura());
            figura.setNombre(figuraDTO.getNombre());
            figura.setDescripcion(figuraDTO.getDescripcion());
            figura.setUrl(figuraDTO.getUrl());
            return figura;
        }

        public FiguraDTO obtenerDTOporId(Integer id_Figura){
            Figura figura = figuraRepository.findById(id_Figura)
            .orElseThrow(() -> new RuntimeException("No se encontro Figura con la id"));
            return convertirADTO(figura);
        }
    }
