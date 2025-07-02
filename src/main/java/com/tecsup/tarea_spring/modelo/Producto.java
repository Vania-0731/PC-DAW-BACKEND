package com.tecsup.tarea_spring.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin; // Importar DecimalMin
import jakarta.validation.constraints.NotBlank;  // Importar NotBlank
import jakarta.validation.constraints.NotNull;   // Importar NotNull
import jakarta.validation.constraints.Size;      // Importar Size

@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", length = 100, nullable = false)
    @NotBlank(message = "El nombre del producto no puede estar vacío")
    @Size(min = 3, max = 100, message = "El nombre del producto debe tener entre 3 y 100 caracteres")
    private String nombre;

    @Column(name = "precio", nullable = false)
    @NotNull(message = "El precio del producto no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El precio del producto debe ser mayor que cero")
    private Double precio;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    @NotNull(message = "La categoría del producto no puede ser nula") // Valida que la categoría exista
    private Categoria categoria;

    public Producto() {}

    public Producto(Long id, String nombre, Double precio, Categoria categoria) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}