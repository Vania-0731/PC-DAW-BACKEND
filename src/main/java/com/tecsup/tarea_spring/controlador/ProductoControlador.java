package com.tecsup.tarea_spring.controlador;

import com.tecsup.tarea_spring.modelo.Producto;
import com.tecsup.tarea_spring.service.ProductoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Importaciones de Swagger/OpenAPI
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.tecsup.tarea_spring.excepciones.ErrorDetalles;

// ¡IMPORTACIÓN NECESARIA PARA @PreAuthorize!
import org.springframework.security.access.prepost.PreAuthorize;


@RestController
@RequestMapping("/api/v1/productos")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Productos", description = "Operaciones de gestión de productos")
public class ProductoControlador {

    @Autowired
    private ProductoServicio productoServicio;

    @Operation(
            summary = "Listar todos los productos",
            description = "Obtiene una lista de todos los productos disponibles en el sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Producto.class)))
            }
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')") // Permitir acceso a ADMIN y USER
    public List<Producto> listarTodosLosProductos() {
        return productoServicio.listarTodosLosProductos();
    }

    @Operation(
            summary = "Crear un nuevo producto",
            description = "Crea un nuevo producto con los detalles proporcionados, incluyendo su categoría.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Producto.class))),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. datos faltantes o incorrectos)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')") // Solo ADMIN puede crear productos
    public Producto guardarProducto(@Valid @RequestBody Producto producto) {
        return productoServicio.guardarProducto(producto);
    }

    @Operation(
            summary = "Obtener producto por ID",
            description = "Obtiene los detalles de un producto específico utilizando su ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Producto encontrado exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Producto.class))),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetalles.class)))
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')") // Permitir acceso a ADMIN y USER
    public ResponseEntity<Producto> obtenerProductoPorId(
            @Parameter(description = "ID del producto a obtener", required = true, example = "1") @PathVariable Long id) {
        Producto producto = productoServicio.obtenerProductoPorId(id);
        return ResponseEntity.ok(producto);
    }

    @Operation(
            summary = "Actualizar producto",
            description = "Actualiza la información de un producto existente utilizando su ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Producto.class))),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. datos faltantes o incorrectos)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetalles.class)))
            }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo ADMIN puede actualizar productos
    public ResponseEntity<Producto> actualizarProducto(
            @Parameter(description = "ID del producto a actualizar", required = true, example = "1") @PathVariable Long id,
            @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Objeto producto actualizado", required = true,
                    content = @Content(schema = @Schema(implementation = Producto.class)))
            @RequestBody Producto detallesProducto) {
        Producto productoActualizado = productoServicio.actualizarProducto(id, detallesProducto);
        return ResponseEntity.ok(productoActualizado);
    }

    @Operation(
            summary = "Eliminar producto",
            description = "Elimina un producto específico por su ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetalles.class)))
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo ADMIN puede eliminar productos
    public ResponseEntity<Map<String, Boolean>> eliminarProducto(
            @Parameter(description = "ID del producto a eliminar", required = true, example = "1") @PathVariable Long id) {
        productoServicio.eliminarProducto(id);
        Map<String, Boolean> respuesta = new HashMap<>();
        respuesta.put("eliminado", Boolean.TRUE);
        return ResponseEntity.ok(respuesta);
    }
}