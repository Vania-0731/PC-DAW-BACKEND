package com.tecsup.tarea_spring.controlador;

import com.tecsup.tarea_spring.modelo.Categoria;
import com.tecsup.tarea_spring.service.CategoriaServicio;
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
@RequestMapping("/api/v1/categorias")
@CrossOrigin(origins = "https://pc-daw-frontend.vercel.app/")
@Tag(name = "Categorías", description = "Operaciones de gestión de categorías")
public class CategoriaControlador {

    @Autowired
    private CategoriaServicio categoriaServicio;

    @Operation(
            summary = "Listar todas las categorías",
            description = "Obtiene una lista de todas las categorías disponibles en el sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Categoria.class)))
            }
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')") // Permitir acceso a ADMIN y USER
    public List<Categoria> listarTodasLasCategorias() {
        return categoriaServicio.listarTodasLasCategorias();
    }

    @Operation(
            summary = "Crear una nueva categoría",
            description = "Crea una nueva categoría con el nombre proporcionado.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. nombre vacío, fuera de rango)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')") // Solo ADMIN puede crear categorías
    public Categoria guardarCategoria(@Valid @RequestBody Categoria categoria) {
        return categoriaServicio.guardarCategoria(categoria);
    }

    @Operation(
            summary = "Obtener categoría por ID",
            description = "Obtiene los detalles de una categoría específica utilizando su ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categoría encontrada exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
                    @ApiResponse(responseCode = "404", description = "Categoría no encontrada",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetalles.class)))
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')") // Permitir acceso a ADMIN y USER
    public ResponseEntity<Categoria> obtenerCategoriaPorId(
            @Parameter(description = "ID de la categoría a obtener", required = true, example = "1") @PathVariable Long id) {
        Categoria categoria = categoriaServicio.obtenerCategoriaPorId(id);
        return ResponseEntity.ok(categoria);
    }

    @Operation(
            summary = "Actualizar categoría",
            description = "Actualiza la información de una categoría existente utilizando su ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. nombre vacío, fuera de rango)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
                    @ApiResponse(responseCode = "404", description = "Categoría no encontrada",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetalles.class)))
            }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo ADMIN puede actualizar categorías
    public ResponseEntity<Categoria> actualizarCategoria(
            @Parameter(description = "ID de la categoría a actualizar", required = true, example = "1") @PathVariable Long id,
            @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Objeto categoría actualizado", required = true,
                    content = @Content(schema = @Schema(implementation = Categoria.class)))
            @RequestBody Categoria detallesCategoria) {
        Categoria categoriaActualizada = categoriaServicio.actualizarCategoria(id, detallesCategoria);
        return ResponseEntity.ok(categoriaActualizada);
    }

    @Operation(
            summary = "Eliminar categoría",
            description = "Elimina una categoría específica por su ID. Si la categoría tiene productos asociados, estos serán reasignados a la categoría por defecto.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categoría eliminada exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
                    @ApiResponse(responseCode = "404", description = "Categoría no encontrada",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetalles.class))),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor (ej. al intentar eliminar la categoría por defecto)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetalles.class)))
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo ADMIN puede eliminar categorías
    public ResponseEntity<Map<String, Boolean>> eliminarCategoria(
            @Parameter(description = "ID de la categoría a eliminar", required = true, example = "1") @PathVariable Long id) {
        categoriaServicio.eliminarCategoria(id);
        Map<String, Boolean> respuesta = new HashMap<>();
        respuesta.put("eliminada", Boolean.TRUE);
        return ResponseEntity.ok(respuesta);
    }
}