package com.Ev3FS.Figura.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.Ev3FS.Figura.DTO.FigurasDTO;
import com.Ev3FS.Figura.DTO.ProductoExternoDTO;
import com.Ev3FS.Figura.client.ProductoClient;
import com.Ev3FS.Figura.model.Figura;
import com.Ev3FS.Figura.model.Figuras;
import com.Ev3FS.Figura.repository.FiguraRepository;
import com.Ev3FS.Figura.repository.FigurasRepository;

@ExtendWith(MockitoExtension.class)
public class FigurasServiceTest {

    @Mock
    private FigurasRepository figurasRepository;

    @Mock
    private FiguraRepository figuraRepository;

    // Antes no existia relacion con Producto. Ahora se valida via WebClient,
    // igual que EdicionClient en FiguraService: se mockea el cliente, no una BD.
    @Mock
    private ProductoClient productoClient;

    @InjectMocks
    private FigurasService figurasService;

    private Figura figuraEjemplo;
    private Figuras figurasEjemplo;

    @BeforeEach
    void setUp() {
        figuraEjemplo = new Figura();
        figuraEjemplo.setId_figura(1);
        figuraEjemplo.setNombre("Space Marine Intercessor");
        figuraEjemplo.setDescripcion("Miniatura de élite de los Space Marines");

        figurasEjemplo = new Figuras();
        figurasEjemplo.setId_producto_figura(1);
        figurasEjemplo.setFigura(figuraEjemplo);
        figurasEjemplo.setId_producto(1);
    }

    @Test
    void obtenerTodos_deberiaRetornarListaDeFigurasDTO() {
        // Given
        when(figurasRepository.findAll()).thenReturn(List.of(figurasEjemplo));

        // When
        List<FigurasDTO> resultado = figurasService.obtenerTodos();

        // Then
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getId_figura());
        verify(figurasRepository).findAll();
    }

    @Test
    void guardarFigura_deberiaGuardarYRetornarDTO() {
        // Given
        FigurasDTO dto = new FigurasDTO();
        dto.setId_figura(1);
        dto.setId_producto(1);

        ProductoExternoDTO productoExterno = new ProductoExternoDTO();
        productoExterno.setId_producto(1);
        productoExterno.setNombre("Caja Indomitus");

        when(figuraRepository.findById(1)).thenReturn(Optional.of(figuraEjemplo));
        when(productoClient.obtenerProducto(1)).thenReturn(productoExterno);
        when(figurasRepository.save(any(Figuras.class)))
            .thenReturn(figurasEjemplo);

        // When
        FigurasDTO resultado = figurasService.guardarFigura(dto);

        // Then
        assertEquals(1, resultado.getId_figura());
        verify(figuraRepository).findById(1);
        verify(productoClient).obtenerProducto(1);
        verify(figurasRepository, times(1)).save(any(Figuras.class));
    }

    @Test
    void guardarFigura_deberiaLanzarExcepcion_cuandoProductoNoExiste() {
        // Given
        FigurasDTO dto = new FigurasDTO();
        dto.setId_figura(1);
        dto.setId_producto(99);

        when(figuraRepository.findById(1)).thenReturn(Optional.of(figuraEjemplo));
        when(productoClient.obtenerProducto(99))
            .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el Producto con id 99"));

        // When & Then
        assertThrows(ResponseStatusException.class, () -> {
            figurasService.guardarFigura(dto);
        });
        verify(productoClient).obtenerProducto(99);
        // Si Producto no existe, no debe llegar a guardar nada en la BD local.
        verify(figurasRepository, times(0)).save(any(Figuras.class));
    }

    @Test
    void obtenerPorID_deberiaRetornarDTO_cuandoExiste() {
        // Given
        when(figurasRepository.findById(1)).thenReturn(Optional.of(figurasEjemplo));

        // When
        FigurasDTO resultado = figurasService.obtenerPorID(1);

        // Then
        assertEquals(1, resultado.getId_producto_figura());
        assertEquals(1, resultado.getId_figura());
        verify(figurasRepository).findById(1);
    }

    @Test
    void obtenerPorID_deberiaLanzarExcepcion_cuandoNoExiste() {
        // Given
        when(figurasRepository.findById(99)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            figurasService.obtenerPorID(99);
        });
    }

    @Test
    void eliminarFigura_deberiaEliminar_cuandoExiste() {
        // Given
        when(figurasRepository.existsById(1)).thenReturn(true);

        // When
        String resultado = figurasService.eliminarFigura(1);

        // Then
        assertEquals("Figura eliminada exitosamente", resultado);
        verify(figurasRepository).deleteById(1);
    }

    @Test
    void eliminarFigura_deberiaLanzarExcepcion_cuandoNoExiste() {
        // Given
        when(figurasRepository.existsById(99)).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            figurasService.eliminarFigura(99);
        });
    }

    @Test
    void actualizarFigura_deberiaActualizarYRetornarDTO() {
        // Given
        FigurasDTO dto = new FigurasDTO();
        dto.setId_figura(1);
        dto.setId_producto(1);

        ProductoExternoDTO productoExterno = new ProductoExternoDTO();
        productoExterno.setId_producto(1);

        when(figurasRepository.findById(1)).thenReturn(Optional.of(figurasEjemplo));
        when(figuraRepository.findById(1)).thenReturn(Optional.of(figuraEjemplo));
        when(productoClient.obtenerProducto(1)).thenReturn(productoExterno);
        when(figurasRepository.save(figurasEjemplo)).thenReturn(figurasEjemplo);

        // When
        FigurasDTO resultado = figurasService.actualizarFigura(1, dto);

        // Then
        assertEquals(1, resultado.getId_figura());
        verify(productoClient).obtenerProducto(1);
        verify(figurasRepository).save(figurasEjemplo);
    }

    @Test
    void actualizarFigura_deberiaLanzarExcepcion_cuandoProductoNoExiste() {
        // Given
        FigurasDTO dto = new FigurasDTO();
        dto.setId_figura(1);
        dto.setId_producto(99);

        when(figurasRepository.findById(1)).thenReturn(Optional.of(figurasEjemplo));
        when(figuraRepository.findById(1)).thenReturn(Optional.of(figuraEjemplo));
        when(productoClient.obtenerProducto(99))
            .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el Producto con id 99"));

        // When & Then
        assertThrows(ResponseStatusException.class, () -> {
            figurasService.actualizarFigura(1, dto);
        });
        verify(productoClient).obtenerProducto(99);
    }
}
