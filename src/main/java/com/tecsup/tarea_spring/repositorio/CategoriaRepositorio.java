package com.tecsup.tarea_spring.repositorio;

import com.tecsup.tarea_spring.modelo.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepositorio extends JpaRepository<Categoria, Long> {}
