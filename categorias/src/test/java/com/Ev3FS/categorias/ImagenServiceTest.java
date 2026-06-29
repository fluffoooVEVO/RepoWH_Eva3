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

import com.Ev3FS.categorias.DTO.ImagenDTO;
import com.Ev3FS.categorias.model.Imagen;
import com.Ev3FS.categorias.repository.ImagenRepository;
import com.Ev3FS.categorias.service.ImagenService;

@ExtendWith(MockitoExtension.class)
class ImagenServiceTest {

    @Mock
    private ImagenRepository imagenRepository;

    @InjectMocks
    private ImagenService imagenService;

    private Imagen imagen;
    private ImagenDTO imagenDTO;

    @BeforeEach
    void setUp() {
        imagen = new Imagen();
        imagen.setIdImagen(1);
        imagen.setUrl("http://localhost/imagenes/portada.png");
        imagen.setOrden(1);
        imagen.setDescripcion("Imagen de portada");

        imagenDTO = new ImagenDTO();
        imagenDTO.setId(1);
        imagenDTO.setUrl("http://localhost/imagenes/portada.png");
        imagenDTO.setOrden(1);
        imagenDTO.setDescripcion("Imagen de portada");
    }

    @Test
    void obtenerPorIdDTO_deberiaRetornarDTO_cuandoExiste() {
        when(imagenRepository.findById(1)).thenReturn(Optional.of(imagen));

        ImagenDTO resultado = imagenService.obtenerPorIdDTO(1);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1);
        assertThat(resultado.getUrl()).isEqualTo("http://localhost/imagenes/portada.png");
        verify(imagenRepository, times(1)).findById(1);
    }

    @Test
    void obtenerPorIdDTO_deberiaLanzarExcepcion_cuandoNoExiste() {
        when(imagenRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> imagenService.obtenerPorIdDTO(99))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("No se encontró la imagen con ID: 99");

        verify(imagenRepository, times(1)).findById(99);
    }

    @Test
    void guardarImagenDTO_deberiaGuardarYRetornarDTO() {
        when(imagenRepository.save(any(Imagen.class))).thenReturn(imagen);

        ImagenDTO resultado = imagenService.guardarImagenDTO(imagenDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getUrl()).isEqualTo("http://localhost/imagenes/portada.png");
        verify(imagenRepository, times(1)).save(any(Imagen.class));
    }

    @Test
    void actualizarImagenDTO_deberiaActualizarYRetornarDTO() {
        when(imagenRepository.findById(1)).thenReturn(Optional.of(imagen));
        when(imagenRepository.save(any(Imagen.class))).thenReturn(imagen);

        ImagenDTO dtoActualizado = new ImagenDTO();
        dtoActualizado.setUrl("http://localhost/imagenes/nueva.png");
        dtoActualizado.setOrden(2);
        dtoActualizado.setDescripcion("Nueva descripcion");

        ImagenDTO resultado = imagenService.actualizarImagenDTO(1, dtoActualizado);

        assertThat(resultado).isNotNull();
        verify(imagenRepository, times(1)).findById(1);
        verify(imagenRepository, times(1)).save(any(Imagen.class));
    }

    @Test
    void actualizarImagenDTO_deberiaLanzarExcepcion_cuandoNoExiste() {
        when(imagenRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> imagenService.actualizarImagenDTO(99, imagenDTO))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("No se encontró la imagen con ID: 99");

        verify(imagenRepository, never()).save(any(Imagen.class));
    }

    @Test
    void deleteImagen_deberiaEliminarYRetornarMensaje() {
        when(imagenRepository.findById(1)).thenReturn(Optional.of(imagen));

        String resultado = imagenService.deleteImagen(1);

        assertThat(resultado).contains("Imagen eliminada exitosamente con ID: 1");
        verify(imagenRepository, times(1)).delete(imagen);
    }

    @Test
    void deleteImagen_deberiaLanzarExcepcion_cuandoNoExiste() {
        when(imagenRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> imagenService.deleteImagen(99))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("No se encontró la imagen con ID: 99");

        verify(imagenRepository, never()).delete(any(Imagen.class));
    }
}