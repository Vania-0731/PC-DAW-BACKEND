package com.tecsup.tarea_spring.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank; // Importar NotBlank
import jakarta.validation.constraints.Size;   // Importar Size

@Entity
@Table(name = "categorias")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", length = 100, nullable = false)
    @NotBlank(message = "El nombre de la categoría no puede estar vacío") // No puede ser null ni cadena vacía/solo espacios
    @Size(min = 3, max = 100, message = "El nombre de la categoría debe tener entre 3 y 100 caracteres") // Longitud
    private String nombre;

    public Categoria() {}

    public Categoria(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}