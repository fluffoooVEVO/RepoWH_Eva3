package com.Ev3FS.categorias.service;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.Ev3FS.categorias.DTO.CategoriasDTO;
import com.Ev3FS.categorias.model.Categoria;
import com.Ev3FS.categorias.model.Categorias;
import com.Ev3FS.categorias.repository.CategoriaRepository;
import com.Ev3FS.categorias.repository.CategoriasRepository;

@ExtendWith(MockitoExtension.class)
class CategoriasServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private CategoriasRepository categoriasRepository;

    @InjectMocks
    private CategoriasService categoriasService;

    private Categoria categoria;
    private Categorias categorias;
    private CategoriasDTO categoriasDTO;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setIdCategoria(1);
        categoria.setNombre("Coleccion 2027");

        categorias = new Categorias();
        categorias.setIdProductoCategoria(1);
        categorias.setCategoria(categoria);
        categorias.setIdProducto(10);

        categoriasDTO = new CategoriasDTO();
        categoriasDTO.setId_categorias_producto(1);
        categoriasDTO.setId_categorias(1);
        categoriasDTO.setId_producto(10);
    }

    @Test
    void obtenerPorID_deberiaRetornarDTO_cuandoExiste() {
        when(categoriasRepository.findById(1)).thenReturn(Optional.of(categorias));

        CategoriasDTO resultado = categoriasService.obtenerPorID(1);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId_categorias_producto()).isEqualTo(1);
        assertThat(resultado.getId_producto()).isEqualTo(10);
        assertThat(resultado.getId_categorias()).isEqualTo(1);
        verify(categoriasRepository, times(1)).findById(1);
    }

    @Test
    void obtenerPorID_deberiaLanzarExcepcion_cuandoNoExiste() {
        when(categoriasRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoriasService.obtenerPorID(99))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("No se encontró el registro con ID: 99");

        verify(categoriasRepository, times(1)).findById(99);
    }

    @Test
    void guardarCategorias_deberiaGuardarYRetornarDTO() {
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));
        when(categoriasRepository.save(any(Categorias.class))).thenReturn(categorias);

        CategoriasDTO resultado = categoriasService.guardarCategorias(categoriasDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId_producto()).isEqualTo(10);
        verify(categoriaRepository, times(1)).findById(1);
        verify(categoriasRepository, times(1)).save(any(Categorias.class));
    }

    @Test
    void guardarCategorias_deberiaLanzarExcepcion_cuandoCategoriaNoExiste() {
        when(categoriaRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoriasService.guardarCategorias(categoriasDTO))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Categoría no encontrada con ID: 1");

        verify(categoriasRepository, never()).save(any(Categorias.class));
    }

    @Test
    void eliminarCategorias_deberiaEliminarYRetornarMensaje() {
        when(categoriasRepository.findById(1)).thenReturn(Optional.of(categorias));

        String resultado = categoriasService.eliminarCategorias(1);

        assertThat(resultado).contains("eliminada exitosamente");
        verify(categoriasRepository, times(1)).delete(categorias);
    }

    @Test
    void eliminarCategorias_deberiaLanzarExcepcion_cuandoNoExiste() {
        when(categoriasRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoriasService.eliminarCategorias(99))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("No se encontró el registro con ID: 99");

        verify(categoriasRepository, never()).delete(any(Categorias.class));
    }
}