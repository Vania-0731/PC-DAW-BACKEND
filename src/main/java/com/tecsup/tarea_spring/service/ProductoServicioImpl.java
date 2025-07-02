package com.tecsup.tarea_spring.service;

import com.tecsup.tarea_spring.excepciones.ResourceNotFoundException; // ¡Asegúrate de que esta línea esté presente!
import com.tecsup.tarea_spring.modelo.Producto;
import com.tecsup.tarea_spring.repositorio.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Indica que esta clase es un componente de servicio de Spring
public class ProductoServicioImpl implements ProductoServicio {

    @Autowired
    private ProductoRepositorio productoRepository; // Inyecta el repositorio

    @Override
    public List<Producto> listarTodosLosProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Producto obtenerProductoPorId(Long id) {
        // CORRECCIÓN AQUÍ: Usar ResourceNotFoundException
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el ID: " + id));
    }

    @Override
    public Producto actualizarProducto(Long id, Producto detallesProducto) {
        // CORRECCIÓN AQUÍ: Usar ResourceNotFoundException
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el ID: " + id));

        producto.setNombre(detallesProducto.getNombre());
        producto.setPrecio(detallesProducto.getPrecio());
        producto.setCategoria(detallesProducto.getCategoria());

        return productoRepository.save(producto);
    }

    @Override
    public void eliminarProducto(Long id) {
        // CORRECCIÓN AQUÍ: Usar ResourceNotFoundException
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el ID: " + id));
        productoRepository.delete(producto);
    }
}