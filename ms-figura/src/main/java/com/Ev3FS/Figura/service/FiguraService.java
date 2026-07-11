package com.Ev3FS.Figura.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.Ev3FS.Figura.DTO.EdicionExternoDTO;
import com.Ev3FS.Figura.DTO.FiguraConEdicionDTO;
import com.Ev3FS.Figura.DTO.FiguraDTO;
import com.Ev3FS.Figura.client.EdicionClient;
import com.Ev3FS.Figura.model.Figura;
import com.Ev3FS.Figura.repository.FiguraRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FiguraService {

    @Autowired
    FiguraRepository figuraRepository;

    // Antes se inyectaba EdicionRepository (acceso directo a la tabla Edicion).
    // Ahora la comunicacion con Edicion es por red, via el cliente WebClient.
    @Autowired
    EdicionClient edicionClient;

    public List<Figura> obtenerTodos(){
        log.info("Obteniendo figuras");
        List<Figura> figura = figuraRepository.findAll();
        log.debug("Cantidad de figuras encontradas: {}", figura.size());
        return figura;
    }

    public List<FiguraDTO> obtenerPorEdicion(Integer idEdicion){
        log.info("Buscando figuras de la edición con ID: {}", idEdicion);
        List<Figura> figuras = figuraRepository.findByIdEdicion(idEdicion);
        log.debug("Cantidad de figuras encontradas para la edición {}: {}", idEdicion, figuras.size());
        return convertirListaADTO(figuras);
    }

    public Figura obtenerPorId(Integer id_Figura){
        log.info("Buscando figura con ID: {}", id_Figura);
        Figura figura = figuraRepository.findById(id_Figura)
        .orElseThrow(() -> {
            log.error("Figura no encontrada con ID: {}", id_Figura);
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro Figura con la id");
        });
        log.debug("Figura encontrada: {}", figura.getNombre());
        return figura;
    }

    public String eliminarFigura(Integer id_Figura){
        log.info("Iniciando eliminación de figura con ID: {}", id_Figura);
        try {
            Figura figura = figuraRepository.findById(id_Figura)
            .orElseThrow(() -> {
                log.error("Figura no encontrada al eliminar con ID: {}", id_Figura);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro Figura con la id");
            });
            figuraRepository.delete(figura);
            log.info("Figura eliminada correctamente con ID: {}", id_Figura);
            return "La figura ha sido eliminada exitosamente";
        } catch (Exception e) {
            log.error("Error al eliminar figura: {}", e.getMessage(), e);
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
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró Figura con la id");
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
        if (figura.getId_edicion() != null) {
            log.debug("Actualizando edicion de figura a: {}", figura.getId_edicion());
            fig.setId_edicion(figura.getId_edicion());
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
        figuraDTO.setId_edicion(figura.getId_edicion());
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

        // Validacion remota: antes era edicionRepository.findById(...).
        // Ahora se consulta a ms-edicion por red. Si no existe alla, lanza 404.
        EdicionExternoDTO edicion = edicionClient.obtenerEdicion(figuraDTO.getId_edicion());
        figura.setId_edicion(edicion.getId_edicion());

        return figura;
    }

    public FiguraDTO obtenerDTOporId(Integer id_Figura){
        Figura figura = figuraRepository.findById(id_Figura)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro Figura con la id"));
        return convertirADTO(figura);
    }

    // Respuesta enriquecida: trae la figura local y le adjunta los datos de su edicion
    // obtenidos del microservicio ms-edicion via WebClient.
    public FiguraConEdicionDTO obtenerFiguraConEdicion(Integer id_Figura){
        Figura figura = obtenerPorId(id_Figura);
        EdicionExternoDTO edicion = edicionClient.obtenerEdicion(figura.getId_edicion());

        FiguraConEdicionDTO dto = new FiguraConEdicionDTO();
        dto.setId_figura(figura.getId_figura());
        dto.setNombre(figura.getNombre());
        dto.setDescripcion(figura.getDescripcion());
        dto.setUrl(figura.getUrl());
        dto.setEdicion(edicion);
        return dto;
    }

}
