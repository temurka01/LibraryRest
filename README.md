## Для запуска приложения нужно:

1. Установить Docker Desktop на устройство
2. Два раза нажать ctrl и ввести команду:
   mvn clean test package
3. Запустить файл docker-compose.yaml
4. Приложение запущено!!! Работает на порту 8090

## Эндпоинты:

1. Доступные всем пользователям:
    - /sign-up - post запрос для регистрации, возвращает jwt, необходимо указать:
        - username - логин
        - email - емэйл
        - password - пароль
    - /sign-in - post запрос для аутентификации, возвращает jwt, необходимо указать:
        - username - логин
        - password - пароль
2. Доступные только пользователям с ролью ADMIN:
    - /add-book - post запрос для добавления книги в бд, необходимо указать:
        - title - название книги
        - name - имя автора
        - country - страна автора
        - releaseYear - год выпуска
        - amount - количество экземпляров
    - /delete-book/{bookId} - delete запрос для удаления книги по bookId, необходимо указать:
        - bookId - id книги
    - /delete-account/{id} - delete запрос для удаления аккаунта по id, необходимо указать:
        - id - id аккаунта
    - /make-admin/{id} - get запрос для выдачи роли админа пользователю, необходимо указать:
        - id - id аккаунта
3. Доступные всем аутентифицированным пользователям:
    - /books - get запрос для просмотра всех книг в бд, возвращает список всех книг
    - /books/{bookId} - get запрос для просмотра книги по id, возвращает сущность книги, необходимо указать:
        - bookId - id книги
    - /borrow/{bookId} - get запрос для выдачи книги, возвращает запись об этой выдаче из бд, необхобимо указать:
        - bookId - id книги
    - /return/{bookId} - get запрос для возврата книги, возвращает запись об этом возврате из бд, необходимо указать:
        - bookId - id книги

## Описание

Сервис библиотеки, позволяет пользователям брать книги в аренду и возвращать их.
Написан по паттерну mvc