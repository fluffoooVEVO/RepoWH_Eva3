package Exception;

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

    // este metodo ataja los errores de validacion
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarErroresDeValidacion(MethodArgumentNotValidException excepcion) {
        Map<String, String> listaDeErrores = new HashMap<>();
        List<ObjectError> errores = excepcion.getBindingResult().getAllErrors();
        for (ObjectError error : errores) {
            String nombreDelCampo = ((FieldError) error).getField();
            String mensajeDeError = error.getDefaultMessage();
            listaDeErrores.put(nombreDelCampo, mensajeDeError);
        }
        return new ResponseEntity<>(listaDeErrores, HttpStatus.BAD_REQUEST);
    }

    // este metodo ataja cuando buscamos un recurso que no existe
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> manejarRecursoNoEncontrado(ResourceNotFoundException excepcion) {
        Map<String, String> respuestaError = new HashMap<>();
        respuestaError.put("error", excepcion.getMessage());
        return new ResponseEntity<>(respuestaError, HttpStatus.NOT_FOUND);
    }
}
