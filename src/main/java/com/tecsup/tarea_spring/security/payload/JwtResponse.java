package com.tecsup.tarea_spring.security.payload;

import lombok.Data;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema; // Importar

// DTO para la respuesta JWT (después de un login exitoso)
@Data
@Schema(description = "Objeto de respuesta que contiene el token JWT y los datos del usuario.")
public class JwtResponse {
    @Schema(description = "Token de autenticación JWT",
            example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY3ODkwNTYwMCwiZXhwIjoxNjc4OTkxOTk5fQ.signature")
    private String token;
    @Schema(description = "Tipo de token (Bearer por defecto)", example = "Bearer")
    private String type = "Bearer";
    @Schema(description = "ID único del usuario", example = "1")
    private Long id;
    @Schema(description = "Nombre de usuario", example = "admin")
    private String username;
    @Schema(description = "Email del usuario", example = "admin@example.com")
    private String email;
    @Schema(description = "Lista de roles del usuario", example = "[\"ROLE_ADMIN\"]")
    private List<String> roles;

    public JwtResponse(String token, Long id, String username, String email, List<String> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}