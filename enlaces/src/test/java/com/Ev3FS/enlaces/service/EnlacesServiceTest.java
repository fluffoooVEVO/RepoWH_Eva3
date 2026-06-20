package com.Ev3FS.enlaces.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.Ev3FS.enlaces.DTO.EnlacesDTO;
import com.Ev3FS.enlaces.DTO.ProductoExternoDTO;
import com.Ev3FS.enlaces.exception.ResourceNotFoundException;
import com.Ev3FS.enlaces.model.Enlace;
import com.Ev3FS.enlaces.model.Enlaces;
import com.Ev3FS.enlaces.repository.EnlaceRepository;
import com.Ev3FS.enlaces.repository.EnlacesRepository;

@ExtendWith(MockitoExtension.class)
class EnlacesServiceTest {

    @Mock
    private EnlacesRepository enlacesRepository;

    @Mock
    private EnlaceRepository enlaceRepository;

    // RETURNS_DEEP_STUBS nos permite simular cadenas largas como webClient.build().get().uri()...
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient.Builder webClientBuilder;

    @InjectMocks
    private EnlacesService enlacesService;

    private Enlace enlace;
    private Enlaces enlaces;
    private EnlacesDTO enlacesDTO;

    @BeforeEach
    void setUp() {
        enlace = new Enlace(1, "Wiki", "url");
        enlaces = new Enlaces(1, enlace, 5); // id_producto = 5
        
        enlacesDTO = new EnlacesDTO();
        enlacesDTO.setId_enlace_producto(1);
        enlacesDTO.setId_enlace(1);
        enlacesDTO.setId_producto(5);
    }

    @Test
    void obtenerTodos_DebeRetornarLista() {
        when(enlacesRepository.findAll()).thenReturn(Arrays.asList(enlaces));
        List<EnlacesDTO> resultado = enlacesService.obtenerTodos();
        assertFalse(resultado.isEmpty());
    }

    @Test
    void buscarPorId_CuandoExiste_DebeRetornarRelacion() {
        when(enlacesRepository.findById(1)).thenReturn(Optional.of(enlaces));
        EnlacesDTO resultado = enlacesService.buscarPorId(1);
        assertEquals(5, resultado.getId_producto());
    }

    @Test
    void guardarEnlaces_ConProductoValido_DebeGuardar() {
        // Mockeamos la base de datos de enlaces
        when(enlaceRepository.findById(1)).thenReturn(Optional.of(enlace));
        when(enlacesRepository.save(any(Enlaces.class))).thenReturn(enlaces);
        
        // Mockeamos la respuesta de WebClient simulando que el producto existe
        ProductoExternoDTO productoFalso = new ProductoExternoDTO();
        productoFalso.setId_producto(5);
        productoFalso.setNombre("Figura Space Marine");
        
        when(webClientBuilder.build().get().uri(anyString()).retrieve().bodyToMono(ProductoExternoDTO.class).block())
            .thenReturn(productoFalso);

        EnlacesDTO resultado = enlacesService.guardarEnlaces(enlacesDTO);
        assertNotNull(resultado);
        assertEquals(5, resultado.getId_producto());
    }

    @Test
    void eliminarEnlaces_DebeLlamarDelete() {
        when(enlacesRepository.findById(1)).thenReturn(Optional.of(enlaces));
        doNothing().when(enlacesRepository).delete(enlaces);
        enlacesService.eliminarEnlaces(1);
        verify(enlacesRepository, times(1)).delete(enlaces);
    }
}