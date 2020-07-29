FROM openjdk:15-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /app
COPY target/*.jar /app
ENTRYPOINT ["java", "-jar", "jupiter-backend.jar"]
