package com.Ev3FS.Edicion.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.Ev3FS.Edicion.model.Edicion;
import com.Ev3FS.Edicion.repository.EdicionRepository;

@ExtendWith(MockitoExtension.class)
public class EdicionServiceTest {

    @Mock
    private EdicionRepository edicionRepository;

    @InjectMocks
    private EdicionService edicionService;

    private Edicion edicionEjemplo;

    @BeforeEach
    void setUp() {
        edicionEjemplo = new Edicion();
        edicionEjemplo.setId_edicion(1);
        edicionEjemplo.setNombre("10ma Edición");
        edicionEjemplo.setDescripcion("Edición actual del juego Warhammer 40K");
        edicionEjemplo.setStatus(true);
    }

    @Test
    void obtenerPorId_deberiaRetornarEdicion_cuandoExiste() {
        // Given
        when(edicionRepository.findById(1)).thenReturn(Optional.of(edicionEjemplo));

        // When
        Edicion resultado = edicionService.obtenerPorId(1);

        // Then
        assertEquals("10ma Edición", resultado.getNombre());
        assertEquals(1, resultado.getId_edicion());
        verify(edicionRepository).findById(1);
    }

    @Test
    void obtenerPorId_deberiaLanzarExcepcion_cuandoNoExiste() {
        // Given
        when(edicionRepository.findById(99)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseStatusException.class, () -> {
            edicionService.obtenerPorId(99);
        });
        verify(edicionRepository).findById(99);
    }
}
