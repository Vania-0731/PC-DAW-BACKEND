package com.tecsup.tarea_spring.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API de Gestión de Productos y Categorías",
                version = "1.0",
                description = "Documentación de la API RESTful para la gestión de productos y categorías. " +
                        "Permite realizar operaciones CRUD completas sobre ambas entidades.",
                contact = @Contact(
                        name = "Sifuentes Carranza Sonaly Vania",
                        email = "sonaly.sifuentes@tecsup.edu.pe",
                        url = "https://github.com/Vania-0731/practica4_DAW.git" // O el repositorio del proyecto
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Servidor Local de Desarrollo"),
        }
)
public class OpenApiConfig {
    // No se necesita código adicional aquí, las anotaciones hacen el trabajo
}