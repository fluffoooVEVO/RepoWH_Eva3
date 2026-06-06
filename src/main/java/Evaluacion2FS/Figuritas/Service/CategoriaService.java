package Evaluacion2FS.Figuritas.Service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Evaluacion2FS.Figuritas.DTO.CategoriaDTO;
import Evaluacion2FS.Figuritas.Model.Categoria;
import Evaluacion2FS.Figuritas.Repository.CategoriaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j

@Service

public class CategoriaService {

    @Autowired
    CategoriaRepository categoriaRepository;

    public List<Categoria> ObtenerStatusTrue() {
        // 1- creamos un objeto para llamar al repo
        List<Categoria>lista=categoriaRepository.findByStatusTrue();
        if(lista.isEmpty()){
            //2- si la lista esta vacia por ejemplo dara este error
            throw new RuntimeException("No hay categorías que encontrar");
        }
        // como en el paso 1 ya se esta haciendo referencia al metodo de Repository no hace falta llamarlo completamente
        return lista;
    }
    public List<Categoria>ObtenerStatusFalse(){
        List<Categoria>lista=categoriaRepository.findByStatusFalse();
        if(lista.isEmpty()){
            throw new RuntimeException("No hay categorias que encontrar");
        }
        return lista;
    }


    public CategoriaDTO convertirADTO(Categoria categoria) {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setIdCategoria(categoria.getIdCategoria());
        dto.setNombre(categoria.getNombre());
        dto.setDescripcion(categoria.getDescripcion());
        dto.setStatus(categoria.getStatus());
        return dto;
    }

    public Categoria convertirAEntidad(CategoriaDTO dto) {
    Categoria categoria = new Categoria();
    categoria.setIdCategoria(dto.getIdCategoria());
    categoria.setNombre(dto.getNombre());
    categoria.setDescripcion(dto.getDescripcion());
    categoria.setStatus(dto.getStatus());
    return categoria;
    }

    public List<CategoriaDTO> obtenerTodasDTO() {
        return categoriaRepository.findAll().stream()
            .map(this::convertirADTO)
            .toList();
    }

    public CategoriaDTO obtenerPorID(Integer id){
        log.info("Buscando ID: {}",id);
        Categoria categoria=categoriaRepository.findById(id)
        .orElseThrow(()->{
            log.error("Error:no se encontro la categoria con id{}:",id);
            return new RuntimeException("No se encontro el registro con ID:"+ id);
        });
        log.info("Categoria encontrada exitosamente");
        return convertirADTO(categoria);
    }

    public List<CategoriaDTO> obtenerStatusTrueDTO(){
        List<Categoria> lista = categoriaRepository.findByStatusTrue();
        if (lista.isEmpty()) {
            log.error("Error:no se han encontrado las categorias.La lista esta vacia");
            throw new RuntimeException("No hay categorias que encontrar");
        }
        log.info("Las categorias se han encontrado");
        return lista.stream()
            .map(this::convertirADTO)
            .toList();
    }

    public List<CategoriaDTO>obtenerStatusFalseDTO(){
        List<Categoria>lista=categoriaRepository.findByStatusFalse();
        if(lista.isEmpty()){
            log.error("Error:no se han encontrado las categorias.La lista esta vacia");
            throw  new RuntimeException("No hay categorias que encontrar");
        }
        log.info("Las categorias se han encontrado");
        return lista.stream()
        .map(this::convertirADTO)
        .toList();
    }

    public CategoriaDTO guardarCategoriaDTO(CategoriaDTO dto) {
        log.info("Recibiendo categoria para guardar: {}", dto.getNombre());
        Categoria entidadParaGuardar=convertirAEntidad(dto);
        Categoria guardada=categoriaRepository.save(entidadParaGuardar);
        log.info("Categoría guardada con éxito");
        return convertirADTO(guardada);
    }

    public CategoriaDTO actualizarCategoriaDTO(Integer id, CategoriaDTO dto) {
        log.info("Iniciando actualización de ID: {}", id);
        Categoria existente=categoriaRepository.findById(id)
            .orElseThrow(()-> new RuntimeException("No se puede actualizar. ID " + id + " no encontrado."));
        if (dto.getNombre()!= null){
            existente.setNombre(dto.getNombre());
        }
        if (dto.getDescripcion()!=null)
            {existente.setDescripcion(dto.getDescripcion());}
        if (dto.getStatus()!=null){existente.setStatus(dto.getStatus());
        }
        Categoria actualizada=categoriaRepository.save(existente);
        log.info("Categoría ID {} actualizada correctamente", id);
        return convertirADTO(actualizada);
    }

    public String eliminarCategoriaDTO(Integer id) {
        log.info("Intentando eliminar físicamente la categoría con ID: {}", id);
        // 1. Buscamos la categoría primero para verificar que existe
        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Error: No se puede eliminar. ID {} no encontrado", id);
                return new RuntimeException("No se encontró el registro con ID: " + id);
            });
        // 2. Guardamos el nombre antes de borrarlo para el mensaje final
        String nombreCategoria=categoria.getNombre();
        // 3. Eliminamos de la base de datos
        categoriaRepository.delete(categoria);
        log.info("Categoría '{}' eliminada exitosamente", nombreCategoria);
        // 4. Retornamos el String que tu Controller recibirá como 'mensaje'
        return "La categoria "+nombreCategoria + " ha sido eliminada exitosamente";
    }
}