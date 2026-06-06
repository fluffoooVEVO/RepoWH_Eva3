package Evaluacion2FS.Figuritas.Exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // este metodo ataja los errores de validacion (por ejemplo cuando dejan un nombre en blanco)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarErroresDeValidacion(MethodArgumentNotValidException excepcion) {
        
        Map<String, String> listaDeErrores = new HashMap<>();
        
        // sacamos todos los errores que encontro spring
        List<ObjectError> errores = excepcion.getBindingResult().getAllErrors();
        
        // recorremos la lista con un for
        for (ObjectError error : errores) {
            String nombreDelCampo = ((FieldError) error).getField();
            String mensajeDeError = error.getDefaultMessage();
            
            // guardamos el nombre del campo y su error en el map
            listaDeErrores.put(nombreDelCampo, mensajeDeError);
        }
        
        return new ResponseEntity<>(listaDeErrores, HttpStatus.BAD_REQUEST);
    }

    // este metodo ataja cuando buscamos una marca o enlace que no existe
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> manejarRecursoNoEncontrado(ResourceNotFoundException excepcion) {
        Map<String, String> respuestaError = new HashMap<>();
        respuestaError.put("error", excepcion.getMessage());
        return new ResponseEntity<>(respuestaError, HttpStatus.NOT_FOUND);
    }
}