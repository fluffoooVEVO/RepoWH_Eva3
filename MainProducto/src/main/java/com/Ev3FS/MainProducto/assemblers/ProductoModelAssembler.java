package com.Ev3FS.MainProducto.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import com.Ev3FS.MainProducto.DTO.ProductoDTO;
import com.Ev3FS.MainProducto.controller.ProductoController;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<ProductoDTO, EntityModel<ProductoDTO>> {
    @Override
    public EntityModel<ProductoDTO> toModel(ProductoDTO producto) {
        return EntityModel.of(producto,
            linkTo(methodOn(ProductoController.class).obtenerProducto(producto.getId_producto())).withSelfRel(),
            linkTo(methodOn(ProductoController.class).listarProductos()).withRel("productos"),
            linkTo(methodOn(ProductoController.class).crearProducto(producto)).withRel("agregar-producto")
        );
    }
}