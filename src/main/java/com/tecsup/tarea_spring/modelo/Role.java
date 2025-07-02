package com.tecsup.tarea_spring.modelo;

import jakarta.persistence.*;
import lombok.Data; // Para getters, setters, etc. Si no usas Lombok, créalos manualmente.

@Entity
@Table(name = "roles") // Esto creará una tabla llamada 'roles' en tu DB
@Data // Anotación de Lombok para generar getters/setters/equals/hashCode
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false, unique = true) // Por ejemplo: ROLE_USER, ROLE_ADMIN
    private String name;

    public Role() {}

    public Role(String name) {
        this.name = name;
    }
}