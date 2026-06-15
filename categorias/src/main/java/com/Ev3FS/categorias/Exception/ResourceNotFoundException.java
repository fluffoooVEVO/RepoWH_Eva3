package com.Ev3FS.categorias.Exception;

// extendemos de RuntimeException para poder lanzarla cuando queramos
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }
}