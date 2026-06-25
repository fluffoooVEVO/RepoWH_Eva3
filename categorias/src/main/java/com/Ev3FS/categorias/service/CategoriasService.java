package com.Ev3FS.categorias.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.Ev3FS.categorias.DTO.CategoriasDTO;
import com.Ev3FS.categorias.DTO.ProductoExternoDTO;
import com.Ev3FS.categorias.model.Categoria;
import com.Ev3FS.categorias.model.Categorias;
import com.Ev3FS.categorias.repository.CategoriaRepository;
import com.Ev3FS.categorias.repository.CategoriasRepository;

@Service
public class CategoriasService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CategoriasRepository categoriasRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public List<CategoriasDTO> obtenerTodas() {
        List<CategoriasDTO> listaDTOs = new ArrayList<>();
        List<Categorias> categoriasReales = categoriasRepository.findAll();
        for (Categorias c : categoriasReales) {
            listaDTOs.add(convertirADTO(c));
        }
        return listaDTOs;
    }

    public CategoriasDTO buscarPorId(Integer id) {
        Categorias c = categoriasRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró el registro con ID: " + id));
        return convertirADTO(c);
    }

    public CategoriasDTO guardarCategorias(CategoriasDTO dto) {
        Categorias entidadParaGuardar = convertirAEntidad(dto);
        Categorias guardada = categoriasRepository.save(entidadParaGuardar);
        return convertirADTO(guardada);
    }

    public CategoriasDTO actualizarCategorias(Integer id, CategoriasDTO dto) {
        Categorias existente = categoriasRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se puede actualizar. ID " + id + " no encontrado."));
        
        if (dto.getId_categorias_producto() != null) {
            Categoria cat = categoriaRepository.findById(dto.getId_categorias())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + dto.getId_categorias()));
            existente.setCategoria(cat);
        }
        
        if (dto.getId_producto() != null) {
            existente.setIdProducto(dto.getId_producto());
        }
        
        Categorias actualizada = categoriasRepository.save(existente);
        return convertirADTO(actualizada);
    }

    public String eliminarCategorias(Integer id) {
        Categorias categoria = categoriasRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró el registro con ID: " + id));
        String info = "Relación producto-categoría con ID " + id;
        categoriasRepository.delete(categoria);
        return info + " ha sido eliminada exitosamente";
    }

    private Categorias convertirAEntidad(CategoriasDTO dto) {
        Categorias categoria = new Categorias();
        categoria.setIdProductoCategoria(dto.getId_categorias_producto());
        Categoria cat = categoriaRepository.findById(dto.getId_categorias())
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + dto.getId_categorias()));
        categoria.setCategoria(cat);
        categoria.setIdProducto(dto.getId_producto());
        return categoria;
    }

    private CategoriasDTO convertirADTO(Categorias categoria) {
        CategoriasDTO dto = new CategoriasDTO();
        dto.setId_categorias_producto(categoria.getIdProductoCategoria());
        dto.setId_categorias(categoria.getCategoria().getIdCategoria());
        dto.setId_producto(categoria.getIdProducto());
        
        if (categoria.getIdProducto() != null) {
            try {
                ProductoExternoDTO productoRecuperado = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8080/" + categoria.getIdProducto())
                    .retrieve()
                    .bodyToMono(ProductoExternoDTO.class)
                    .block();

                dto.setProducto(productoRecuperado);
                
            } catch (Exception e) {
                dto.setProducto(null); 
            }
        }
        return dto;
    }
}