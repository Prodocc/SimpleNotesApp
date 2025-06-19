Simple Notes API

Simple Notes API — это простое RESTful веб-приложение для управления текстовыми заметками, созданное с использованием Spring Boot.

Технологии:

    Java 17, Spring Boot 3.x, Spring (WEB, DATA JPA), h2 database, lombok, gradle, JUnit 5, Mockito

Функционал

Приложение предоставляет полный набор CRUD-операций для работы с заметками. Каждая заметка имеет id, title (уникальный заголовок), text (содержимое), updatedAt (дата последнего обновления).
API Эндпоинты

Базовый URL для всех запросов: /api/notes

    GET	/               Получить список всех заметок.	                                      - 200 OK + List<Note>

    GET	/{title}	Получить одну заметку по ее заголовку.	                              -	200 OK + Note

    POST	/	        Создать новую заметку.	{"title": "string", "text": "string"}         -	201 Created + Note

    PUT	/{oldTitle}	Обновить существующую заметку.	{"title": "string", "text": "string"} -	200 OK + Note

    DELETE	/{title}	Удалить заметку по ее заголовку.	                              -	204 No Content

Пример ответа для GET /api/notes/{title}:

    {
    "id": 1,
    "title": "My First Note",
    "text": "This is a sample note.",
    "updatedAt": "2025-06-17T10:05:00.000000"
    }

Как запустить проект

Требования

    JDK 17 или выше.

    Gradle.

Инструкция по запуску

    Клонируйте репозиторий:
          
    git clone https://github.com/Prodocc/SimpleNotesApp.git
    cd SimpleNotesApp

Соберите и запустите проект с помощью Gradle:

    ./gradlew bootRun

Приложение будет доступно по адресу http://localhost:8080.

Доступ к H2 Console

После запуска приложения вы можете получить доступ к веб-консоли базы данных H2, чтобы просматривать таблицы и выполнять SQL-запросы.

    URL: http://localhost:8080/h2-console

    JDBC URL: jdbc:h2:mem:notes (важно убедиться, что это значение совпадает со значением в application.properties)

    Username: sa

    Password: (оставьте пустым)

Нажмите "Connect", чтобы войти.
Тестирование

Проект имеет высокое тестовое покрытие. Чтобы запустить все тесты, выполните команду:

    ./gradlew test
