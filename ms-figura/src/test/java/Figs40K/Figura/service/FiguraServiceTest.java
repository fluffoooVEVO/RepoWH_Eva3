package Figs40K.Figura.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import Figs40K.Figura.DTO.EdicionExternoDTO;
import Figs40K.Figura.DTO.FiguraConEdicionDTO;
import Figs40K.Figura.DTO.FiguraDTO;
import Figs40K.Figura.client.EdicionClient;
import Figs40K.Figura.model.Figura;
import Figs40K.Figura.repository.FiguraRepository;

@ExtendWith(MockitoExtension.class)
public class FiguraServiceTest {

    @Mock
    private FiguraRepository figuraRepository;

    // Antes se mockeaba EdicionRepository (acceso directo a BD).
    // Ahora la edicion vive en otro microservicio: se mockea el cliente WebClient.
    @Mock
    private EdicionClient edicionClient;

    @InjectMocks
    private FiguraService figuraService;

    private Figura figuraEjemplo;

    @BeforeEach
    void setUp() {
        figuraEjemplo = new Figura();
        figuraEjemplo.setId_figura(1);
        figuraEjemplo.setNombre("Space Marine Intercessor");
        figuraEjemplo.setDescripcion("Miniatura de élite de los Space Marines");
        figuraEjemplo.setUrl("https://ejemplo.com/intercessor.jpg");
        figuraEjemplo.setId_edicion(1);
    }

    @Test
    void obtenerPorId_deberiaRetornarFigura_cuandoExiste() {
        // Given
        when(figuraRepository.findById(1)).thenReturn(Optional.of(figuraEjemplo));

        // When
        Figura resultado = figuraService.obtenerPorId(1);

        // Then
        assertEquals("Space Marine Intercessor", resultado.getNombre());
        assertEquals(1, resultado.getId_figura());
        verify(figuraRepository).findById(1);
    }

    @Test
    void obtenerPorId_deberiaLanzarExcepcion_cuandoNoExiste() {
        // Given
        when(figuraRepository.findById(99)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseStatusException.class, () -> {
            figuraService.obtenerPorId(99);
        });
        verify(figuraRepository).findById(99);
    }

    @Test
    void guardarFigura_deberiaGuardarYRetornarFigura() {
        // Given
        when(figuraRepository.save(figuraEjemplo)).thenReturn(figuraEjemplo);

        // When
        Figura resultado = figuraService.guardarFigura(figuraEjemplo);

        // Then
        assertEquals("Space Marine Intercessor", resultado.getNombre());
        verify(figuraRepository, times(1)).save(figuraEjemplo);
    }

    @Test
    void convertirAEntidad_deberiaAsignarEdicionCorrectamente() {
        // Given
        FiguraDTO dto = new FiguraDTO();
        dto.setNombre("Ork Warboss");
        dto.setDescripcion("Líder brutal de una horda Ork");
        dto.setUrl("https://ejemplo.com/warboss.jpg");
        dto.setId_edicion(1);

        EdicionExternoDTO edicionExterna = new EdicionExternoDTO();
        edicionExterna.setId_edicion(1);
        edicionExterna.setNombre("10ma Edición");
        when(edicionClient.obtenerEdicion(1)).thenReturn(edicionExterna);

        // When
        Figura resultado = figuraService.convertirAEntidad(dto);

        // Then
        assertEquals("Ork Warboss", resultado.getNombre());
        assertEquals(1, resultado.getId_edicion());
        verify(edicionClient).obtenerEdicion(1);
    }

    @Test
    void convertirAEntidad_deberiaLanzarExcepcion_cuandoEdicionNoExiste() {
        // Given
        FiguraDTO dto = new FiguraDTO();
        dto.setNombre("Ork Warboss");
        dto.setId_edicion(99);

        when(edicionClient.obtenerEdicion(99))
            .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la edicion"));

        // When & Then
        assertThrows(ResponseStatusException.class, () -> {
            figuraService.convertirAEntidad(dto);
        });
    }

    @Test
    void obtenerPorEdicion_deberiaRetornarListaDeFiguras() {
        // Given
        when(figuraRepository.findByIdEdicion(1)).thenReturn(List.of(figuraEjemplo));

        // When
        List<FiguraDTO> resultado = figuraService.obtenerPorEdicion(1);

        // Then
        assertEquals(1, resultado.size());
        assertEquals("Space Marine Intercessor", resultado.get(0).getNombre());
        verify(figuraRepository).findByIdEdicion(1);
    }
    // --- Tests para obtenerFiguraConEdicion (combina datos locales con ms-edicion via WebClient) ---

    @Test
    void obtenerFiguraConEdicion_deberiaCombinarDatosLocalesYRemotos_cuandoTodoExiste() {
        // Given
        when(figuraRepository.findById(1)).thenReturn(Optional.of(figuraEjemplo));

        EdicionExternoDTO edicionExterna = new EdicionExternoDTO();
        edicionExterna.setId_edicion(1);
        edicionExterna.setNombre("Indomitus");
        edicionExterna.setDescripcion("Edicion de inicio de la 9na edicion");
        edicionExterna.setStatus(true);
        when(edicionClient.obtenerEdicion(1)).thenReturn(edicionExterna);

        // When
        FiguraConEdicionDTO resultado = figuraService.obtenerFiguraConEdicion(1);

        // Then
        assertEquals("Space Marine Intercessor", resultado.getNombre());
        assertEquals(1, resultado.getId_figura());
        assertEquals("Indomitus", resultado.getEdicion().getNombre());
        assertEquals(1, resultado.getEdicion().getId_edicion());
        verify(figuraRepository).findById(1);
        verify(edicionClient).obtenerEdicion(1);
    }

    @Test
    void obtenerFiguraConEdicion_deberiaLanzarExcepcion_cuandoFiguraNoExiste() {
        // Given
        when(figuraRepository.findById(99)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseStatusException.class, () -> {
            figuraService.obtenerFiguraConEdicion(99);
        });
        verify(figuraRepository).findById(99);
        verify(edicionClient, times(0)).obtenerEdicion(anyInt());
    }

    @Test
    void obtenerFiguraConEdicion_deberiaPropagar404_cuandoEdicionNoExisteEnMsEdicion() {
        // Given
        when(figuraRepository.findById(1)).thenReturn(Optional.of(figuraEjemplo));
        when(edicionClient.obtenerEdicion(1))
            .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la Edicion con id 1 en ms-edicion"));

        // When & Then
        assertThrows(ResponseStatusException.class, () -> {
            figuraService.obtenerFiguraConEdicion(1);
        });
        verify(figuraRepository).findById(1);
        verify(edicionClient).obtenerEdicion(1);
    }
}
