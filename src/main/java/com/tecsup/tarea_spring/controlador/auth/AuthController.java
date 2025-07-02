package com.tecsup.tarea_spring.controlador.auth;

import com.tecsup.tarea_spring.modelo.Role;
import com.tecsup.tarea_spring.modelo.User;
import com.tecsup.tarea_spring.repositorio.RoleRepository;
import com.tecsup.tarea_spring.repositorio.UserRepository;
import com.tecsup.tarea_spring.security.jwt.JwtGenerator;
import com.tecsup.tarea_spring.security.payload.JwtResponse;
import com.tecsup.tarea_spring.security.payload.LoginRequest;
import com.tecsup.tarea_spring.security.payload.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// --- Importaciones de Swagger/OpenAPI ---
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody; // Importa el RequestBody de Swagger para documentación


@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "https://pc-daw-frontend.vercel.app/")
@Tag(name = "Autenticación", description = "Endpoints para el registro y el inicio de sesión de usuarios.")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private RoleRepository roleRepository;

    @Operation(summary = "Iniciar sesión de usuario", description = "Autentica a un usuario y devuelve un token JWT para acceder a recursos protegidos.")
    @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso, devuelve el token JWT.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = JwtResponse.class)))
    @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. credenciales faltantes).",
            content = @Content(mediaType = "text/plain", // Especificado como texto plano
                    schema = @Schema(type = "string", example = "Bad Request: Credenciales faltantes."))) // Con un ejemplo
    @ApiResponse(responseCode = "401", description = "Credenciales inválidas (nombre de usuario o contraseña incorrectos).",
            content = @Content(mediaType = "text/plain", // Especificado como texto plano
                    schema = @Schema(type = "string", example = "Unauthorized: Credenciales inválidas."))) // Con un ejemplo
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(
            @RequestBody(description = "Objeto con las credenciales de inicio de sesión", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequest.class)))
            @org.springframework.web.bind.annotation.RequestBody @Valid LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtGenerator.generateToken(authentication);

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Error: Usuario no encontrado después de autenticación."));

        return ResponseEntity.ok(new JwtResponse(token, user.getId(), user.getUsername(), user.getEmail(), roles));
    }

    @Operation(summary = "Registrar un nuevo usuario", description = "Registra un nuevo usuario en el sistema. Los roles pueden ser asignados, por defecto se asigna 'ROLE_USER'.")
    @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente.",
            content = @Content(mediaType = "text/plain", // Especificado como texto plano
                    schema = @Schema(type = "string", example = "Usuario registrado exitosamente!"))) // Con un ejemplo
    @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. nombre de usuario o email ya existen, validación de campos fallida).",
            content = @Content(mediaType = "text/plain", // Especificado como texto plano
                    schema = @Schema(type = "string", example = "Nombre de usuario ya existe!"))) // Con un ejemplo
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestBody(description = "Objeto con los datos del nuevo usuario para registrar", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegisterRequest.class)))
            @org.springframework.web.bind.annotation.RequestBody @Valid RegisterRequest registerRequest) {

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return new ResponseEntity<>("Nombre de usuario ya existe!", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new ResponseEntity<>("Email ya existe!", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());

        Set<Role> roles = new HashSet<>();

        if (registerRequest.getRoles() == null || registerRequest.getRoles().isEmpty()) {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Error: Rol 'ROLE_USER' no encontrado. Asegúrate de que exista en la DB."));
            roles.add(userRole);
        } else {
            registerRequest.getRoles().forEach(roleStr -> {
                switch (roleStr.toLowerCase()) {
                    case "admin":
                        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                                .orElseThrow(() -> new RuntimeException("Error: Rol 'ROLE_ADMIN' no encontrado. Asegúrate de que exista en la DB."));
                        roles.add(adminRole);
                        break;
                    case "user":
                        Role userRole = roleRepository.findByName("ROLE_USER")
                                .orElseThrow(() -> new RuntimeException("Error: Rol 'ROLE_USER' no encontrado. Asegúrate de que exista en la DB."));
                        roles.add(userRole);
                        break;
                    default:
                        Role defaultRole = roleRepository.findByName("ROLE_USER")
                                .orElseThrow(() -> new RuntimeException("Error: Rol por defecto 'ROLE_USER' no encontrado."));
                        roles.add(defaultRole);
                }
            });
        }
        user.setRoles(roles);

        userRepository.save(user);

        return new ResponseEntity<>("Usuario registrado exitosamente!", HttpStatus.OK);
    }
}