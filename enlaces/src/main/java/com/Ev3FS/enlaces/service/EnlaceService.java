package com.Ev3FS.enlaces.service;

import com.Ev3FS.enlaces.DTO.EnlaceDTO;
import com.Ev3FS.enlaces.Exception.ResourceNotFoundException;
import com.Ev3FS.enlaces.model.Enlace;
import com.Ev3FS.enlaces.repository.EnlaceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class EnlaceService {
    @Autowired
    private EnlaceRepository enlaceRepository;

    private EnlaceDTO convertirADto(Enlace enlace) {
        EnlaceDTO dto = new EnlaceDTO();
        dto.setId_enlace(enlace.getId_enlace());
        dto.setNombre(enlace.getNombre());
        dto.setUrl(enlace.getUrl());
        return dto;
    }

    private Enlace convertirAModelo(EnlaceDTO dto) {
        Enlace enlace = new Enlace();
        enlace.setNombre(dto.getNombre());
        enlace.setUrl(dto.getUrl());
        return enlace;
    }

    public List<EnlaceDTO> obtenerTodos() {
        log.info("Obteniendo todos los enlaces");
        return enlaceRepository.findAll().stream().map(this::convertirADto).toList();
    }

    public EnlaceDTO buscarPorId(Integer id) {
        log.info("Buscando enlace ID: {}", id);
        Enlace enlace = enlaceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro enlace ID " + id));
        return convertirADto(enlace);
    }

    public EnlaceDTO guardarEnlace(EnlaceDTO enlaceDTO) {
        log.info("Guardando enlace: {}", enlaceDTO.getNombre());
        return convertirADto(enlaceRepository.save(convertirAModelo(enlaceDTO)));
    }

    public EnlaceDTO actualizarEnlace(Integer id, EnlaceDTO enlaceDTO) {
        log.info("Actualizando enlace ID: {}", id);
        Enlace enlaceExistente = enlaceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro enlace ID " + id));
        enlaceExistente.setNombre(enlaceDTO.getNombre());
        enlaceExistente.setUrl(enlaceDTO.getUrl());
        return convertirADto(enlaceRepository.save(enlaceExistente));
    }

    public void eliminarEnlace(Integer id) {
        log.info("Eliminando enlace ID: {}", id);
        Enlace enlace = enlaceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro enlace ID " + id));
        enlaceRepository.delete(enlace);
    }
}