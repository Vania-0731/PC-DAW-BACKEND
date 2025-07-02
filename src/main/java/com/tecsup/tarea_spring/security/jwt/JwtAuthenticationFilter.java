package com.tecsup.tarea_spring.security.jwt;

import com.tecsup.tarea_spring.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Este filtro se ejecuta una vez por cada petición HTTP
// Se encarga de validar el JWT y establecer la autenticación en el contexto de seguridad de Spring
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtGenerator jwtGenerator; // Necesitamos el generador/validador de JWT

    @Autowired
    private CustomUserDetailsService customUserDetailsService; // Necesitamos el servicio para cargar los detalles del usuario

    // *** NUEVA ADICIÓN AQUÍ ***
    // Este método permite al filtro saber si debe o no ejecutarse para una petición específica.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Excluye las rutas de autenticación (login y registro) de este filtro JWT
        // Estas rutas son públicas y no necesitan un token JWT para ser accedidas.
        return request.getServletPath().startsWith("/api/v1/auth/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = getJwtFromRequest(request); // Extrae el JWT de la cabecera
        // Si el token no es nulo y es válido
        if (token != null && jwtGenerator.validateToken(token)) {
            String username = jwtGenerator.getUsernameFromToken(token); // Obtiene el nombre de usuario del token

            // Carga los detalles del usuario
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // Crea un objeto de autenticación
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null, // No se usa la contraseña aquí ya que el usuario ya está autenticado via token
                    userDetails.getAuthorities() // Obtiene los roles/autoridades del usuario
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Establece la autenticación en el contexto de seguridad de Spring
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        // Continúa la cadena de filtros
        filterChain.doFilter(request, response);
    }

    // Método para extraer el JWT de la cabecera "Authorization"
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // Comprueba si la cabecera contiene el prefijo "Bearer " y extrae el token
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Devuelve el token sin el prefijo "Bearer "
        }
        return null;
    }
}