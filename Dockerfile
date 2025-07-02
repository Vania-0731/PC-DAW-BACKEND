# Usa una imagen base de Java optimizada para Spring Boot
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR de tu aplicación al contenedor
# Asegúrate de que el nombre del JAR aquí coincida exactamente con el que se
COPY target/tarea_Spring-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto que usa tu aplicación Spring Boot (por defecto 8080)
EXPOSE 8080

# Comando para ejecutar la aplicación cuando el contenedor se inicie
ENTRYPOINT ["java", "-jar", "app.jar"]