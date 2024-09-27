# Use a base image with Java
FROM eclipse-temurin:19-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file from the build directory to the container
COPY build/libs/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]