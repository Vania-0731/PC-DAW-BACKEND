package com.tecsup.tarea_spring.config;

import com.tecsup.tarea_spring.enums.Role; // Este es tu ENUM (com.tecsup.tarea_spring.enums.Role)
import com.tecsup.tarea_spring.modelo.User;
import com.tecsup.tarea_spring.repositorio.RoleRepository;
import com.tecsup.tarea_spring.repositorio.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            // 1. Crear o asegurar que los roles base existan en la tabla 'roles'
            // Usamos .name() para obtener el String del enum
            com.tecsup.tarea_spring.modelo.Role adminModelRole = roleRepository.findByName(Role.ROLE_ADMIN.name())
                    .orElseGet(() -> {
                        com.tecsup.tarea_spring.modelo.Role newRole = new com.tecsup.tarea_spring.modelo.Role(Role.ROLE_ADMIN.name());
                        return roleRepository.save(newRole);
                    });

            com.tecsup.tarea_spring.modelo.Role userModelRole = roleRepository.findByName(Role.ROLE_USER.name())
                    .orElseGet(() -> {
                        com.tecsup.tarea_spring.modelo.Role newRole = new com.tecsup.tarea_spring.modelo.Role(Role.ROLE_USER.name());
                        return roleRepository.save(newRole);
                    });

            // 2. Verificar si no hay usuarios y, si es así, crear el usuario admin por defecto
            if (userRepository.count() == 0) {
                System.out.println("No users found. Creating a default admin user...");

                // Crear un conjunto de roles para el nuevo usuario
                Set<com.tecsup.tarea_spring.modelo.Role> roles = new HashSet<>();
                roles.add(adminModelRole); // Asignamos el rol de ADMIN

                // Crear el usuario usando el constructor que acepta username, password, email y Set<Role>
                User adminUser = new User(
                        "Vania",
                        passwordEncoder.encode("Vania123*"), // ¡Cambia "adminpassword" por una contraseña segura!
                        "sonalysifuentes@gmail.com",
                        roles // Pasamos el set de roles
                );

                userRepository.save(adminUser);
                System.out.println("Default admin user 'admin' created successfully!");

            } else {
                System.out.println("Users already exist. Skipping default admin creation.");
            }
        };
    }
}