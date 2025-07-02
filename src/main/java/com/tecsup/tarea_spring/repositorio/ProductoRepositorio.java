package com.tecsup.tarea_spring.repositorio;

import com.tecsup.tarea_spring.modelo.Producto;
import com.tecsup.tarea_spring.modelo.Categoria; // Importar Categoria
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, Long> {
    // Método para encontrar productos por una categoría específica
    List<Producto> findByCategoria(Categoria categoria);
}