package com.tecsup.tarea_spring.security.payload;

import lombok.Data;
import java.util.Set;
import io.swagger.v3.oas.annotations.media.Schema;
// ¡Asegúrate de que estas importaciones estén activas y NO comentadas!
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


// DTO para la solicitud de registro de usuario
@Data
@Schema(description = "Objeto de solicitud para registrar un nuevo usuario.")
public class RegisterRequest {
    @Schema(description = "Nombre de usuario único para el nuevo usuario", example = "newuser")
    @NotBlank(message = "El nombre de usuario no puede estar vacío.") // ¡Descomentado y activo!
    @Size(min = 3, max = 20, message = "El nombre de usuario debe tener entre 3 y 20 caracteres.") // ¡Descomentado y activo!
    private String username;

    @Schema(description = "Contraseña para el nuevo usuario", example = "securepassword")
    @NotBlank(message = "La contraseña no puede estar vacía.") // ¡Descomentado y activo!
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.") // ¡Descomentado y activo!
    private String password;

    @Schema(description = "Email único del nuevo usuario", example = "newuser@example.com")
    @NotBlank(message = "El email no puede estar vacío.") // ¡Descomentado y activo!
    @Email(message = "El formato del email no es válido.") // ¡Descomentado y activo!
    private String email;

    @Schema(description = "Roles opcionales para asignar al nuevo usuario (ej. 'ADMIN', 'USER'). Si está vacío, se asigna 'ROLE_USER'.",
            example = "[\"ADMIN\"]")
    private Set<String> roles;
}