package com.tecsup.tarea_spring.service; // Nota: Si tu paquete es 'servicio', mantén 'servicio'

import com.tecsup.tarea_spring.excepciones.ResourceNotFoundException; // ¡Importar ResourceNotFoundException!
import com.tecsup.tarea_spring.modelo.Categoria;
import com.tecsup.tarea_spring.modelo.Producto;
import com.tecsup.tarea_spring.repositorio.CategoriaRepositorio;
import com.tecsup.tarea_spring.repositorio.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaServicioImpl implements CategoriaServicio {

    @Autowired
    private CategoriaRepositorio categoriaRepository;

    @Autowired
    private ProductoRepositorio productoRepository;

    @Override
    public List<Categoria> listarTodasLasCategorias() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria guardarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    public Categoria obtenerCategoriaPorId(Long id) {
        // CORRECCIÓN AQUÍ: Volver a lanzar ResourceNotFoundException
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con el ID: " + id));
    }

    @Override
    public Categoria actualizarCategoria(Long id, Categoria detallesCategoria) {
        // CORRECCIÓN AQUÍ: Volver a lanzar ResourceNotFoundException
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con el ID: " + id));
        categoria.setNombre(detallesCategoria.getNombre());
        return categoriaRepository.save(categoria);
    }

    @Override
    @Transactional // Asegura que toda la operación sea atómica
    public void eliminarCategoria(Long id) {
        // CORRECCIÓN AQUÍ: Volver a lanzar ResourceNotFoundException
        Categoria categoriaAEliminar = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con el ID: " + id));

        // 1. Encontrar la categoría por defecto
        Categoria categoriaPorDefecto = categoriaRepository.findById(1L)
                .orElseGet(() -> {
                    Categoria nuevaDefault = new Categoria();
                    nuevaDefault.setNombre("Sin Categoría");
                    return categoriaRepository.save(nuevaDefault);
                });

        if (categoriaAEliminar.getId().equals(categoriaPorDefecto.getId())) {
            throw new IllegalStateException("No se puede eliminar la categoría por defecto.");
        }

        List<Producto> productosAsociados = productoRepository.findByCategoria(categoriaAEliminar);
        for (Producto producto : productosAsociados) {
            producto.setCategoria(categoriaPorDefecto);
            productoRepository.save(producto);
        }

        categoriaRepository.delete(categoriaAEliminar);
    }
}