package com.tecsup.tarea_spring.security.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.slf4j.Logger; // Importa Logger
import org.slf4j.LoggerFactory; // Importa LoggerFactory

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class); // Añade el logger

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // Esto se ejecuta cuando un usuario no autenticado (o con token inválido)
        // intenta acceder a un endpoint seguro.
        // Aquí enviamos una respuesta 401 Unauthorized.

        logger.error("Unauthorized error: {}", authException.getMessage()); // <-- AÑADE ESTA LÍNEA para que se imprima en la consola
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}