# Stage 1: Build the application
FROM openjdk:17-slim AS build
WORKDIR /app

# Copy the Maven Wrapper and pom.xml
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Make the Maven Wrapper script executable
RUN chmod +x ./mvnw

# Fetch all dependencies
RUN ./mvnw dependency:go-offline

# Copy the source code and build the application
COPY src src
RUN ./mvnw package -DskipTests

# Stage 2: Create the final image
FROM openjdk:17
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]