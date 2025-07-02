package com.tecsup.tarea_spring.excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException; // ¡Importar esta clase!

import java.time.LocalDateTime; // ¡Usar LocalDateTime!
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Maneja la excepción ResourceNotFoundException (si la tienes definida en tu proyecto)
    // Asegúrate de que ResourceNotFoundException exista, si no, puedes comentar o eliminar este método
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetalles> manejarResourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest) {
        ErrorDetalles errorDetalles = new ErrorDetalles(LocalDateTime.now(), exception.getMessage(), webRequest.getDescription(false), null);
        return new ResponseEntity<>(errorDetalles, HttpStatus.NOT_FOUND);
    }

    // Maneja las excepciones de validación (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetalles> manejarMethodArgumentNotValidException(MethodArgumentNotValidException exception, WebRequest webRequest) {
        Map<String, String> erroresDeCampo = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String nombreCampo = ((FieldError) error).getField();
            String mensaje = error.getDefaultMessage();
            erroresDeCampo.put(nombreCampo, mensaje);
        });

        ErrorDetalles errorDetalles = new ErrorDetalles(
                LocalDateTime.now(),
                "Errores de validación", // Mensaje general para errores de validación
                webRequest.getDescription(false),
                erroresDeCampo // Aquí se pasan los errores de campo específicos
        );
        return new ResponseEntity<>(errorDetalles, HttpStatus.BAD_REQUEST);
    }

    // **NUEVO**: Maneja ResponseStatusException (lanzadas con new ResponseStatusException(HttpStatus, "mensaje"))
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDetalles> manejarResponseStatusException(ResponseStatusException exception, WebRequest webRequest) {
        ErrorDetalles errorDetalles = new ErrorDetalles(
                LocalDateTime.now(),
                exception.getReason() != null ? exception.getReason() : "Error en la solicitud", // Usa el 'reason' de la excepción
                webRequest.getDescription(false),
                null // No hay errores de campo específicos aquí
        );
        return new ResponseEntity<>(errorDetalles, exception.getStatusCode()); // Devuelve el HttpStatus de la excepción
    }


    // Maneja otras excepciones generales no capturadas
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetalles> manejarGlobalException(Exception exception, WebRequest webRequest) {
        ErrorDetalles errorDetalles = new ErrorDetalles(
                LocalDateTime.now(),
                exception.getMessage() != null ? exception.getMessage() : "Ocurrió un error interno inesperado",
                webRequest.getDescription(false),
                null
        );
        return new ResponseEntity<>(errorDetalles, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

// **Definición COMPLETA de la clase ErrorDetalles** (Colócala en el mismo archivo o en uno separado en el mismo paquete)
