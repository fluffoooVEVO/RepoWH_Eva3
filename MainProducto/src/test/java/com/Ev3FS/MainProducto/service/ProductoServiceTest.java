package com.Ev3FS.MainProducto.service;

import java.time.LocalDate;
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

import com.Ev3FS.MainProducto.DTO.ProductoDTO;
import com.Ev3FS.MainProducto.exception.ResourceNotFoundException;
import com.Ev3FS.MainProducto.repository.ProductoRepository;

import model.Producto;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;
    private ProductoDTO productoDTO;

    @BeforeEach
    void setUp() {
        producto = new Producto(1, "Figura Test", "Desc", LocalDate.now(), 2);
        
        productoDTO = new ProductoDTO();
        productoDTO.setId_producto(1);
        productoDTO.setNombre("Figura Test");
        productoDTO.setDescripcion("Desc");
        productoDTO.setIdCategoria(2);
    }

    @Test
    void obtenerTodos_DebeRetornarLista() {
        when(productoRepository.findAll()).thenReturn(Arrays.asList(producto));
        List<ProductoDTO> resultado = productoService.obtenerTodos();
        assertFalse(resultado.isEmpty());
        assertEquals("Figura Test", resultado.get(0).getNombre());
    }

    @Test
    void buscarPorId_CuandoExiste_RetornaProducto() {
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        ProductoDTO resultado = productoService.buscarPorId(1);
        assertEquals("Figura Test", resultado.getNombre());
    }

    @Test
    void buscarPorId_CuandoNoExiste_LanzaExcepcion() {
        when(productoRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productoService.buscarPorId(99));
    }

    @Test
    void guardarProducto_DebeRetornarProductoGuardado() {
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        ProductoDTO resultado = productoService.guardarProducto(productoDTO);
        assertEquals("Figura Test", resultado.getNombre());
    }

    @Test
    void eliminarProducto_DebeLlamarDelete() {
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        doNothing().when(productoRepository).delete(producto);
        productoService.eliminarProducto(1);
        verify(productoRepository, times(1)).delete(producto);
    }
}