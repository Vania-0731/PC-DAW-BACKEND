package com.tecsup.tarea_spring.service;

import com.tecsup.tarea_spring.modelo.Producto;
import java.util.List;

public interface ProductoServicio {
    List<Producto> listarTodosLosProductos();
    Producto guardarProducto(Producto producto);
    Producto obtenerProductoPorId(Long id);
    Producto actualizarProducto(Long id, Producto detallesProducto);
    void eliminarProducto(Long id);
}