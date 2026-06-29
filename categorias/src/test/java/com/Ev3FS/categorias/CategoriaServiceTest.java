package com.Ev3FS.categorias;

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

import com.Ev3FS.categorias.DTO.CategoriaDTO;
import com.Ev3FS.categorias.model.Categoria;
import com.Ev3FS.categorias.repository.CategoriaRepository;
import com.Ev3FS.categorias.service.CategoriaService;



// cd "D:\Proyectos vs\Warhammer40K\categorias"
//mvn test -Dtest=CategoriaServiceTest
// COMMANDOS PARA HACER EL TEST
@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoria;
    private CategoriaDTO categoriaDTO;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setIdCategoria(1);
        categoria.setNombre("Coleccion 2027");
        categoria.setDescripcion("Productos proximos");
        categoria.setStatus(true);

        categoriaDTO = new CategoriaDTO();
        categoriaDTO.setIdCategoria(1);
        categoriaDTO.setNombre("Coleccion 2027");
        categoriaDTO.setDescripcion("Productos proximos");
        categoriaDTO.setStatus(true);
    }


    @Test
    void obtenerPorID_deberiaRetornarCategoriaDTO_cuandoExiste() {
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));
        CategoriaDTO resultado = categoriaService.obtenerPorID(1);
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdCategoria()).isEqualTo(1);
        assertThat(resultado.getNombre()).isEqualTo("Coleccion 2027");
        verify(categoriaRepository, times(1)).findById(1);
    }

    @Test
    void obtenerPorID_deberiaLanzarExcepcion_cuandoNoExiste() {
        when(categoriaRepository.findById(100)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> categoriaService.obtenerPorID(100))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("No se encontro el registro con ID:100");
        verify(categoriaRepository, times(1)).findById(100);
    }

    @Test
    void guardarCategoriaDTO_deberiaGuardarYRetornarDTO() {
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);
        CategoriaDTO resultado = categoriaService.guardarCategoriaDTO(categoriaDTO);
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNombre()).isEqualTo("Coleccion 2027");
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void eliminarCategoriaDTO_deberiaEliminarYRetornarMensaje() {
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));
        String resultado = categoriaService.eliminarCategoriaDTO(1);
        assertThat(resultado).contains("Coleccion 2027");
        assertThat(resultado).contains("eliminada exitosamente");
        verify(categoriaRepository, times(1)).delete(categoria);
    }

    @Test
    void eliminarCategoriaDTO_deberiaLanzarExcepcion_cuandoNoExiste() {
        when(categoriaRepository.findById(99)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> categoriaService.eliminarCategoriaDTO(99))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("No se encontró el registro con ID: 99");
        verify(categoriaRepository, never()).delete(any(Categoria.class));
    }

}