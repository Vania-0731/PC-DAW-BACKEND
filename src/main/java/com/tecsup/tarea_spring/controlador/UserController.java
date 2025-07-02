package com.tecsup.tarea_spring.controlador;

import com.tecsup.tarea_spring.modelo.Role;
import com.tecsup.tarea_spring.modelo.User;
import com.tecsup.tarea_spring.repositorio.RoleRepository;
import com.tecsup.tarea_spring.repositorio.UserRepository;
import com.tecsup.tarea_spring.security.payload.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// --- Importaciones de Swagger/OpenAPI ---
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "https://pc-daw-frontend.vercel.app/")
@Tag(name = "Gestión de Usuarios", description = "Operaciones CRUD y de roles para usuarios del sistema. Algunos accesibles por usuarios, otros solo por administradores.")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    // DTO para la actualización de roles
    @Schema(description = "Objeto para actualizar los roles de un usuario.")
    public static class RolesUpdateRequest {
        @Schema(description = "Lista de nombres de roles (ej. 'ROLE_USER', 'ROLE_ADMIN') a asignar al usuario.",
                example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]")
        private Set<String> roles;

        public Set<String> getRoles() {
            return roles;
        }

        public void setRoles(Set<String> roles) {
            this.roles = roles;
        }
    }

    @Operation(summary = "Obtener todos los usuarios", description = "Recupera una lista de todos los usuarios registrados en el sistema. Requiere rol USER o ADMIN.")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDTO.class)))
    @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT no proporcionado o inválido.",
            content = @Content(mediaType = "text/plain",
                    schema = @Schema(type = "string", example = "No autorizado - Token JWT no proporcionado o inválido."))) // **CORREGIDO**
    @ApiResponse(responseCode = "403", description = "Prohibido - El usuario no tiene el rol USER o ADMIN.",
            content = @Content(mediaType = "text/plain",
                    schema = @Schema(type = "string", example = "Prohibido - El usuario no tiene el rol USER o ADMIN."))) // **CORREGIDO**
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponseDTO> userDTOs = users.stream()
                .map(this::mapUserToUserResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @Operation(summary = "Actualizar roles de un usuario", description = "Actualiza los roles de un usuario específico por su ID. Requiere rol ADMIN.")
    @ApiResponse(responseCode = "200", description = "Roles del usuario actualizados exitosamente.",
            content = @Content(mediaType = "text/plain",
                    schema = @Schema(type = "string", example = "Roles del usuario actualizados exitosamente.")))
    @ApiResponse(responseCode = "400", description = "Solicitud inválida - ID de usuario no válido o roles no proporcionados/inválidos.",
            content = @Content(mediaType = "text/plain",
                    schema = @Schema(type = "string", example = "Debe proporcionar al menos un rol.")))
    @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT no proporcionado o inválido.",
            content = @Content(mediaType = "text/plain",
                    schema = @Schema(type = "string", example = "No autorizado - Token JWT no proporcionado o inválido."))) // **CORREGIDO**
    @ApiResponse(responseCode = "403", description = "Prohibido - El usuario no tiene el rol ADMIN.",
            content = @Content(mediaType = "text/plain",
                    schema = @Schema(type = "string", example = "Prohibido - El usuario no tiene el rol ADMIN."))) // **CORREGIDO**
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado con el ID proporcionado.",
            content = @Content(mediaType = "text/plain",
                    schema = @Schema(type = "string", example = "Usuario no encontrado con ID: 1")))
    @PutMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateUserRoles(
            @Parameter(description = "ID del usuario a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @RequestBody(description = "Objeto con los nuevos roles del usuario", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RolesUpdateRequest.class)))
            @org.springframework.web.bind.annotation.RequestBody RolesUpdateRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));

        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            return new ResponseEntity<>("Debe proporcionar al menos un rol.", HttpStatus.BAD_REQUEST);
        }

        Set<Role> newRoles = request.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol no encontrado: " + roleName)))
                .collect(Collectors.toSet());

        user.setRoles(newRoles);
        userRepository.save(user);
        return ResponseEntity.ok("Roles del usuario actualizados exitosamente.");
    }

    @Operation(summary = "Eliminar un usuario", description = "Elimina un usuario del sistema por su ID. Requiere rol ADMIN.")
    @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente.",
            content = @Content(mediaType = "text/plain",
                    schema = @Schema(type = "string", example = "Usuario eliminado exitosamente."))) // **CORREGIDO**
    @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT no proporcionado o inválido.",
            content = @Content(mediaType = "text/plain",
                    schema = @Schema(type = "string", example = "No autorizado - Token JWT no proporcionado o inválido."))) // **CORREGIDO**
    @ApiResponse(responseCode = "403", description = "Prohibido - El usuario no tiene el rol ADMIN.",
            content = @Content(mediaType = "text/plain",
                    schema = @Schema(type = "string", example = "Prohibido - El usuario no tiene el rol ADMIN."))) // **CORREGIDO**
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado con el ID proporcionado.",
            content = @Content(mediaType = "text/plain",
                    schema = @Schema(type = "string", example = "Usuario no encontrado con ID: 1")))
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(
            @Parameter(description = "ID del usuario a eliminar", required = true, example = "1")
            @PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));

        userRepository.delete(user);
        return ResponseEntity.ok("Usuario eliminado exitosamente.");
    }

    // Método auxiliar (no expuesto directamente en la API)
    private UserResponseDTO mapUserToUserResponseDTO(User user) {
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), roles);
    }
}