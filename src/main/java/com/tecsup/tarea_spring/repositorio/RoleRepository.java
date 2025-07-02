package com.tecsup.tarea_spring.repositorio;

import com.tecsup.tarea_spring.modelo.Role; // Importa la nueva entidad Role
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name); // Método para buscar un rol por su nombre
}