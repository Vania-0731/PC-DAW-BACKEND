package com.tecsup.tarea_spring.modelo;

// REMUEVE: import com.tecsup.tarea_spring.enums.Role; // Ya no importes el enum Role aquí para la relación
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"}),
        @UniqueConstraint(columnNames = {"email"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    // *** CAMBIO CRÍTICO AQUÍ: DE @ElementCollection A @ManyToMany ***
    @ManyToMany(fetch = FetchType.EAGER) // Carga los roles al mismo tiempo que el usuario
    @JoinTable(
            name = "user_roles", // Nombre de la tabla de unión (intermedia)
            joinColumns = @JoinColumn(name = "user_id"), // Columna para el ID del usuario en la tabla de unión
            inverseJoinColumns = @JoinColumn(name = "role_id") // Columna para el ID del rol en la tabla de unión
    )
    private Set<Role> roles = new HashSet<>(); // Ahora es Set<com.tecsup.tarea_spring.modelo.Role>

    // Constructor sin id
    public User(String username, String password, String email, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }
}