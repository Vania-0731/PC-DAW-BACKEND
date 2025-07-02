package com.tecsup.tarea_spring.excepciones;

import java.time.LocalDateTime; // ¡Importante: Usar LocalDateTime!
import java.util.Map; // Importar para el mapa de errores de campo

public class ErrorDetalles {
    private LocalDateTime timestamp; // Cambiado a LocalDateTime
    private String message;         // Renombrado de 'mensaje' a 'message' para consistencia
    private String details;
    private Map<String, String> fieldErrors; // Nuevo campo para errores de validación

    // Constructor actualizado para incluir fieldErrors
    public ErrorDetalles(LocalDateTime timestamp, String message, String details, Map<String, String> fieldErrors) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
        this.fieldErrors = fieldErrors;
    }

    // Getters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }


}