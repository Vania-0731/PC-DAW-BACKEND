package com.tecsup.tarea_spring.service;

import com.tecsup.tarea_spring.modelo.Categoria;
import java.util.List;

public interface CategoriaServicio {
    List<Categoria> listarTodasLasCategorias();
    Categoria guardarCategoria(Categoria categoria);
    Categoria obtenerCategoriaPorId(Long id); // Retorna Categoria, el controlador manejará ResponseEntity
    Categoria actualizarCategoria(Long id, Categoria detallesCategoria);
    void eliminarCategoria(Long id);
}