package com.tecsup.tarea_spring.security.payload;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank; // ¡Asegúrate de que esto esté aquí y NO comentado!

// DTO para la solicitud de inicio de sesión
@Data
@Schema(description = "Objeto de solicitud para iniciar sesión.")
public class LoginRequest {
    @NotBlank(message = "El nombre de usuario no puede estar vacío.") // ¡Descomentado y activo!
    @Schema(description = "Nombre de usuario para el inicio de sesión", example = "admin")
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacía.") // ¡Descomentado y activo!
    @Schema(description = "Contraseña para el inicio de sesión", example = "password123")
    private String password;
}