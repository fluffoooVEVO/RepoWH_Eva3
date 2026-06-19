package com.Ev3FS.enlaces.service;

import com.Ev3FS.enlaces.DTO.EnlacesDTO;
import com.Ev3FS.enlaces.exception.ResourceNotFoundException;
import com.Ev3FS.enlaces.model.Enlace;
import com.Ev3FS.enlaces.model.Enlaces;
import com.Ev3FS.enlaces.repository.EnlaceRepository;
import com.Ev3FS.enlaces.repository.EnlacesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class EnlacesService {
    @Autowired
    private EnlacesRepository enlacesRepository;
    @Autowired
    private EnlaceRepository enlaceRepository;

    private EnlacesDTO convertirADto(Enlaces enlaces) {
        EnlacesDTO dto = new EnlacesDTO();
        dto.setId_enlace_producto(enlaces.getId_enlace_producto());
        dto.setId_enlace(enlaces.getEnlace().getId_enlace());
        dto.setId_producto(enlaces.getId_producto()); // Se corrigio por la separacion
        return dto;
    }

    public List<EnlacesDTO> obtenerTodos() {
        log.info("Obteniendo todas las relaciones producto-enlace");
        return enlacesRepository.findAll().stream().map(this::convertirADto).toList();
    }

    public EnlacesDTO buscarPorId(Integer id) {
        log.info("Buscando relacion ID: {}", id);
        Enlaces enlaces = enlacesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro la relacion ID " + id));
        return convertirADto(enlaces);
    }

    public EnlacesDTO guardarEnlaces(EnlacesDTO enlacesDTO) {
        log.info("Creando nueva relacion enlace-producto");
        Enlace enlace = enlaceRepository.findById(enlacesDTO.getId_enlace())
                .orElseThrow(() -> new ResourceNotFoundException("No existe el enlace ID " + enlacesDTO.getId_enlace()));

        Enlaces nuevaRelacion = new Enlaces();
        nuevaRelacion.setEnlace(enlace);
        nuevaRelacion.setId_producto(enlacesDTO.getId_producto());

        return convertirADto(enlacesRepository.save(nuevaRelacion));
    }

    public EnlacesDTO actualizarEnlaces(Integer id, EnlacesDTO enlacesDTO) {
        log.info("Actualizando relacion ID: {}", id);
        Enlaces relacionExistente = enlacesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro relacion ID " + id));

        Enlace enlace = enlaceRepository.findById(enlacesDTO.getId_enlace())
                .orElseThrow(() -> new ResourceNotFoundException("No existe el enlace ID " + enlacesDTO.getId_enlace()));

        relacionExistente.setEnlace(enlace);
        relacionExistente.setId_producto(enlacesDTO.getId_producto());

        return convertirADto(enlacesRepository.save(relacionExistente));
    }

    public void eliminarEnlaces(Integer id) {
        log.info("Eliminando relacion ID: {}", id);
        Enlaces enlaces = enlacesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro relacion ID " + id));
        enlacesRepository.delete(enlaces);
    }
}