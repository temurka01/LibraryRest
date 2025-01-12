# Используем официальный образ OpenJDK
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию в контейнере
WORKDIR /app

# Копируем JAR файл приложения в контейнер
COPY target/Library-0.0.1-SNAPSHOT.jar /app/Library-0.0.1-SNAPSHOT.jar

# Устанавливаем переменные окружения для базы данных
ENV DB_HOST=localhost
ENV DB_PORT=5432
ENV DB_NAME=mydatabase
ENV DB_USER=user
ENV DB_PASSWORD=password

# Открываем порт для приложения (например, 8080)
EXPOSE 8090

#ENTRYPOINT["java","-jar","target/Library-0.0.1-SNAPSHOT.jar"]
# Команда для запуска приложения
CMD ["java", "-jar", "Library-0.0.1-SNAPSHOT.jar"]