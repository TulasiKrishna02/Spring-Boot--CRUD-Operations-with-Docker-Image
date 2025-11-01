# 1️ Use base image (JDK 17)
FROM openjdk:17-jdk-slim

# 2️ Set working directory inside container
WORKDIR /app

# 3️ Copy jar file into container (Maven build తర్వాత target లో ఉంటుంది)
COPY target/springBoot-CRUD-Operations-0.0.1-SNAPSHOT.jar app.jar

# 4️ Expose application port
EXPOSE 8080

# 5️ Start the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
