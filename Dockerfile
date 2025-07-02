# --- ETAPA DE CONSTRUCCIÓN (BUILD STAGE) ---
# Usa una imagen de Java con Maven para compilar tu aplicación
# CAMBIA ESTA LÍNEA:
FROM maven:3.9-openjdk-17 AS build

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo pom.xml para descargar las dependencias primero
COPY pom.xml .

# Descarga las dependencias de Maven
RUN mvn dependency:go-offline

# Copia todo el código fuente de la aplicación
COPY src ./src

# Construye la aplicación y genera el JAR
RUN mvn clean install -DskipTests

# --- ETAPA DE EJECUCIÓN (RUNTIME STAGE) ---
# Usa una imagen base más ligera solo con JRE para ejecutar la aplicación
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el JAR compilado desde la etapa de construcción anterior
COPY --from=build /app/target/tarea_Spring-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto que usa tu aplicación Spring Boot (por defecto 8080)
EXPOSE 8080

# Comando para ejecutar la aplicación cuando el contenedor se inicie
ENTRYPOINT ["java", "-jar", "app.jar"]