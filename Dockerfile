FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/Library-0.0.1-SNAPSHOT.jar /app/Library-0.0.1-SNAPSHOT.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "Library-0.0.1-SNAPSHOT.jar"]