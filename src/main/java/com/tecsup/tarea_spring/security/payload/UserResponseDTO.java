package com.tecsup.tarea_spring.security.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema; // Importar

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representación de los datos de un usuario para respuesta de API.")
public class UserResponseDTO {
    @Schema(description = "ID único del usuario", example = "1")
    private Long id;
    @Schema(description = "Nombre de usuario único", example = "john.doe")
    private String username;
    @Schema(description = "Email único del usuario", example = "john.doe@example.com")
    private String email;
    @Schema(description = "Lista de roles asignados al usuario", example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]")
    private List<String> roles;
}