package com.Ev3FS.enlaces.service;

<<<<<<< HEAD
import java.util.List;

=======
import com.Ev3FS.enlaces.DTO.MarcaDTO;
import com.Ev3FS.enlaces.Exception.ResourceNotFoundException;
import com.Ev3FS.enlaces.model.Marca;
import com.Ev3FS.enlaces.repository.MarcaRepository;
import lombok.extern.slf4j.Slf4j;
>>>>>>> branchDaniel
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Ev3FS.enlaces.DTO.MarcaDTO;
import com.Ev3FS.enlaces.Exception.ResourceNotFoundException;
import com.Ev3FS.enlaces.model.Marca;
import com.Ev3FS.enlaces.repository.MarcaRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MarcaService {
    @Autowired
    private MarcaRepository marcaRepository;

    private MarcaDTO convertirADto(Marca marca) {
        MarcaDTO dto = new MarcaDTO();
        dto.setId_marca(marca.getId_marca());
        dto.setNombre(marca.getNombre());
        dto.setDescripcion(marca.getDescripcion());
        return dto;
    }

    private Marca convertirAModelo(MarcaDTO dto) {
        Marca marca = new Marca();
        marca.setNombre(dto.getNombre());
        marca.setDescripcion(dto.getDescripcion());
        return marca;
    }

    public List<MarcaDTO> obtenerTodas() {
        log.info("Buscando todas las marcas");
        return marcaRepository.findAll().stream().map(this::convertirADto).toList();
    }

    public MarcaDTO buscarPorId(Integer id) {
        log.info("Buscando marca con ID: {}", id);
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró la marca con ID: {}", id);
                    return new ResourceNotFoundException("No se encontro la marca con el id " + id);
                });
        return convertirADto(marca);
    }

    public MarcaDTO guardarMarca(MarcaDTO marcaDTO) {
        log.info("Guardando nueva marca: {}", marcaDTO.getNombre());
        Marca marcaGuardada = marcaRepository.save(convertirAModelo(marcaDTO));
        return convertirADto(marcaGuardada);
    }

    public MarcaDTO actualizarMarca(Integer id, MarcaDTO marcaDTO) {
        log.info("Actualizando marca con ID: {}", id);
        Marca marcaExistente = marcaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro la marca con el id " + id));

        marcaExistente.setNombre(marcaDTO.getNombre());
        marcaExistente.setDescripcion(marcaDTO.getDescripcion());

        return convertirADto(marcaRepository.save(marcaExistente));
    }

    public void eliminarMarca(Integer id) {
        log.info("Eliminando marca con ID: {}", id);
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro la marca con el id " + id));
        marcaRepository.delete(marca);
    }
}