# Imagen base con OpenJDK 17
FROM eclipse-temurin:17-jdk-alpine

# Crear directorio de la app
WORKDIR /app

# Copiar todo el código
COPY . .

# Dar permisos al wrapper y compilar
RUN chmod +x mvnw && ./mvnw -DskipTests clean package

# Exponer el puerto
EXPOSE 8080

# Ejecutar el JAR
CMD ["java", "-Dserver.port=8080", "-jar", "target/appsegura-0.0.1-SNAPSHOT.jar"]
