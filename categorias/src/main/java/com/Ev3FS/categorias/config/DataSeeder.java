package com.Ev3FS.categorias.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.Ev3FS.categorias.model.Categoria;
import com.Ev3FS.categorias.model.Categorias;
import com.Ev3FS.categorias.model.Imagen;
import com.Ev3FS.categorias.repository.CategoriaRepository;
import com.Ev3FS.categorias.repository.CategoriasRepository;
import com.Ev3FS.categorias.repository.ImagenRepository;

import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;

@Slf4j
@Component
@Profile("dev")
public class DataSeeder implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final CategoriasRepository categoriasRepository;
    private final ImagenRepository imagenRepository;
    private final Faker faker = new Faker();

    public DataSeeder(CategoriaRepository categoriaRepository,
        CategoriasRepository categoriasRepository,
        ImagenRepository imagenRepository) {
        this.categoriaRepository = categoriaRepository;
        this.categoriasRepository = categoriasRepository;
        this.imagenRepository = imagenRepository;
    }

    @Override
    public void run(String... args) {
        log.info("Verificando datos existentes...");

        var categoriasExistentes = categoriaRepository.findAll();

        if (categoriasExistentes.isEmpty()) {
            log.info("No hay categorías, creando con Faker...");
            for (int i = 0; i < 5; i++) {
                Categoria categoria = new Categoria();
                categoria.setNombre(faker.commerce().department());
                categoria.setDescripcion(faker.lorem().sentence());
                categoria.setStatus(faker.bool().bool());
                categoriaRepository.save(categoria);
            }
            categoriasExistentes = categoriaRepository.findAll();
        } else {
            log.info("Ya existen {} categorías, se usarán las existentes.", categoriasExistentes.size());
        }

        if (imagenRepository.count() == 0) {
            log.info("Insertando imágenes con Faker...");
            for (int i = 1; i <= 5; i++) {
                Imagen imagen = new Imagen();
                imagen.setUrl(faker.internet().image());
                imagen.setOrden(i);
                imagen.setDescripcion(faker.lorem().sentence());
                imagen.setIdProducto(i);
                imagenRepository.save(imagen);
            }
        } else {
            log.info("Ya existen imágenes, se omite ese seeding.");
        }

        if (categoriasRepository.count() == 0) {
            log.info("Insertando relaciones Producto-Categoria con Faker...");
            for (int i = 1; i <= 5; i++) {
                Categorias categorias = new Categorias();
                categorias.setIdProducto(i);
                categorias.setCategoria(categoriasExistentes.get(i % categoriasExistentes.size()));
                categoriasRepository.save(categorias);
            }
        } else {
            log.info("Ya existen relaciones, se omite ese seeding.");
        }
    }
}