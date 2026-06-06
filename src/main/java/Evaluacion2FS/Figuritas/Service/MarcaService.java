package Evaluacion2FS.Figuritas.Service;

import Evaluacion2FS.Figuritas.DTO.MarcaDTO;
import Evaluacion2FS.Figuritas.Exception.ResourceNotFoundException;
import Evaluacion2FS.Figuritas.Model.Marca;
import Evaluacion2FS.Figuritas.Repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// la anotacion service le dice a spring que aqui va toda la logica del negocio
@Service
public class MarcaService {

    // inyectamos el repositorio para poder usar la base de datos
    @Autowired
    private MarcaRepository marcaRepository;

    // este metodo para pasar de un modelo a un dto
    private MarcaDTO convertirADto(Marca marca) {
        MarcaDTO dto = new MarcaDTO();
        dto.setId_marca(marca.getId_marca());
        dto.setNombre(marca.getNombre());
        dto.setDescripcion(marca.getDescripcion());
        return dto;
    }

    // este hace lo contrario pasa de dto a modelo para poder guardarlo en la base de datos
    private Marca convertirAModelo(MarcaDTO dto) {
        Marca marca = new Marca();
        marca.setNombre(dto.getNombre());
        marca.setDescripcion(dto.getDescripcion());
        return marca;
    }

    // aca obtenemos todas las marcas
    public List<MarcaDTO> obtenerTodas() {
        // buscamos todas las marcas en la bd
        List<Marca> listaMarcas = marcaRepository.findAll();
        // creamos una lista vacia para guardar los dtos
        List<MarcaDTO> listaDtos = new ArrayList<>();

        // usamos un for para recorrer la lista y transformar cada marca a dto
        for (Marca marca : listaMarcas) {
            MarcaDTO dto = convertirADto(marca);
            listaDtos.add(dto);
        }
        return listaDtos;
    }

    // buscar una marca por su id
    public MarcaDTO buscarPorId(Integer id) {
        // si no la encuentra lanzamos la excepcion que creamos en el primer commit
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("no se encontro ninguna marca con el id " + id));
        return convertirADto(marca);
    }

    // guardar una marca nueva
    public MarcaDTO guardarMarca(MarcaDTO marcaDTO) {
        Marca nuevaMarca = convertirAModelo(marcaDTO);
        Marca marcaGuardada = marcaRepository.save(nuevaMarca);
        return convertirADto(marcaGuardada);
    }

    // actualizar una marca que ya existe
    public MarcaDTO actualizarMarca(Integer id, MarcaDTO marcaDTO) {
        // primero verificamos que exista
        Marca marcaExistente = marcaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("no se encontro la marca con el id " + id));

        // le pasamos los datos nuevos
        marcaExistente.setNombre(marcaDTO.getNombre());
        marcaExistente.setDescripcion(marcaDTO.getDescripcion());

        // guardamos los cambios
        Marca marcaActualizada = marcaRepository.save(marcaExistente);
        return convertirADto(marcaActualizada);
    }

    // eliminar una marca
    public void eliminarMarca(Integer id) {
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("no se encontro la marca con el id " + id));
        marcaRepository.delete(marca);
    }
}