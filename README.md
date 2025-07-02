# PC-DAW-BACKEND 🚀

**Parte del Proyecto Principal:** [https://github.com/Vania-0731/PC4-DAW](https://github.com/Vania-0731/PC4-DAW)

Bienvenida al backend de la aplicación PC-DAW (Programación Concurrente y Distribuida de Aplicaciones Web). Este proyecto proporciona la API RESTful para gestionar productos, categorías y autenticación de usuarios, utilizando Spring Boot y PostgreSQL.

---

## 📋 Tabla de Contenidos

1.  [Acerca del Proyecto](#-acerca-del-proyecto)
2.  [Tecnologías Utilizadas](#-tecnologías-utilizadas)
3.  [Funcionalidades de la API](#-funcionalidades-de-la-api)
4.  [Configuración del Entorno Local](#-configuración-del-entorno-local)
5.  [Despliegue en Render](#-despliegue-en-render)
6.  [Endpoints de la API (Documentación OpenAPI/Swagger)](#-endpoints-de-la-api-documentación-openapiswagger)
7.  [Consideraciones de Seguridad](#-consideraciones-de-seguridad)
8.  [Licencia](#-licencia)
9.  [Contacto](#-contacto)

---

## 💡 Acerca del Proyecto

Este proyecto es la parte de la API que sirve como cerebro para la aplicación web. Expone endpoints para las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) de productos y categorías, además de un robusto sistema de autenticación y autorización basado en JWT (JSON Web Tokens) con roles de usuario (ADMIN, USER).

### Características Clave:

* **API RESTful:** Interfaz bien definida para interactuar con los recursos.
* **Gestión de Productos:** CRUD completo para productos, incluyendo su relación con categorías.
* **Gestión de Categorías:** CRUD para organizar los productos.
* **Autenticación JWT:** Seguridad robusta para proteger los endpoints.
* **Autorización Basada en Roles:** Diferentes niveles de acceso (ADMIN, USER).
* **Base de Datos Relacional:** Persistencia de datos con PostgreSQL.

---

## 🛠️ Tecnologías Utilizadas

* **Java 17:** Lenguaje de programación.
* **Spring Boot 3.3.0:** Framework principal para construir la API.
    * **Spring Data JPA:** Para la interacción con la base de datos.
    * **Spring Security:** Para la autenticación y autorización (JWT).
    * **Spring Web:** Para construir APIs RESTful.
* **PostgreSQL:** Base de datos relacional.
* **Maven:** Herramienta de gestión de proyectos y dependencias.
* **Lombok:** Para reducir el código boilerplate (getters, setters, constructores, etc.).
* **JWT (JSON Web Tokens):** Para la seguridad basada en tokens.
* **Swagger/OpenAPI:** Para la documentación interactiva de la API.
* **Render:** Plataforma de despliegue en la nube.

---

## 🚀 Funcionalidades de la API

Los endpoints principales incluyen:

* **`/api/v1/auth/register`**: Registro de nuevos usuarios (se pueden asignar roles si el usuario que registra tiene permisos, o por defecto `ROLE_USER`).
* **`/api/v1/auth/login`**: Inicio de sesión y obtención de token JWT.
* **`/api/v1/productos`**: Gestión de productos (CRUD).
* **`/api/v1/categorias`**: Gestión de categorías (CRUD).
* **`/api/v1/users`**: Gestión de usuarios (CRUD y gestión de roles, generalmente restringido a `ROLE_ADMIN`).

---

## 💻 Configuración del Entorno Local

Para levantar el proyecto en tu máquina local:

1.  **Clona el repositorio:**
    ```bash
    git clone [https://github.com/Vania-0731/PC-DAW-BACKEND.git](https://github.com/Vania-0731/PC-DAW-BACKEND.git)
    cd PC-DAW-BACKEND
    ```

2.  **Configura PostgreSQL:**
    * Asegúrate de tener una instancia de PostgreSQL en ejecución (puedes usar Docker, un servicio local, etc.).
    * Crea una nueva base de datos para el proyecto (ej. `pc_daw_db_local`).

3.  **Configura `application.properties` (o `application.yml`):**
    Crea o modifica el archivo `src/main/resources/application.properties` (o `application.yml`) con la configuración de tu base de datos local:

    ```properties
    # application.properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/pc_daw_db_local
    spring.datasource.username=tu_usuario_postgres
    spring.datasource.password=tu_contraseña_postgres
    spring.datasource.driver-class-name=org.postgresql.Driver
    spring.jpa.hibernate.ddl-auto=update # o create si quieres que las tablas se creen automáticamente cada vez que inicies
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

    # Configuración JWT (cambia esta clave secreta por una más compleja en producción)
    app.jwt-secret=tuClaveSecretaParaJWTQueSeaLargaYCompleja
    app.jwt-expiration-milliseconds=604800000 # 7 días
    ```
    **Importante:** Asegúrate de que `tuClaveSecretaParaJWTQueSeaLargaYCompleja` sea la misma que uses en tus variables de entorno de Render para `JWT_SECRET`.

4.  **Inicialización de Datos (Usuario Admin):**
    El proyecto incluye una clase `DataInitializer` que creará automáticamente un usuario administrador (`username: Vania`, `password: Vania123*`, `email: sonalysifuentes@gmail.com`) y los roles necesarios (`ROLE_ADMIN`, `ROLE_USER`) la primera vez que se ejecute en una base de datos vacía. No necesitas hacer nada manual aquí, solo asegúrate de que el código `DataInitializer` esté presente.

5.  **Ejecuta la aplicación:**
    Puedes ejecutar la aplicación desde tu IDE (IntelliJ IDEA, Eclipse) o usando Maven:
    ```bash
    mvn spring-boot:run
    ```

La API estará disponible en `http://localhost:8080`.

---

## ☁️ Despliegue en Render

Este backend está configurado para un despliegue continuo en Render.

### Pasos para Desplegar:

1.  **Base de Datos PostgreSQL en Render:**
    * Crea un nuevo servicio **PostgreSQL** en Render.
    * Asegúrate de copiar el `Host`, `User`, `Password` y `Database Name` generados.

2.  **Servicio Web de Spring Boot en Render:**
    * Crea un nuevo servicio **Web Service** en Render.
    * Conecta tu repositorio de GitHub (`https://github.com/Vania-0731/PC-DAW-BACKEND.git`).
    * Configura las siguientes **variables de entorno** en la sección `Environment`:
        * `SPRING_DATASOURCE_URL`: `jdbc:postgresql://[RENDER_DB_HOST]/[RENDER_DB_NAME]?sslmode=require`
        * `SPRING_DATASOURCE_USERNAME`: `[RENDER_DB_USER]`
        * `SPRING_DATASOURCE_PASSWORD`: `[RENDER_DB_PASSWORD]`
        * `JWT_SECRET`: `tuClaveSecretaParaJWTQueSeaLargaYCompleja` (Debe ser la misma que en local, o una única definida aquí).
        * `PORT`: `8080` (Opcional, Render lo suele detectar)
    * **Build Command:** `mvn clean install`
    * **Start Command:** `java -jar target/tarea_spring-0.0.1-SNAPSHOT.jar` (verifica que el nombre del JAR coincida con el de tu proyecto).

3.  **Configuración de CORS:**
    * Para permitir que tu frontend (desplegado en Vercel) acceda a tu backend, las anotaciones `@CrossOrigin` en tus controladores deben apuntar a la URL de tu frontend.
    * Ejemplo en tus controladores: `@CrossOrigin(origins = "https://pc-daw-frontend.vercel.app/")`

4.  **Redeploy:**
    Render automáticamente desplegará tu aplicación. La URL de tu backend será algo como `https://pc-daw-backend-app.onrender.com`.

---

## 📚 Endpoints de la API (Documentación OpenAPI/Swagger)

Una vez que el backend esté ejecutándose (localmente o en Render), puedes acceder a la documentación interactiva de la API (Swagger UI):

* **Local:** `http://localhost:8080/swagger-ui.html`
* **Desplegado en Render:** `https://pc-daw-backend-app.onrender.com/swagger-ui.html`

Esta interfaz te permitirá explorar todos los endpoints disponibles, sus parámetros, modelos de respuesta y probar las peticiones.

---

## 🔒 Consideraciones de Seguridad

* **Clave Secreta JWT:** La clave `app.jwt-secret` (o `JWT_SECRET` en Render) debe ser una cadena larga, aleatoria y compleja. **¡Nunca la expongas públicamente!**
* **Contraseñas:** Las contraseñas de los usuarios se almacenan encriptadas usando `BCryptPasswordEncoder`.
* **CORS:** La configuración `@CrossOrigin` restringe los orígenes permitidos para las solicitudes HTTP.

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Consulta el archivo `LICENSE` para más detalles.

---

## ✉️ Contacto

Para cualquier consulta o sugerencia, no dudes en contactar a:

* Sonaly Sifuentes - [sonalysifuentes@gmail.com](mailto:sonalysifuentes@gmail.com)
* GitHub: [Vania-0731](https://github.com/Vania-0731)

---

