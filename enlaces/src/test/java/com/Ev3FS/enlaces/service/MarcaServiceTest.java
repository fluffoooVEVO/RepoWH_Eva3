package com.Ev3FS.enlaces.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.Ev3FS.enlaces.DTO.MarcaDTO;
import com.Ev3FS.enlaces.Exception.ResourceNotFoundException;
import com.Ev3FS.enlaces.model.Marca;
import com.Ev3FS.enlaces.repository.MarcaRepository;

@ExtendWith(MockitoExtension.class)
class MarcaServiceTest {

    @Mock
    private MarcaRepository marcaRepository;

    @InjectMocks
    private MarcaService marcaService;

    private Marca marca;
    private MarcaDTO marcaDTO;

    @BeforeEach
    void setUp() {
        marca = new Marca(1, "Citadel", "Pinturas oficiales");
        marcaDTO = new MarcaDTO();
        marcaDTO.setId_marca(1);
        marcaDTO.setNombre("Citadel");
        marcaDTO.setDescripcion("Pinturas oficiales");
    }

    @Test
    void obtenerTodas_DebeRetornarLista() {
        when(marcaRepository.findAll()).thenReturn(Arrays.asList(marca));
        List<MarcaDTO> resultado = marcaService.obtenerTodas();
        assertFalse(resultado.isEmpty());
        assertEquals("Citadel", resultado.get(0).getNombre());
    }

    @Test
    void buscarPorId_CuandoExiste_DebeRetornarMarca() {
        when(marcaRepository.findById(1)).thenReturn(Optional.of(marca));
        MarcaDTO resultado = marcaService.buscarPorId(1);
        assertEquals("Citadel", resultado.getNombre());
    }

    @Test
    void buscarPorId_CuandoNoExiste_LanzaExcepcion() {
        when(marcaRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> marcaService.buscarPorId(99));
    }

    @Test
    void guardarMarca_DebeRetornarGuardado() {
        when(marcaRepository.save(any(Marca.class))).thenReturn(marca);
        MarcaDTO resultado = marcaService.guardarMarca(marcaDTO);
        assertEquals("Citadel", resultado.getNombre());
    }

    @Test
    void eliminarMarca_DebeLlamarDelete() {
        when(marcaRepository.findById(1)).thenReturn(Optional.of(marca));
        doNothing().when(marcaRepository).delete(marca);
        marcaService.eliminarMarca(1);
        verify(marcaRepository, times(1)).delete(marca);
    }
}