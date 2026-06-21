package com.Ev3FS.enlaces.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import com.Ev3FS.enlaces.DTO.EnlaceDTO;
import com.Ev3FS.enlaces.Exception.ResourceNotFoundException;
import com.Ev3FS.enlaces.model.Enlace;
import com.Ev3FS.enlaces.repository.EnlaceRepository;

// Esta etiqueta enciende Mockito para poder crear objetos "falsos" o Mocks
@ExtendWith(MockitoExtension.class)
class EnlaceServiceTest {

    // @Mock simula la base de datos para no tocar la real durante las pruebas
    @Mock
    private EnlaceRepository enlaceRepository;

    // @InjectMocks inyecta nuestra base de datos falsa en el servicio real que vamos a probar
    @InjectMocks
    private EnlaceService enlaceService;

    private Enlace enlace;
    private EnlaceDTO enlaceDTO;

    // @BeforeEach se ejecuta antes de CADA test para dejarnos los datos listos y frescos
    @BeforeEach
    void setUp() {
        enlace = new Enlace(1, "Wiki Oficial", "https://wiki.com");
        
        enlaceDTO = new EnlaceDTO();
        enlaceDTO.setId_enlace(1);
        enlaceDTO.setNombre("Wiki Oficial");
        enlaceDTO.setUrl("https://wiki.com");
    }

    @Test
    void obtenerTodos_DebeRetornarListaDeEnlaces() {
        // GIVEN (Dado que...) la base de datos falsa va a devolver una lista con 1 enlace
        when(enlaceRepository.findAll()).thenReturn(Arrays.asList(enlace));

        // WHEN (Cuando...) llamamos al metodo real de nuestro servicio
        List<EnlaceDTO> resultado = enlaceService.obtenerTodos();

        // THEN (Entonces...) comprobamos que la lista no venga vacia y los datos coincidan
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Wiki Oficial", resultado.get(0).getNombre());
        // Verificamos que el repositorio falso fue llamado exactamente 1 vez
        verify(enlaceRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_CuandoExiste_DebeRetornarEnlace() {
        // GIVEN: El repositorio falso encuentra el enlace con ID 1
        when(enlaceRepository.findById(1)).thenReturn(Optional.of(enlace));

        // WHEN
        EnlaceDTO resultado = enlaceService.buscarPorId(1);

        // THEN
        assertNotNull(resultado);
        assertEquals("Wiki Oficial", resultado.getNombre());
        verify(enlaceRepository, times(1)).findById(1);
    }

    @Test
    void buscarPorId_CuandoNoExiste_DebeLanzarExcepcion() {
        // GIVEN: El repositorio falso NO encuentra nada (vacio) para el ID 99
        when(enlaceRepository.findById(99)).thenReturn(Optional.empty());

        // WHEN & THEN: Comprobamos que nuestro servicio lance la excepcion personalizada que creamos
        assertThrows(ResourceNotFoundException.class, () -> enlaceService.buscarPorId(99));
        verify(enlaceRepository, times(1)).findById(99);
    }

    @Test
    void guardarEnlace_DebeRetornarEnlaceGuardado() {
        // GIVEN: Configuramos el mock para que cuando intentemos guardar CUALQUIER enlace, devuelva nuestro enlace de prueba
        when(enlaceRepository.save(any(Enlace.class))).thenReturn(enlace);

        // WHEN
        EnlaceDTO resultado = enlaceService.guardarEnlace(enlaceDTO);

        // THEN
        assertNotNull(resultado);
        assertEquals("Wiki Oficial", resultado.getNombre());
        assertEquals("https://wiki.com", resultado.getUrl());
        verify(enlaceRepository, times(1)).save(any(Enlace.class));
    }

    @Test
    void actualizarEnlace_CuandoExiste_DebeRetornarEnlaceActualizado() {
        // GIVEN: Primero el mock debe encontrar el enlace, y luego debe guardarlo con exito
        when(enlaceRepository.findById(1)).thenReturn(Optional.of(enlace));
        when(enlaceRepository.save(any(Enlace.class))).thenReturn(enlace);

        // WHEN
        EnlaceDTO resultado = enlaceService.actualizarEnlace(1, enlaceDTO);

        // THEN
        assertNotNull(resultado);
        assertEquals("Wiki Oficial", resultado.getNombre());
        verify(enlaceRepository, times(1)).findById(1);
        verify(enlaceRepository, times(1)).save(any(Enlace.class));
    }

    @Test
    void eliminarEnlace_CuandoExiste_DebeLlamarAlDelete() {
        // GIVEN: El mock encuentra el enlace
        when(enlaceRepository.findById(1)).thenReturn(Optional.of(enlace));
        // doNothing() se usa para metodos void (como delete) para decir "no hagas nada, solo asume que salio bien"
        doNothing().when(enlaceRepository).delete(enlace);

        // WHEN
        enlaceService.eliminarEnlace(1);

        // THEN: Verificamos que se haya llamado a la base de datos para borrarlo
        verify(enlaceRepository, times(1)).findById(1);
        verify(enlaceRepository, times(1)).delete(enlace);
    }
}