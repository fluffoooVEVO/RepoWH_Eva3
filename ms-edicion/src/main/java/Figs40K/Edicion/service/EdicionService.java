package Figs40K.Edicion.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import Figs40K.Edicion.model.Edicion;
import Figs40K.Edicion.repository.EdicionRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EdicionService {

    @Autowired
    EdicionRepository edicionRepository;

    public List<Edicion> obtenerTodos() {
        log.info("Obteniendo todas las ediciones");
        List<Edicion> ediciones = edicionRepository.findAll();
        log.debug("Cantidad de ediciones encontradas: {}", ediciones.size());
        return ediciones;
    }

    public Edicion obtenerPorId(Integer id_Edicion) {
        log.info("Buscando edición con ID= {}", id_Edicion);
        return edicionRepository.findById(id_Edicion)
            .orElseThrow(() ->{
                log.error("Edición no encontrada con ID= {}", id_Edicion);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro Edicion con la id " + id_Edicion);
            });
    }

    public String eliminarEdicion(Integer id_Edicion) {
        log.info("Iniciando eliminación de edición con ID= ", id_Edicion);
        Edicion edicion = edicionRepository.findById(id_Edicion)
            .orElseThrow(() -> {
                log.error("Edición no encontrada al eliminar con ID= ", id_Edicion);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro Edicion con la id");
            });
        edicionRepository.delete(edicion);
        log.info("Edición eliminada correctamente con ID= ", id_Edicion);
        return "La edición ha sido eliminada exitosamente";
    }

    public Edicion guardarEdicion(Edicion edicion) {
        log.info("Guardando edición: {}", edicion.getNombre());
        Edicion saved= edicionRepository.save(edicion);
        log.info("Edicion guardada correctamente con ID={}", saved.getId_edicion());
        return saved;
    }

    public Edicion actualizarEdicion(Integer id_Edicion, Edicion edicion) {
        log.info("Actualizando edición con ID= {}", id_Edicion);
        log.debug("Datos recibidos para actualización: {}", edicion);

        Edicion edi = edicionRepository.findById(id_Edicion)
            .orElseThrow(() -> {
                log.error("Edición no encontrada al actualizar con ID= {}", id_Edicion);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro Edicion con esa id ");
            });

        if (edicion.getNombre()!=null){
            log.debug("Actualizando nombre a: ", edicion.getNombre());
            edi.setNombre(edicion.getNombre());
        }
        if (edicion.getDescripcion()!=null){
            log.debug("Actualizando descripcion a: ", edicion.getDescripcion());
            edi.setDescripcion(edicion.getDescripcion());
        }
        if (edicion.getStatus()!=null){
            log.debug("Actualizando status a: ", edicion.getStatus());
            edi.setStatus(edicion.getStatus());
        }

        Edicion updated = edicionRepository.save(edi);
        log.info("Edición actualizada correctamente con ID= ", id_Edicion);
        return updated;
    }
}
